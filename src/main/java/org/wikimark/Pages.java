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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author Martín Straus
 */
public class Pages {

    private final File root;
    private final Template template;
    private final Template abstractTemplate;
    private final Charset charset;
    private final Index index;

    public Pages(File root, Template template, Template abstractTemplate, Charset charset) throws IOException {
        this.root = root;
        this.template = template;
        this.abstractTemplate = abstractTemplate;
        this.charset = charset;
        this.index = new Index(all());
    }

    public Optional<Page> find(String name) {
        final File file = new File(root, name);
        return file.exists()
            ? Optional.of(loadPageFrom(file, name))
            : Optional.empty();
    }

    private Page loadPageFrom(File file, String name) {
        try (FileInputStream input = new FileInputStream(file)) {
            final AST ast = new Parser().parse(input);
            return new Page(template, abstractTemplate, name, ast.title(), ast.author(), ast.content(), ast.keywords());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Page create(String name, String title, String author, String content, Set<String> keywords) {
        try {
            saveToFile(new java.io.File(root, name), title, author, content, keywords);
            final Page page = new Page(template, abstractTemplate, name, title, author, content, keywords);
            index.index(page);
            return page;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Pages update(String name, String title, String author, String content, Set<String> keywords) {
        try {
            saveToFile(new File(root, name), title, author, content, keywords);
            return this;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveToFile(File file, String title, String author, String content, Set<String> keywords) throws IOException {
        try (final PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
            writer.print("title=");
            writer.println(title);
            writer.print("author=");
            writer.println(author);
            writer.print("keywords=");
            writer.println(keywords.stream().collect(Collectors.joining(",")));
            writer.write(content);
        }
    }

    public Set<Page> all() {
        try {
            return Files.list(root.toPath())
                .filter((p) -> p.toString().endsWith(".md"))
                .map((p) -> p.toFile())
                .map(this::loadPageFrom)
                .collect(toSet());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Page loadPageFrom(File file) {
        return loadPageFrom(file, file.getName());
    }

    public List<Page> findByTerms(String query, int maxResults) {
        try {
            return index
                .search(query, maxResults)
                .stream()
                .map((doc) -> find(doc.get("name")))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        } catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

}
