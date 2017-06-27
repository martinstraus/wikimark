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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 *
 * @author Martín Straus
 */
class FileReader {

    private final BufferedReader reader;

    public FileReader(BufferedReader reader) {
        this.reader = reader;
    }

    public Optional<String> nextLineStartingWith(String prefix) throws IOException {
        String line = reader.readLine();
        while (line != null && !line.trim().startsWith(prefix)) {
            line = reader.readLine();
        }
        return Optional.ofNullable(line != null ? line.trim() : null);
    }

    public String remainingContext() throws IOException {
        final StringWriter string = new StringWriter();
        final char[] buffer = new char[1024];
        try (final PrintWriter writer = new PrintWriter(string)) {
            int read = reader.read(buffer);
            while (read > 0) {
                writer.write(buffer, 0, read);
                read = reader.read(buffer);
            }
        }
        return string.toString();
    }

}
