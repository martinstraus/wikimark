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
            <h1>File upload</h1>
            <form action="${pageContext.request.contextPath}/attachments" method="post" enctype="multipart/form-data">
                <label for="file">File</label>
                <input type="file" id="file" name="file">
                <button class="button-primary">Upload</button>
            </form>
        </div>
    </body>
</html>
