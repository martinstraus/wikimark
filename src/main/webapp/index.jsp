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
            <c:if test="${not empty mensaje}">
                <p class="error">${mensaje}</p>
            </c:if>
            <c:if test="${not empty pageContext.request.userPrincipal}">
                <p>Welcome, <%= ((javax.servlet.http.HttpServletRequest) pageContext.getRequest()).getUserPrincipal().getName() %>.</p>
                <p>You can <a href="new-page">create a new page</a>.</p>
            </c:if>
            <c:if test="${empty pageContext.request.userPrincipal}">
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <label for="j_username">Username</label>
                    <input type="text" id="j_username" name="j_username" class="u-full-width">
                    <label for="j_password">Password</label>
                    <input type="password" id="j_password" name="j_password" class="u-full-width" >
                    <button class="button-primary">Log in</button>
                </form>
            </c:if>
        </div>
    </body>
</html>
