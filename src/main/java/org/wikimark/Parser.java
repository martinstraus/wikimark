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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

/**
 *
 * @author Martín Straus
 */
public class Parser {

    private FileReader reader;

    public AST parse(InputStream input) throws IOException {
        reader = new FileReader(new BufferedReader(new InputStreamReader(input)));
        return new AST(title(), author(), keywords(), content());
    }

    private String title() throws IOException {
        return nextAttribute("title", "");
    }

    private String author() throws IOException {
        return nextAttribute("author", "");
    }

    private Set<String> keywords() throws IOException {
        return new SplitString(nextAttribute("keywords", ""), ",").values();
    }

    private String nextAttribute(String prefix, String defaultValue) throws IOException {
        return reader
            .nextLineStartingWith(prefix)
            .map((s) -> new StringProperty(s).value())
            .orElse(defaultValue);
    }

    /**
     * Whatever the reader provides, as is.
     *
     * @return
     */
    private String content() throws IOException {
        return reader.remainingContext();
    }

}
