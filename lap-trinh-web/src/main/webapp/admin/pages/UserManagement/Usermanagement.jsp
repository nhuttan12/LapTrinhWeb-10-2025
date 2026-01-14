<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Management - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png" />
</head>
<body>
<div class="container-scroller">
    <jsp:include page="../../partials/navbar.jsp"/>
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../../partials/settings-panel.jsp"/>
        <jsp:include page="../../partials/sidebar.jsp"/>
        <div class="main-panel">
            <div class="content-wrapper">
                <h4>Quản lý người dùng</h4>
                <p>Danh sách tất cả người dùng trong hệ thống</p>

                <!-- Hiển thị thông báo -->
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-info alert-dismissible fade show">
                            ${sessionScope.message}
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                    <c:remove var="message" scope="session"/>
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
                            <th>Chức vụ</th>
                            <th>Số điện thoại</th>
                            <th>Địa chỉ</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty users}">
                            <tr>
                                <td colspan="10" class="text-center">Chưa có người dùng nào</td>
                            </tr>
                        </c:if>

                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.id}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty u.userImage and not empty u.userImage.image}">
                                            <img src="${u.userImage.image.url}" class="img-sm rounded-circle" alt="image"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/default.png" class="img-sm rounded-circle" alt="image"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${u.username}</td>
                                <td>${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.status eq 'ACTIVE'}">
                                            <span class="badge badge-success">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">Đã khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post">
                                        <input type="hidden" name="action" value="changeRole"/>
                                        <input type="hidden" name="userId" value="${u.id}"/>
                                        <select name="newRoleName" class="form-control-sm" onchange="this.form.submit()">
                                            <option value="ADMIN" ${u.roleId == 1 ? 'selected' : ''}>Quản lý</option>
                                            <option value="CUSTOMER" ${u.roleId == 2 ? 'selected' : ''}>Khách hàng</option>
                                        </select>
                                    </form>
                                </td>
                                <td>${u.userDetail != null ? u.userDetail.phone : ''}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.userDetail != null}">
                                            ${u.userDetail.address1} ${u.userDetail.address2} ${u.userDetail.address3}
                                        </c:when>
                                        <c:otherwise></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="softDelete"/>
                                        <input type="hidden" name="userId" value="${u.id}"/>
                                        <button type="submit"
                                                class="btn btn-sm ${u.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'}"
                                                onclick="return confirm('${u.status == 'ACTIVE' ? 'Xác nhận xóa?' : 'Xác nhận khôi phục?'}')">
                                                ${u.status == 'ACTIVE' ? 'Xóa' : 'Khôi phục'}
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <jsp:include page="../../partials/footer.jsp"/>
        </div>
    </div>
</div>

<%--<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>--%>
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<%--<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>--%>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>


</body>
</html>
