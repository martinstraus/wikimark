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
import static java.util.Arrays.asList;
import java.util.HashSet;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Martín Straus
 */
public class ParserTest {

    @Test
    public void parsesValidStreamCorrectly() throws IOException {
        assertThat(
            "parsed page",
            new Parser().parse(
                new LinesInputStream(
                    "title=The title",
                    "author=The author",
                    "keywords=k1,k2,k3",
                    "first line",
                    "second line"
                ).inputStream()
            ),
            is(equalTo(new AST(
                "The title",
                "The author",
                new HashSet<>(asList("k1", "k2", "k3")),
                new LinesAsString("first line", "second line").toString())
            ))
        );
    }

    private Matcher<AST> equalTo(AST page) {
        return new TypeSafeMatcher<AST>() {
            @Override
            public boolean matchesSafely(AST t) {
                return t.equalTo(page);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(page);
            }

        };
    }

}
