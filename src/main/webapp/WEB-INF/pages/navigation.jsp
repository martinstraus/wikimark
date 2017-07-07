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
<nav>
    <object data="${context.urlRelativeToHost('/img/logomark.svg')}" type="image/svg+xml" class="logomark"></object>
    <ul>
        <li><a href="${context.baseURL()}"><i class="fa fa-home"></i><span class="shown-only tablet desktop">&nbsp;Home</span></a></li>
                    <c:if test="${empty pageContext.request.userPrincipal}">
            <li><a href="${context.urlRelativeToHost('/login.jsp')}" title="Sign in"><i class="fa fa-sign-in"></i><span class="shown-only tablet desktop">&nbsp;Sign in</span></a></li>
                    </c:if>
                    <c:if test="${not empty pageContext.request.userPrincipal}">
            <li><a href="${context.urlRelativeToHost('/new-page')}" title="Create new page"><i class="fa fa-plus"></i><span class="shown-only tablet desktop">&nbsp;Create new page</span></a></a></li>
            <li><a href="#" title="Sign out"><i class="fa fa-sign-out"></i><span class="shown-only tablet desktop">&nbsp;Sign out</span></a></li>
                    </c:if>
    </ul>
</nav>