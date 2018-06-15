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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 *
 * @author Martín Straus
 */
public class Page {

    public static Comparator<Page> SORT_BY_TITLE = (Page a, Page b) -> a.title.compareTo(b.title);

    private final String name;
    private final String title;
    private final String author;
    private final String content;
    private final Set<String> keywords;
    private final Instant creationTime;

    public Page(String name, String title, String author, String content, Set<String> keywords, Instant creationTime) {
        this.name = name;
        this.title = title;
        this.author = author;
        this.content = content;
        this.keywords = new HashSet<>(keywords);
        this.creationTime = creationTime;
    }

    public Document asLuceneDocument() {
        final Document document = new Document();
        document.add(new Field("name", name, TextField.TYPE_STORED));
        document.add(new Field("title", title, TextField.TYPE_STORED));
        document.add(new Field("author", author, TextField.TYPE_STORED));
        keywords.forEach((k) -> document.add(new Field("keyword", k, TextField.TYPE_STORED)));
        document.add(new Field("content", content, TextField.TYPE_STORED));
        document.add(new Field(
            "creationDate",
            DateTools.timeToString(creationTime.getEpochSecond(), DateTools.Resolution.MINUTE),
            StringField.TYPE_STORED
        ));
        document.add(new NumericDocValuesField("creationDate", creationTime.getEpochSecond()));
        return document;
    }

    public String url() {
        return name;
    }

    public String urlRelativeToHost(Context context) {
        return context.urlRelativeToHost("/pages/".concat(name));
    }

    public Page update(Pages pages) {
        pages.update(name, title, author, content, keywords);
        return this;
    }

    public PageContext pageContext() {
        return new PageContext() {

            @Override
            public String name() {
                return name;
            }

            @Override
            public String title() {
                return title;
            }

            @Override
            public String author() {
                return author;
            }

            @Override
            public String content() {
                return new CommonMark().format(content);
            }

            @Override
            public String rawContent() {
                return content;
            }

            @Override
            public List<String> keywords() {
                return new ArrayList<>(keywords);
            }

            @Override
            public String url(Context appContext) {
                return urlRelativeToHost(appContext);
            }

            public Instant creationTime() {
                return creationTime;
            }

        };
    }

    public Page update(Pages pages, String title, String content, Set<String> keywords) {
        pages.update(name, title, author, content, keywords);
        return this;
    }

}
