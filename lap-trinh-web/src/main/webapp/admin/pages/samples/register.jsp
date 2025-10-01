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
    <!-- endinject -->
    <!-- Plugin css for this page -->
    <!-- End plugin css for this page -->
    <!-- inject:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <!-- endinject -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        const form = document.querySelector("form");

        form.addEventListener("submit", function (e) {
          const username = form.querySelector("input[name='username']").value.trim();
          const email = form.querySelector("input[name='email']").value.trim();
          const password = form.querySelector("input[name='password']").value.trim();
          const retypePassword = form.querySelector("input[name='retypePassword']").value.trim();

          // check empty
          if (!username || !email || !password || !retypePassword) {
            alert("Vui lòng điền đầy đủ thông tin!");
            e.preventDefault();
            return;
          }

          // username + password length
          if (username.length < 4 || password.length < 4) {
            alert("Tên đăng nhập và mật khẩu phải có ít nhất 4 ký tự!");
            e.preventDefault();
            return;
          }

          // email format
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
          if (!emailRegex.test(email)) {
            alert("Email không hợp lệ!");
            e.preventDefault();
            return;
          }

          // password match
          if (password !== retypePassword) {
            alert("Mật khẩu nhập lại không khớp!");
            e.preventDefault();
            return;
          }
        });
      });

      document.addEventListener("DOMContentLoaded", function () {
        const form = document.querySelector("form");
        const registerBtn = document.getElementById("registerBtn");

        form.addEventListener("submit", function () {
          // disable button immediately
          registerBtn.disabled = true;
          registerBtn.innerText = "Đang xử lý...";
        });
      });
    </script>

    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <script>
      alert("<%= errorMessage %>");
      // re-enable button after error
      const registerBtn = document.getElementById("registerBtn");
      if (registerBtn) {
        registerBtn.disabled = false;
        registerBtn.innerText = "ĐĂNG KÝ";
      }
    </script>
    <%
        }
    %>
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
                        <h4>Bạn là người mới?</h4>
                        <h6 class="font-weight-light">Đăng ký cực nhanh, nó chỉ tốn một chút thời gian!</h6>

                        <form class="pt-3" action="${pageContext.request.contextPath}/register" method="post">
                            <div class="form-group">
                                <input name="username" type="text" class="form-control form-control-lg"
                                       id="exampleInputUsername1"
                                       placeholder="Tài khoản">
                            </div>
                            <div class="form-group">
                                <input name="email" type="email" class="form-control form-control-lg"
                                       id="exampleInputEmail1"
                                       placeholder="Email">
                            </div>
                            <div class="form-group">
                                <input name="password" type="password" class="form-control form-control-lg"
                                       id="exampleInputPassword1"
                                       placeholder="Mật khẩu">
                            </div>
                            <div class="form-group">
                                <input name="retypePassword" type="password" class="form-control form-control-lg"
                                       id="exampleInputPassword1"
                                       placeholder="Nhập lại mật khẩu">
                            </div>
                            <div class="mb-4">
                                <div class="form-check">
                                    <label class="form-check-label text-muted">
                                        <input type="checkbox" class="form-check-input">
                                        Tôi đồng ý với điều khoản
                                    </label>
                                </div>
                            </div>
                            <div class="mt-3">
                                <button type="submit" id="registerBtn"
                                        class="btn btn-block btn-primary btn-lg font-weight-medium auth-form-btn"
                                        href="/register">ĐĂNG KÝ
                                </button>
                            </div>
                            <div class="text-center mt-4 font-weight-light">
                                Nếu bạn đã có tài khoản? <a href="/login" class="text-primary">Trở lại đăng nhập</a>
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
