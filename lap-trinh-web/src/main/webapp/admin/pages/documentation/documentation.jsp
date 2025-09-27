<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Skydash Admin Dashboard</title>
  <!-- plugins:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
  <!-- endinject -->
  <!-- plugin css for this page -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/codemirror/codemirror.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/codemirror/ambiance.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/pwstabs/jquery.pwstabs.min.css">
  <!-- End plugin css for this page -->
  <!-- inject:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
  <!-- endinject -->
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png" />
</head>

<body>
    <div class=" container-scroller">
        <div class="container-fluid page-body-wrapper full-page-wrapper">
          <div class="main-panel w-100  documentation">
            <div class="content-wrapper">
              <div class="container-fluid">
                <div class="row">
                  <div class="col-12 pt-5">
                    <a class="btn btn-primary" href="../../index.jsp"><i class="ti-home mr-2"></i>Back to home</a>
                  </div>
                </div>
                <div class="row pt-5 mt-5">
                  <div class="col-12 pt-5 text-center">
                    <i class="text-primary mdi mdi-file-document-box-multiple-outline display-1"></i>
                    <h3 class="text-primary font-weight-light mt-5">
                      The detailed documentation of Skydash Admin Template is provided at 
                      <a href="http://bootstrapdash.com/demo/skydash/docs/documentation.html" target="_blank" class="text-danger d-block text-truncate">
                        http://bootstrapdash.com/demo/skydash/docs/documentation.html
                      </a>
                    </h3>
                    <h4 class="mt-4 font-weight-light text-primary">
                      In case you want to refer the documentation file, it is available at 
                      <span class="text-danger">/docs/documentation.html</span> 
                      in the downloaded folder
                    </h4>
                  </div>
                </div>
              </div>
            </div>
            <!-- partial:../../partials/footer.jsp -->

            <jsp:include page="../../partials/footer.jsp" />

            <!-- partial -->
          </div>
        </div>
      </div>
<!-- plugins:js -->
<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>
<!-- endinject -->
<!-- Custom js for this page-->
<script src="${pageContext.request.contextPath}/admin/js/codeEditor.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/tabs.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/tooltips.js"></script>
<script src="${pageContext.request.contextPath}/admin/docs/documentation.js"></script>
<!-- End custom js for this page-->
</body>

</html>