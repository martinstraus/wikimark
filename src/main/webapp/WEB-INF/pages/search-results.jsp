<%-- 
    Document   : search-results
    Created on : Jul 5, 2017, 9:15:18 PM
    Author     : Martín Straus
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/wikimark" prefix="wm"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="description" content="A new wiki page">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <title>Search results - WikiMark</title>
        <link href="https://fonts.googleapis.com/css?family=Work+Sans" rel="stylesheet"> 
        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/skeleton.css">
        <link rel="stylesheet" href="css/wikimark.css">
    </head>
    <body class="search-results">
        <div class="container">
            <h1>Wikimark</h1>
            <h2>Search results</h2>
            <ol class="search-results">
            <c:forEach items="${found}" var="page">
                <li>
                    <a href="${page.url(context)}">${page.title()}</a> <span class="author">by ${page.author()}</span>
                </li>
            </c:forEach>
            </ol>
        </div>
    </body>
</html>
