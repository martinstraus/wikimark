/*
 * The MIT License
 *
 * Copyright 2017 Martín Straus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.wikimark;

import java.io.IOException;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 *
 * @author Martín Straus
 */
public class PageServlet extends javax.servlet.http.HttpServlet {

    public static String pageName(HttpServletRequest request) {
        return request.getPathInfo().substring(1, request.getPathInfo().length());
    }

    private final TemplateEngine thymeleaf;
    private final Context context;
    private final Pages pages;

    public PageServlet(ServletContext ctx, TemplateEngine thymeleaf, Context context, Pages pages) {
        this.thymeleaf = thymeleaf;
        this.context = context;
        this.pages = pages;
        ctx.addServlet(PageServlet.class.getName(), this).addMapping("/pages/*");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            search(req, resp);
        } else if (req.getPathInfo().endsWith("/edit")) {
            showEdit(req, resp);
        } else {
            showOne(req, resp);
        }
    }

    private void search(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setHeader("Content-Type", "text/html; charset=utf-8");
        WebContext webContext = new WebContext(req, resp, req.getServletContext());
        webContext.setVariable(
            "pages",
            pages
                .findByTerms(req.getParameter("query"), 20)
                .stream()
                .map((page) -> page.pageContext())
                .collect(toList())
        );
        thymeleaf.process("/search-results", webContext, resp.getWriter());
    }

    private void showOne(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final Optional<Page> foundPage = new PageRequest(pages, req).page();
        if (foundPage.isPresent()) {
            resp.setHeader("Content-Type", "text/html; charset=utf-8");
            final Page page = foundPage.get();
            WebContext webContext = new WebContext(req, resp, req.getServletContext());
            webContext.setVariable("page", page.pageContext());
            thymeleaf.process("/page", webContext, resp.getWriter());
        } else {
            new Response(resp).notFound();
        }
    }

    private void showEdit(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final Optional<Page> page = pageToEdit(req);
        if (!page.isPresent()) {
            new Response(resp).notFound();
        } else {
            WebContext webContext = new WebContext(req, resp, req.getServletContext());
            webContext.setVariable("page", page.get().pageContext());
            resp.setCharacterEncoding("UTF-8");
            thymeleaf.process("/edit-page", webContext, resp.getWriter());
        }
    }

    private Optional<Page> pageToEdit(HttpServletRequest req) {
        return pages.find(req.getPathInfo().substring(0, req.getPathInfo().indexOf("/edit")));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        switch (new Request(req).parameter("_method", "post")) {
            case "post":
                create(req, resp);
                break;
            case "put":
                edit(req, resp);
                break;
            default:
                new Response(resp).badRequest();
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Page page = new CreatePageForm(req).create(pages);
        new Response(resp).redirectTo(page.urlRelativeToHost(context));
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Page page = new EditPageForm(req, pages).edit(resp);
            new Response(resp).redirectTo(page.urlRelativeToHost(context));
        } catch (IllegalArgumentException ex) {
            new Response(resp).notFound();
        }
    }

}
