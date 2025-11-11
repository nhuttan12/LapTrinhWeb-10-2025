<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Danh sách sản phẩm | Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png" />
</head>

<body>
<div class="container-scroller">
    <jsp:include page="../../partials/navbar.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../../partials/settings-panel.jsp" />
        <jsp:include page="../../partials/sidebar.jsp" />

        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-lg-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Danh sách sản phẩm</h4>
                                <p class="card-description">
                                    Dữ liệu được tải từ cơ sở dữ liệu
                                </p>
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Giá</th>
                                            <th>Giảm giá</th>
                                            <th>Danh mục</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="p" items="${products}">
                                            <tr>
                                                <td>${p.id}</td>
                                                <td>${p.name}</td>
                                                <td>${p.price}</td>
                                                <td>${p.discount}</td>
                                                <td>${p.category}</td>
                                                <td>${p.status}</td>
                                                <td>
                                                    <a href="products?action=detail&id=${p.id}" class="btn btn-sm btn-info">Chi tiết</a>
                                                    <a href="products?action=delete&id=${p.id}" class="btn btn-sm btn-danger"
                                                       onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">Xóa</a>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty products}">
                                            <tr><td colspan="7" class="text-center">Không có sản phẩm nào.</td></tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination -->
                                <div class="mt-3 text-center">
                                    <c:if test="${currentPage > 1}">
                                        <a href="products?page=${currentPage - 1}" class="btn btn-outline-primary btn-sm">Trước</a>
                                    </c:if>
                                    <span>Trang ${currentPage}</span>
                                    <a href="products?page=${currentPage + 1}" class="btn btn-outline-primary btn-sm">Sau</a>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="../../partials/footer.jsp" />
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>
</body>
</html>
