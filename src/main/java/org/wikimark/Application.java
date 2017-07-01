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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 *
 * @author Martín Straus
 */
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            final Context context = new Context(sce.getServletContext());
            final VelocityEngine velocity = velocity();
            final Pages pages = new Pages(
                new File(System.getProperty("user.home"), ".wikimark"),
                new VelocityTemplate(velocity, "org/wikimark/page-template.vsl", context),
                new VelocityTemplate(velocity, "org/wikimark/page-abstract.vsl", context),
                Charset.forName("UTF-8")
            );
            context
                .registerServlet(
                    "Page",
                    new PageServlet(context, pages, velocity.getTemplate("org/wikimark/search-result.vsl")),
                    "/pages/*"
                )
                .registerServlet("LogIn", new LogInServlet(), "/login")
                .registerServlet("Create page", new NewPageServlet(context, pages), "/new-page")
                .registerServlet("index", new IndexServlet(pages), "/index.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private VelocityEngine velocity() {
        VelocityEngine velocity = new VelocityEngine();
        velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        return velocity;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
