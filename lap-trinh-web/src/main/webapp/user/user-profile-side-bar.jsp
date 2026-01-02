<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="messages" />

<div class="list-group">
    <a href="${pageContext.request.contextPath}/profile" class="list-group-item list-group-item-action">
        <i class="fas fa-user"></i> <fmt:message key="profile.sidebar.userInfo"/>
    </a>

    <a href="${pageContext.request.contextPath}/change-password" class="list-group-item list-group-item-action">
        <i class="fas fa-lock"></i> <fmt:message key="profile.sidebar.changePassword"/>
    </a>

    <a href="${pageContext.request.contextPath}/orders" class="list-group-item list-group-item-action">
        <i class="fas fa-shopping-cart"></i> <fmt:message key="profile.sidebar.orders"/>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="list-group-item list-group-item-action">
        <i class="fas fa-sign-out-alt"></i> Đăng xuất
    </a>
</div>
