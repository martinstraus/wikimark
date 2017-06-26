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

import java.util.Set;

/**
 * Super-simple abstract syntax tree for the parsed page file.
 *
 * @author Martín Straus
 */
public class AST {

    private final String title;
    private final String author;
    private final Set<String> keywords;
    private final String content;

    public AST(String title, String author, Set<String> keywords, String content) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.content = content;
    }

    public String title() {
        return title;
    }

    public String author() {
        return author;
    }

    public Set<String> keywords() {
        return keywords;
    }

    public String content() {
        return content;
    }

    public boolean equalTo(AST page) {
        return this.title.equals(page.title)
            && this.author.equals(page.author)
            && this.keywords.containsAll(page.keywords) && page.keywords.containsAll(this.keywords)
            && this.content.equals(page.content);
    }

    @Override
    public String toString() {
        return "AST{" + "title=" + title + ", author=" + author + ", keywords=" + keywords + ", content=" + content + '}';
    }

}
