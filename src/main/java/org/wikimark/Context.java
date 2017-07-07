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

import static java.util.Arrays.asList;
import java.util.function.Consumer;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 *
 * @author Martín Straus
 */
public class Context {

    private final ServletContext context;

    public Context(ServletContext context) {
        this.context = context;
    }

    public Context registerServlet(String name, Servlet servlet, String... mappings) {
        return this.registerServlet(name, servlet, null, mappings);
    }

    public Context registerServlet(String name, Servlet servlet, MultipartConfigElement multipartConfig, String... mappings) {
        final ServletRegistration.Dynamic registration = context.addServlet(name, servlet);
        if (multipartConfig != null) {
            registration.setMultipartConfig(multipartConfig);
        }
        registration.addMapping(mappings);
        return this;
    }

    public Context registerFilter(String name, Filter filter, Consumer<FilterRegistration.Dynamic>... registrations) {
        FilterRegistration.Dynamic registracion = context.addFilter(name, filter);
        asList(registrations).forEach((c) -> c.accept(registracion));
        return this;
    }

    public String baseURL() {
        return context.getContextPath();
    }

    public String urlRelativeToHost(String url) {
        return context.getContextPath() + url;
    }

}
