<%@taglib uri="/WEB-INF/tld/wikimark" prefix="wm"%>
<!DOCTYPE html>
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
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <title>${page.title()} - Edit</title>
        <jsp:include page="/WEB-INF/pages/styles.jsp"/>
    </head>
    <body>
        <jsp:include page="/WEB-INF/pages/navigation.jsp"/>
        <div class="container">
            <h1>Edit page</h1>   
            <form action="${pageContext.request.contextPath}/pages${page.name()}" method="post">
                <input type="hidden" name="_method" value="put">
                <div class="row">
                    <div class="six columns">
                        <label for="title">Title</label>
                        <input type="text" id="title" name="title" class="u-full-width" value="${page.title()}">
                    </div>
                    <div class="six columns">
                        <label for="name">User-friendly URL</label>
                        <input type="text" id="name" name="name" class="u-full-width" value="${page.name()}">
                    </div>
                </div>
                <label for="keywords">Keywords</label>
                <em>A comma-separated list of keywords for this page.</em>
                <input type="text" id="keywords" name="keywords" class="u-full-width" value="${wm:separatedBy(page.keywords(),',')}">
                <label for="content">Content</label>
                <em>Write the content of the page using <a href="http://commonmark.org" target="_blank">CommonMark</a> syntax.</em>
                <textarea id="content" name="content" class="u-full-width editor">${page.rawContent()}</textarea>
                <button type="submit" class="button-primary">Save changes</button>
            </form>
        </div>
    </body>
</html>