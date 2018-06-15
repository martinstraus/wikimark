/*
 * The MIT License
 *
 * Copyright 2017 Wikimark.
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 *
 * @author Martín Straus
 */
public class AttachmentServlet extends HttpServlet {

    private static final int BUFFER_SIZE = 1024;

    private static final class PostRequest {

        private final HttpServletRequest request;

        public PostRequest(HttpServletRequest request) {
            this.request = request;
        }

        public Part file() throws IOException, ServletException {
            return request.getPart("file");
        }

    }

    private final Path root;
    private final TemplateEngine thymeleaf;

    public AttachmentServlet(ServletContext ctx, TemplateEngine thymeleaf, File base) {
        this.thymeleaf = thymeleaf;
        File root = new File(base, "attachments");
        root.mkdirs();
        this.root = root.toPath();
        ServletRegistration.Dynamic registration = ctx.addServlet(AttachmentServlet.class.getName(), this);
        registration.addMapping("/attachments/*");
        File temp = new File(base, "temp");
        temp.mkdirs();
        registration.setMultipartConfig(new MultipartConfigElement(temp.getAbsolutePath()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || req.getPathInfo().isEmpty()) {
            WebContext ctx = new WebContext(req, resp, req.getServletContext());
            thymeleaf.process("/new-attachment", ctx, resp.getWriter());
        } else {
            Files.copy(pathRelativeToRoot(req.getPathInfo()), resp.getOutputStream());
        }
    }

    private Path pathRelativeToRoot(String file) {
        return new File(root.toFile(), file).toPath();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final AttachmentServlet.PostRequest attachmentRequest = new PostRequest(request);
        final File file = file(attachmentRequest);
        Files.copy(attachmentRequest.file().getInputStream(), file.toPath());
        response.sendRedirect(request.getContextPath() + "/attachments/" + file.getName());
    }

    private File file(AttachmentServlet.PostRequest request) throws IOException, ServletException {
        return new File(root.toFile(), getFileName(request.file()));
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
