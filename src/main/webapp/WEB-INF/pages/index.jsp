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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Wikimark</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="/WEB-INF/pages/styles.jsp"/>
    </head>
    <body class="index">
        <jsp:include page="/WEB-INF/pages/navigation.jsp"/>
        <main>
            <div class="container">
                <h1>Wikimark</h1>
                <form action="${pageContext.request.contextPath}/pages" method="get">
                    <div class="row">
                        <div class="ten columns">
                            <input type="text" name="query" class="u-full-width" placeholder="Enter your seach here...">
                        </div>
                        <div class="two columns">
                            <button type="submit" class="button-primary">Search</button>
                        </div>
                    </div>
                </form>
                <div class="row">
                    <div class="six columns">
                        <div class="panel with-border">
                            <h3>Latest pages</h3>
                            <c:if test="${latestPages.isEmpty()}">
                                <p>There are no pages... yet. <a href="pages/new-page">Create one!</a></p>
                            </c:if>
                            <c:if test="${not latestPages.isEmpty()}">
                                <ol class="search-results">
                                    <c:forEach items="${latestPages}" var="page">
                                        <li>
                                            <a href="${page.url(context)}">${page.title()}</a> <span class="author">by ${page.author()}</span>
                                        </li>
                                    </c:forEach>
                                </ol>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>
