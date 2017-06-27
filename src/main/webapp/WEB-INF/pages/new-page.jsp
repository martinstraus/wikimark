<!DOCTYPE html>
<!--
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
-->
<html>
    <head>
        <meta charset="utf-8">
        <meta name="description" content="A new wiki page">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <title>Wikimark - New page</title>
        <link rel="stylesheet" href="css/wikimark.css">
    </head>
    <body>
        <div class="container">
            <h1>New page</h1>   
            <form action="${pageContext.request.contextPath}/new-page" method="post">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" oninput="titleChanged()">
                <label for="name">User-friendly URL</label>
                <p class="input-description">All spaces will be replaced with dashes. Every other invalid character will be removed.</p>
                <input type="text" id="name" name="name" oninput="nameEdited()">
                <label for="keywords">Keywords</label>
                <p class="input-description">A comma-separated list of keywords for this page.</p>
                <input type="text" id="keywords" name="keywords">
                <label for="content">Content</label>
                <p class="input-description">Write the content of the page using <a href="http://commonmark.org" target="_blank">CommonMark</a> syntax.</p>
                <textarea id="content" rows="10" name="content"></textarea>
                <button type="submit">Create</button>
            </form>
        </div>
        <script>
            var nameWasEdited = false;
            
            function titleChanged() {
                var name = document.getElementById('name');
                if (name.value === '' || !nameWasEdited) {
                    name.value = transformTitle(document.getElementById('title').value);
                }
            }
            
            function transformTitle(title) {
                return title.replace(' ', '-').toLowerCase();
            }
            
            function nameEdited() {
                nameWasEdited = true;
            }
        </script>
    </body>
</html>