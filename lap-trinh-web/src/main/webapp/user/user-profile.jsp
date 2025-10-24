<%--
  Created by IntelliJ IDEA.
  User: NhutTan
  Date: 10/2/2025
  Time: 8:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thông tin người dùng</title>
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
    <script>
      $(document).ready(function () {
        var readURL = function (input) {
          if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
              $('.avatar').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
          }
        }

        $(".file-upload").on('change', function () {
          readURL(this);
        });
      });
    </script>
</head>

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

        <!-- User Profile -->
        <div class="col-md-9">
            <div class="tab-content">
                <div class="tab-pane active" id="home">
                    <form class="form" action="${pageContext.request.contextPath}/profile" method="post"
                          enctype="multipart/form-data">
                        <div class="row">
                            <div class="col-md-8">
                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="fullName"><h4>Họ tên:</h4></label>
                                        <input type="text" class="form-control" name="fullName" id="fullName"
                                               value="${userProfile.fullName}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="phone"><h4>Số điện thoại:</h4></label>
                                        <input type="tel" class="form-control" name="phone" id="phone"
                                               value="${userProfile.phone}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="email"><h4>Email:</h4></label>
                                        <input type="email"
                                               class="form-control"
                                               name="email" id="email"
                                               value="${userProfile.email}"
                                               <c:if test="${not empty userProfile.email}">readonly</c:if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="address1"><h4>Địa chỉ 1:</h4></label>
                                        <input type="text" class="form-control" name="address1" id="address1"
                                               value="${userProfile.address1}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="address2"><h4>Địa chỉ 2:</h4></label>
                                        <input type="text" class="form-control" name="address2" id="address2"
                                               value="${userProfile.address2}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6">
                                        <label for="address3"><h4>Địa chỉ 3:</h4></label>
                                        <input type="text" class="form-control" name="address3" id="address3"
                                               value="${userProfile.address3}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <button class="btn btn-lg btn-success" type="submit"><i
                                                class="glyphicon glyphicon-ok-sign"></i> Lưu thay đổi
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <!-- Upload Image -->
                            <div class="col-md-4 text-center">
                                <div class="text-center">
                                    <c:set var="avatarUrl"
                                           value="${empty userProfile.avatar ? 'http://ssl.gstatic.com/accounts/ui/avatar_2x.png' : userProfile.avatar}"/>

                                    <img src="${avatarUrl}"
                                         class="avatar img-circle img-thumbnail"
                                         onerror="this.src='http://ssl.gstatic.com/accounts/ui/avatar_2x.png'"
                                         alt="avatar">

                                    <input type="file" name="avatar" accept="image/*"
                                           class="text-center center-block file-upload"/>
                                </div>
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

<!-- Image preview script -->
<script>
  $(function () {
    $(".file-upload").on("change", function () {
      const input = this;
      if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
          $(".avatar").attr("src", e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
      }
    });
  });
</script>
</body>
</html>
