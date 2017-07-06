<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Wikimark</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css?family=Work+Sans" rel="stylesheet"> 
        <link rel="stylesheet" type="text/css" href="css/normalize.css">
        <link rel="stylesheet" type="text/css" href="css/skeleton.css">
        <link rel="stylesheet" type="text/css" href="css/wikimark.css"/>
    </head>
    <body class="index">
        <div class="container">
            <h1>Wikimark</h1>
            <c:if test="${empty pageContext.request.userPrincipal}">
                You can <a href="login.jsp">log in</a> if you have an account.
            </c:if>
            <c:if test="${not empty pageContext.request.userPrincipal}">
                <p>Welcome, <%= ((javax.servlet.http.HttpServletRequest) pageContext.getRequest()).getUserPrincipal().getName() %>.</p>
                <p>You can <a href="new-page">create a new page</a>.</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/pages" method="get">
                <input type="text" name="query" class="u-full-width">
                <button type="submit" class="button-primary">Search</button>
            </form>
            <div class="row">
                <div class="four columns">
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
    </body>
</html>
