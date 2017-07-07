<%--
The MIT License

Copyright 2017 Wikimark.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/wikimark" prefix="wm"%>
<!DOCTYPE html>
<html>
    <head>
        <title>${page.title()}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="author" content="${page.author()}">
        <meta name="keywords" content="${wm:separatedBy(page.keywords(),',')}">
        <meta name="generator" content="Wikimark">
        <jsp:include page="/WEB-INF/pages/styles.jsp"/>
    </head>
    <body class="page">
        <jsp:include page="/WEB-INF/pages/navigation.jsp"/>
        <div class="container">
            <article>
                <header>
                        <h1>${page.title()}</h1>
                        <div class="page-info">
                            <p>Created by <a href="../pages?query=${page.author()}">${page.author()}</a> on ${wm:instantToString(page.creationTime())}</p>
                            <ul class="tags">
                                <c:forEach items="${page.keywords()}" var="keyword">
                                <li><a href="../pages?query=$keyword">${keyword}</a></li>
                                </c:forEach>
                            <ul>
                        </div>
                </header>
                ${page.content()}
            </article>
        </div>
    </body>
</html>
