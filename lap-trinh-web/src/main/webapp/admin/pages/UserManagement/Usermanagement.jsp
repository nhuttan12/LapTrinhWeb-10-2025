<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>User Management - Admin</title>

    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
    <!-- inject:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <!-- endinject -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png" />
</head>

<body>
<div class="container-scroller">
    <!-- Navbar -->
    <jsp:include page="../../partials/navbar.jsp" />

    <div class="container-fluid page-body-wrapper">
        <!-- Settings Panel -->
        <jsp:include page="../../partials/settings-panel.jsp" />

        <!-- Sidebar -->
        <jsp:include page="../../partials/sidebar.jsp" />

        <!-- Main content -->
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-lg-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">

                                <h4 class="card-title">Quản lý người dùng</h4>
                                <p class="card-description">Danh sách tất cả người dùng trong hệ thống</p>

                                <!-- Hiển thị thông báo -->
                                <c:if test="${not empty sessionScope.message}">
                                    <div class="alert alert-info alert-dismissible fade show" role="alert">
                                            ${sessionScope.message}
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <c:remove var="message" scope="session" />
                                </c:if>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Ảnh đại diện</th>
                                            <th>Tên người dùng</th>
                                            <th>Họ và tên</th>
                                            <th>Email</th>
                                            <th>Trạng thái</th>
                                            <th>Vai trò</th>
                                            <th>Số điện thoại</th>
                                            <th>Địa chỉ</th>
                                            <th>Thao tác</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="u" items="${users}">
                                            <tr>
                                                <td>${u.id}</td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/images/${u.imageId != null ? u.imageId : 1}.png"
                                                         class="img-sm rounded-circle" alt="image">
                                                </td>
                                                <td>${u.username}</td>
                                                <td>${u.fullName}</td>
                                                <td>${u.email}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${u.status == 'ACTIVE'}">
                                                            <label class="badge badge-success">Hoạt động</label>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <label class="badge badge-danger">Đã khóa</label>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${u.roleName}</td>
                                                <td>${u.phone}</td>
                                                <td>${u.address}</td>
                                                <td>
                                                    <!-- Nút Xóa mềm -->
                                                    <form action="${pageContext.request.contextPath}/admin/users"
                                                          method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="softDelete"/>
                                                        <input type="hidden" name="userId" value="${u.id}"/>
                                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                                onclick="return confirm('Xác nhận xóa người dùng này?')">
                                                            Xóa
                                                        </button>
                                                    </form>

                                                    <!-- Đổi role -->
                                                    <form action="${pageContext.request.contextPath}/admin/users"
                                                          method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="changeRole"/>
                                                        <input type="hidden" name="userId" value="${u.id}"/>
                                                        <select name="newRoleId" onchange="this.form.submit()" class="form-control-sm">
                                                            <option value="1" ${u.roleName == 'User' ? 'selected' : ''}>User</option>
                                                            <option value="2" ${u.roleName == 'Admin' ? 'selected' : ''}>Admin</option>
                                                        </select>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <jsp:include page="../../partials/footer.jsp" />
        </div>
    </div>
</div>

<!-- JS -->
<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>

</body>
</html>
