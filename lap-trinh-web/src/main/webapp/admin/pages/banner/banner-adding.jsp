<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin - Thêm banner mới</title>

    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">

    <!-- layout css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>
    <script>
      document.addEventListener("DOMContentLoaded", function () {

        const fileInput = document.querySelector(".file-upload");
        const previewImg = document.querySelector(".avatar"); // your <img>

        if (!fileInput || !previewImg) return;

        fileInput.addEventListener("change", function () {
          const file = this.files[0];

          if (!file) return;

          const reader = new FileReader();

          reader.onload = function (e) {
            previewImg.src = e.target.result;
          };

          reader.readAsDataURL(file);
        });

      });
    </script>
</head>

<body>
<div class="container-scroller">

    <!-- Navbar -->
    <jsp:include page="../../partials/navbar.jsp"/>

    <div class="container-fluid page-body-wrapper">

        <!-- Settings panel -->
        <jsp:include page="../../partials/settings-panel.jsp"/>

        <!-- Sidebar -->
        <jsp:include page="../../partials/sidebar.jsp"/>

        <!-- Main content -->
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">

                                <h4 class="card-title">Thêm banner mới</h4>
                                <form
                                        method="post"
                                        action="${pageContext.request.contextPath}/admin/banners/adding"
                                        enctype="multipart/form-data">
                                    <div class="col-md-6 mb-3">
                                        <div class="text-center">
                                            <img src=""
                                                 class="avatar img-circle img-thumbnail"
                                                 style="width: 150px; height: 150px;"
                                                 onerror="this.src='http://ssl.gstatic.com/accounts/ui/avatar_2x.png'"
                                                 alt="${detail.name}"/>

                                            <input type="file" name="thumbnailFile" accept="image/*"
                                                   class="text-center center-block file-upload"/>
                                        </div>
                                    </div>
                                    <button class="btn btn-primary" type="submit">Thêm Banner</button>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>

                <!-- Footer -->
                <jsp:include page="../../partials/footer.jsp"/>
            </div>
            <!-- main-panel ends -->
        </div>
    </div>

    <!-- plugins:js -->
    <script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
    <!-- inject:js -->
    <script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>


    <!-- Image preview script -->
    <script>
      document.addEventListener("DOMContentLoaded", function () {

        const fileUpload = document.querySelector(".file-upload");
        const avatar = document.querySelector(".avatar");

        if (!fileUpload || !avatar) return;

        fileUpload.addEventListener("change", function () {
          const input = this;

          if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
              avatar.src = e.target.result;
            };

            reader.readAsDataURL(input.files[0]);
          }
        });
      });
    </script>
</body>
</html>
