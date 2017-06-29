<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Wikimark</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css?family=Work+Sans" rel="stylesheet"> 
        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/skeleton.css">
        <link rel="stylesheet" href="css/wikimark.css"/>
    </head>
    <body class="index">
        <div class="container">
            <h1>Wikimark</h1>
            <form action="${pageContext.request.contextPath}/pages" method="get">
                <input type="text" name="query" class="u-full-width">
                <button type="submit" class="button-primary">Search</button>
            </form>
            <c:if test="${empty pageContext.request.userPrincipal}">
                You can <a href="login.jsp">log in</a> if you have an account.
            </c:if>
            <c:if test="${not empty pageContext.request.userPrincipal}">
                <p>Welcome, <%= ((javax.servlet.http.HttpServletRequest) pageContext.getRequest()).getUserPrincipal().getName() %>.</p>
                <p>You can <a href="new-page">create a new page</a>.</p>
            </c:if>
        </div>
    </body>
</html>
