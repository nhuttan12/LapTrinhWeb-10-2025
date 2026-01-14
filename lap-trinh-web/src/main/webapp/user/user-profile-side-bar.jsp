<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="messages"/>

<div class="list-group">
    <%--    User profile--%>
    <a href="${pageContext.request.contextPath}/profile" class="list-group-item list-group-item-action">
        <i class="bi bi-person"></i>
        <fmt:message key="profile.sidebar.userInfo"/>
    </a>

    <%--    Change password--%>
    <a href="${pageContext.request.contextPath}/change-password" class="list-group-item list-group-item-action">
        <i class="bi bi-lock"></i>
        <fmt:message key="profile.sidebar.changePassword"/>
    </a>

    <%--    Personal order management--%>
    <a href="${pageContext.request.contextPath}/orders" class="list-group-item list-group-item-action">
        <i class="bi bi-cart"></i>
        <fmt:message key="profile.sidebar.orders"/>
    </a>

    <%--    Admin--%>
    <c:if test="${sessionScope.roleName eq 'admin'}">
        <a href="${pageContext.request.contextPath}/admin/products"
           class="list-group-item list-group-item-action">
            <i class="bi bi-shield-lock"></i>
            <fmt:message key="profile.sidebar.admin"/>
        </a>
    </c:if>

    <%--    Logout--%>
    <a href="${pageContext.request.contextPath}/logout" class="list-group-item list-group-item-action">
        <i class="bi bi-box-arrow-right"></i> Đăng xuất
    </a>
</div>