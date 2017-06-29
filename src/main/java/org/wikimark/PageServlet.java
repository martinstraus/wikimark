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
        Optional<Page> page = pages.find(req.getPathInfo());
        if (page.isPresent()) {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(page.get().asHTML().getBytes(Charset.forName("UTF-8")));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final CreatePageForm form = new CreatePageForm(req).validate();
        Page page = pages.create(form.name(), form.title(), req.getUserPrincipal().getName(), form.content(), form.keywords());
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("location", page.urlRelativeToHost(context));
    }

}
