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

import java.io.Closeable;
import java.io.IOException;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.*;

/**
 *
 * @author Martín Straus
 */
public class Index implements Closeable {

    @FunctionalInterface
    private static interface IndexAction {

        void execute(IndexWriter writer) throws IOException;
    }

    private final Directory directory;
    private final Analyzer analyzer;

    public Index(Set<Page> pages) throws IOException {
        directory = new RAMDirectory();
        analyzer = new StandardAnalyzer();
        indexAllPages(pages);
    }

    public void indexAllPages(final Set<Page> pages) throws IOException {
        withNewWriter((writer) -> pages.forEach((page) -> {
            try {
                writer.addDocument(page.asLuceneDocument());
            } catch (IOException ex) {
                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
    }

    public void index(final Page page) {
        withNewWriter((w) -> w.addDocument(page.asLuceneDocument()));
    }

    private void withNewWriter(IndexAction action) {
        try (IndexWriter iwriter = newIndexWriter()) {
            action.execute(iwriter);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private IndexWriter newIndexWriter() throws IOException {
        return new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

    public List<Document> search(final String queryText, final int maxResults) throws IOException, ParseException {
        try (DirectoryReader ireader = DirectoryReader.open(directory)) {
            final IndexSearcher searcher = new IndexSearcher(ireader);
            return search(searcher, queryText, maxResults)
                .stream()
                .map((scoreDoc) -> documentForScore(searcher, scoreDoc))
                .collect(toList());
        }
    }

    private Document documentForScore(IndexSearcher searcher, ScoreDoc scoreDoc) {
        try {
            return searcher.doc(scoreDoc.doc);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<ScoreDoc> search(IndexSearcher searcher, String queryText, int maxResults) throws IOException, ParseException {
        return asList(searcher.search(queryFromText(queryText), maxResults).scoreDocs);
    }

    private Query queryFromText(String queryText) throws ParseException {
        return new MultiFieldQueryParser(
            new String[]{"name", "author", "title", "keywords", "content"},
            analyzer
        ).parse(queryText);
    }

    @Override
    public void close() throws IOException {
        directory.close();
    }

    public List<Document> latest(int maxResults) throws IOException {
        try (DirectoryReader ireader = DirectoryReader.open(directory)) {
            final IndexSearcher searcher = new IndexSearcher(ireader);
            return collectDocuments(
                searcher,
                searcher.search(
                    new MatchAllDocsQuery(),
                    maxResults,
                    new Sort(SortField.FIELD_SCORE, new SortField("creationDate", SortField.Type.DOUBLE, true))
                )
            );
        }
    }

    private List<Document> collectDocuments(final IndexSearcher searcher, final TopFieldDocs foundDocs) {
        return asList(foundDocs.scoreDocs)
            .stream()
            .map((scoreDoc) -> documentForScore(searcher, scoreDoc))
            .collect(toList());
    }

}
