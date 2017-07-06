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
import java.util.Collections;
import org.apache.lucene.queryparser.classic.ParseException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Martín Straus
 */
public class PagesTest {

    @Test
    public void fileExistsAfterCreation() throws IOException, ParseException {
        final Path tempdir = Files.createTempDirectory(".wikimark-it");
        final Pages pages = new Pages(tempdir.toFile(), Charset.defaultCharset());
        pages.create("test.md", "", "", "test", Collections.EMPTY_SET);
        assertThat("file for new page exists", tempdir.resolve("test.md").toFile().exists(), is(true));
    }

    @Test
    public void allReturnsAllCreatedPages() throws IOException, ParseException {
        final Path tempdir = Files.createTempDirectory(".wikimark-it");
        final Pages pages = new Pages(tempdir.toFile(), Charset.defaultCharset());
        pages.create("p1.md", "Page 1", "Author", "content", Collections.EMPTY_SET);
        pages.create("p2.md", "Page 2", "Author", "content", Collections.EMPTY_SET);
        assertThat("all pages", pages.all(), hasSize(2));
    }
}
