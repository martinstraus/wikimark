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

import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Martín Straus
 */
final class EditPageForm {
    
    private final HttpServletRequest request;
    private final Pages pages;

    public EditPageForm(HttpServletRequest request, Pages pages) {
        this.request = request;
        this.pages = pages;
    }

    public Page edit(HttpServletResponse resp) {
        final Page page = page().orElseThrow(() -> new IllegalArgumentException());
        page.update(pages, title(), content(), keywords());
        return page;
    }

    private Optional<Page> page() {
        return pages.find(pageName());
    }

    private String pageName() {
        return request.getPathInfo().substring(1, request.getPathInfo().length());
    }

    private String title() {
        return request.getParameter("title");
    }

    private String content() {
        return request.getParameter("content");
    }

    private Set<String> keywords() {
        return new Keywords().fromString(request.getParameter("keywords"));
    }
    
}
