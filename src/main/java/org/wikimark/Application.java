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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Martín Straus
 */
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            final Context context = new Context(sce.getServletContext());
            final File root = new File(System.getProperty("user.home"), ".wikimark");
            
            if (!root.exists()) {
                root.mkdir();
            }
            
            final Pages pages = new Pages(new File(root, "pages"), Charset.forName("UTF-8"));
            
            context
                .registerServlet("Page",
                    new PageServlet(context, pages),
                    "/pages/*"
                )
                .registerServlet("LogIn", new LogInServlet(), "/login")
                .registerServlet("Create page", new NewPageServlet(context, pages), "/new-page")
                .registerServlet("index", new IndexServlet(pages), "/index.html")
                .registerServlet(
                    "Attachments", 
                    new AttachmentServlet(new File(root, "attachments").toPath()), 
                    new MultipartConfigElement(new File(root, "attachments").getAbsolutePath()),
                    "/attachments/*"
                );

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
