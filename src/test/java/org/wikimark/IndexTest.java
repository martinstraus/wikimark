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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Martín Straus
 */
public class IndexTest {

    @Test
    public void searchReturnsMatchingPages() throws IOException, ParseException {
        final Path tempdir = Files.createTempDirectory(".wikimark-it");
        final Pages pages = new Pages(tempdir.toFile(), new DummyTemplate(), new DummyTemplate(), Charset.forName("UTF-8"));
        pages.create("page-1.md", "Expected page", "Author of the Page", "# Expected page", new HashSet<>(asList("indexed")));
        pages.create("page-2.md", "Unexpected page", "Author of the Page", "# Unexpected page", Collections.EMPTY_SET);
        final List<Document> found = new Index(pages.all()).search("expected", 5);
        assertThat("documents found in " + tempdir.toAbsolutePath().toString(), found, hasSize(1));
    }
}
