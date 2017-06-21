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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Martín Straus
 */
public class Pages {

    private final File root;

    public Pages(File root) {
        this.root = root;
    }

    public Optional<FilePage> find(String name) {
        final File file = new File(root, name);
        return file.exists() ? Optional.of(new FilePage(file)) : Optional.empty();
    }

    public FilePage create(String name, String content) {
        return new FilePage(saveOrUpdate(newOrExistingFile(name), content));
    }

    private File newOrExistingFile(String name) {
        final File file = new File(root, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException("Could not create file " + name);
            }
        }
        return file;
    }

    private File saveOrUpdate(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            return file;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
