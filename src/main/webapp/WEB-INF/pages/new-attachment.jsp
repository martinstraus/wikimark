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
        <div class="container">
            <h1>File upload</h1>
            <form action="${pageContext.request.contextPath}/attachments" method="post" enctype="multipart/form-data">
                <label for="file">File</label>
                <input type="file" id="file" name="file">
                <button class="button-primary">Upload</button>
            </form>
        </div>
    </body>
</html>
