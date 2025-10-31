<%--
  Created by IntelliJ IDEA.
  User: NhutTan
  Date: 10/2/2025
  Time: 8:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Đổi mật khẩu</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">
</head>

<c:if test="${not empty error}">
    <script>
      alert("${fn:escapeXml(error)}");
    </script>
</c:if>

<c:if test="${not empty message}">
    <script>
      alert("${fn:escapeXml(message)}");
    </script>
</c:if>
<body>
<div class="container bootstrap snippet">
    <%--    Header--%>
    <jsp:include page="header.jsp"/>

    <%--    Content    --%>
    <div class="row justify-content-center">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="user-profile-side-bar.jsp"/>
        </div>

        <!-- Change Password -->
        <div class="col-sm-9">
            <div class="tab-content">
                <div class="tab-pane active" id="home">
                    <form class="form" action="${pageContext.request.contextPath}/change-password" method="post">
                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="username"><h4>Tài khoản:</h4></label>
                                <input type="text" class="form-control" name="username" id="username"
                                       value="${username}"
                                       <c:if test="${not empty username}">readonly</c:if>>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="password"><h4>Mật khẩu:</h4></label>
                                <input type="password" class="form-control" name="password" id="password">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="newPassword"><h4>Mật khẩu mới: </h4></label>
                                <input type="password" class="form-control" name="newPassword" id="newPassword">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="retypePassword"><h4>Nhập lại mật khẩu:</h4></label>
                                <input type="password" class="form-control" name="retypePassword" id="retypePassword">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-12">
                                <button class="btn btn-lg btn-success" type="submit"><i
                                        class="glyphicon glyphicon-ok-sign"></i> Lưu thay đổi
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%--    Footer--%>
    <jsp:include page="footer.jsp"/>
</div>

<%--Import library--%>
<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/user/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery.magnific-popup.min.js"></script>
</body>
</html>