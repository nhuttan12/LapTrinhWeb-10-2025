<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="list-group">
    <a href="${pageContext.request.contextPath}/profile" class="list-group-item list-group-item-action">
        <i class="fas fa-user"></i> Thông tin người dùng
    </a>
    <a href="${pageContext.request.contextPath}/change-password" class="list-group-item list-group-item-action">
        <i class="fas fa-lock"></i> Đổi mật khẩu
    </a>
    <a href="${pageContext.request.contextPath}/orders" class="list-group-item list-group-item-action">
        <i class="fas fa-shopping-cart"></i> Hoá đơn người dùng
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="list-group-item list-group-item-action">
        <i class="fas fa-sign-out-alt"></i> Đăng xuất
    </a>
</div>
