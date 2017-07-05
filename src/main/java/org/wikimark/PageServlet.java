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
import java.nio.charset.Charset;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author Martín Straus
 */
public class PageServlet extends javax.servlet.http.HttpServlet {

    public static String pageName(HttpServletRequest request) {
        return request.getPathInfo().substring(1, request.getPathInfo().length());
    }

    private final Context context;
    private final Pages pages;
    private final org.apache.velocity.Template searchResultPageTemplate;

    public PageServlet(Context context, Pages pages, org.apache.velocity.Template searchResultPageTemplate) {
        this.context = context;
        this.pages = pages;
        this.searchResultPageTemplate = searchResultPageTemplate;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            search(req, resp);
        } else if (req.getPathInfo().endsWith("/edit")) {
            showEdit(req, resp);
        } else {
            findOne(req, resp);
        }
    }

    private void search(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final VelocityContext vc = new VelocityContext();
        vc.put("found", pages.findByTerms(req.getParameter("query"), 20));
        searchResultPageTemplate.merge(vc, resp.getWriter());
    }

    private void findOne(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Page> page = new PageRequest(pages, req).page();
        if (page.isPresent()) {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(page.get().asHTML().getBytes(Charset.forName("UTF-8")));
        } else {
            new Response(resp).notFound();
        }
    }

    private void showEdit(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final Optional<Page> page = new ShowEditRequest(req, pages).page();
        if (!page.isPresent()) {
            new Response(resp).notFound();
        } else {
            new Request(req)
                .withAttribute("page", page.get().pageContext())
                .forwardTo("/WEB-INF/pages/edit-page.jsp", resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
