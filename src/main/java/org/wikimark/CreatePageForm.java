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

import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Martín Straus
 */
public class CreatePageForm {

    private final HttpServletRequest request;

    public CreatePageForm(HttpServletRequest request) {
        this.request = request;
    }

    public String name() {
        return request.getPathInfo();
    }

    public String content() {
        return request.getParameter("content");
    }

    public String title() {
        return request.getParameter("title");
    }

    public Set<String> keywords() {
        return new Keywords().fromString(request.getParameter("keywords"));
    }

    private String author() {
        return request.getUserPrincipal().getName();
    }

    public Page create(Pages pages) {
        return pages.create(name(), title(), author(), content(), keywords());
    }
}
