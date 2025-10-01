<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Skydash Admin</title>
    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
    <!-- end inject -->
    <!-- Plugin css for this page -->
    <!-- End plugin css for this page -->
    <!-- inject:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <!-- end inject -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>

    <%--Showing error--%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <script>
      alert("<%= errorMessage %>");
    </script>
    <%
        }
    %>

    <script>
      document.querySelector("form").addEventListener("submit", function (event) {
        const username = document.querySelector("input[name='username']").value.trim();
        const password = document.querySelector("input[name='password']").value.trim();

        if (!username || !password) {
          alert("Tài khoản và mật khẩu không được để trống!");
          event.preventDefault(); // Stop form submission
          return;
        }

        if (username.length < 4 || password.length < 4) {
          alert("Tài khoản và mật khẩu phải có ít nhất 4 ký tự!");
          event.preventDefault();
          return;
        }
      });
    </script>
</head>

<body>
<div class="container-scroller">
    <div class="container-fluid page-body-wrapper full-page-wrapper">
        <div class="content-wrapper d-flex align-items-center auth px-0">
            <div class="row w-100 mx-0">
                <div class="col-lg-4 mx-auto">
                    <div class="auth-form-light text-left py-5 px-4 px-sm-5">
                        <div class="brand-logo">
                            <img src="${pageContext.request.contextPath}/admin/images/logo.svg" alt="logo">
                        </div>
                        <h4>Xin chào!</h4>
                        <h6 class="font-weight-light">Đăng nhập để tiếp tục duyệt web.</h6>

                        <form class="pt-3" action="${pageContext.request.contextPath}/login" method="post">
                            <div class="form-group">
                                <input name="username" type="text" class="form-control form-control-lg"
                                       id="exampleInputEmail1"
                                       placeholder="Tài khoản">
                            </div>
                            <div class="form-group">
                                <input name="password" type="password" class="form-control form-control-lg"
                                       id="exampleInputPassword1"
                                       placeholder="Mật khẩu">
                            </div>
                            <div class="mt-3">
                                <button type="submit"
                                        class="btn btn-block btn-primary btn-lg font-weight-medium auth-form-btn">
                                    SIGN IN
                                </button>
                            </div>
                            <div class="my-2 d-flex justify-content-between align-items-center">
                                <div class="form-check">
                                    <label class="form-check-label text-muted">
                                        <input type="checkbox" class="form-check-input">
                                        Giữ đăng nhập
                                    </label>
                                </div>
                            </div>
                            <%--                <div class="mb-2">--%>
                            <%--                  <button type="button" class="btn btn-block btn-facebook auth-form-btn">--%>
                            <%--                    <i class="ti-facebook mr-2"></i>Connect using facebook--%>
                            <%--                  </button>--%>
                            <%--                </div>--%>
                            <div class="text-center mt-4 font-weight-light">
                                Nếu bạn chưa có tài khoản? <a href="${pageContext.request.contextPath}/register"
                                                          class="text-primary">Tạo tài khoản</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- content-wrapper ends -->
    </div>
    <!-- page-body-wrapper ends -->
</div>
<!-- container-scroller -->
<!-- plugins:js -->
<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<!-- end inject -->
<!-- Plugin js for this page -->
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>
<!-- end inject -->
</body>

</html>
