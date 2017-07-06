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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public PageServlet(Context context, Pages pages) {
        this.context = context;
        this.pages = pages;
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
        new Request(req)
            .withAttribute("context", context)
            .withAttribute(
                "found", 
                pages.findByTerms(req.getParameter("query"), 20).stream().map((page) -> page.pageContext()).collect(toList())
            )
            .forwardTo("/WEB-INF/pages/search-results.jsp", resp);
    }

    private void showOne(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final Optional<Page> foundPage = new PageRequest(pages, req).page();
        if (foundPage.isPresent()) {
            final Page page = foundPage.get();
            new Request(req)
                .withAttribute("page", page.pageContext())
                .forwardTo("/WEB-INF/pages/page.jsp", resp);
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
