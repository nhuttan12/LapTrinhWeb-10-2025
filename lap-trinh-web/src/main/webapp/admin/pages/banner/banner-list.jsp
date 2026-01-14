<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin - quản lý Banner</title>

    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">

    <!-- layout css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>
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

                                <h4 class="card-title">Danh sách Banner</h4>

                                <div class="pb-2">
                                    <a href="${pageContext.request.contextPath}/admin/banners/adding"
                                       class="btn btn-primary">
                                        Thêm Banner
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th>Mã số</th>
                                            <th>Hình ảnh</th>
                                            <th>Trạng thái</th>
                                            <th>Ngày tạo</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="banner" items="${banners}">
                                            <tr id="bannerRow${banner.id}">
                                                <td>${banner.id}</td>
                                                <td>
                                                    <img src="${banner.url}" style="width:200px;height:auto;border-radius: 0%"/>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${banner.status.imageStatus == 'active'}">
                                                            Đang hiển thị
                                                        </c:when>
                                                        <c:otherwise>
                                                            Tạm ẩn
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                        ${banner.createdAt}
                                                </td>
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/admin/banners"
                                                          method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${banner.id}">
                                                        <button class="btn btn-danger btn-sm" type="submit">Xóa</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination -->
                                <nav class="mt-3">
                                    <ul class="pagination justify-content-center">

                                        <!-- Prev -->
                                        <li class="page-item ${!meta.hasPrevious ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="?page=${meta.currentPage - 1}&pageSize=${meta.pageSize}">
                                                Prev
                                            </a>
                                        </li>

                                        <!-- Page numbers -->
                                        <c:forEach begin="1" end="${meta.totalPages}" var="i">
                                            <li class="page-item ${i == meta.currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="?page=${i}&pageSize=${meta.pageSize}">
                                                        ${i}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <!-- Next -->
                                        <li class="page-item ${!meta.hasNext ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="?page=${meta.currentPage + 1}&pageSize=${meta.pageSize}">
                                                Next
                                            </a>
                                        </li>

                                    </ul>
                                </nav>
                            </div>
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
<%--<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>--%>
<!-- inject:js -->

<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<%--<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>--%>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>

</body>
</html>
