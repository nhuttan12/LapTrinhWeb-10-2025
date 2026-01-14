<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
    <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
        <a class="navbar-brand brand-logo mr-5" href="${pageContext.request.contextPath}/admin/home"><img
                src="${pageContext.request.contextPath}/admin/images/logo.svg" class="mr-2" alt="logo"/></a>
        <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/admin/home"><img
                src="${pageContext.request.contextPath}/admin/images/logo-mini.svg" alt="logo"/></a>
    </div>
    <div class="navbar-menu-wrapper d-flex align-items-center justify-content-end">
        <button class="navbar-toggler navbar-toggler align-self-center" type="button" data-toggle="minimize">
            <span class="icon-menu"></span>
        </button>
        <ul class="navbar-nav mr-lg-2">
            <li class="nav-item nav-search d-none d-lg-block">
                <div class="input-group">
                    <div class="input-group-prepend hover-cursor" id="navbar-search-icon">
                <span class="input-group-text" id="search">
                  <i class="icon-search"></i>
                </span>
                    </div>
                    <input type="text" class="form-control" id="navbar-search-input" placeholder="Search now"
                           aria-label="search" aria-describedby="search">
                </div>
            </li>
        </ul>
        <ul class="navbar-nav navbar-nav-right">
            <li class="nav-item nav-profile dropdown">
                <a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/profile">
                    <c:set var="avatarUrl"
                           value="${empty userProfile.avatar ? 'http://ssl.gstatic.com/accounts/ui/avatar_2x.png' : userProfile.avatar}"/>

                    <img src="${avatarUrl}?v=${pageContext.session.id}"
                         class="img-xs rounded-circle"
                         onerror="this.src='http://ssl.gstatic.com/accounts/ui/avatar_2x.png'"
                         alt="profile"/>

                </a>
                <div class="dropdown-menu dropdown-menu-right navbar-dropdown" aria-labelledby="profileDropdown">
                    <a class="dropdown-item">
                        <i class="ti-settings text-primary"></i>
                        Settings
                    </a>
                    <c:if test="${not empty sessionScope.ADMIN_BACK_URL}">
                        <a class="dropdown-item" href="${sessionScope.ADMIN_BACK_URL}">
                            <i class="ti-arrow-left text-primary"></i>
                            Quay lại
                        </a>
                    </c:if>
                </div>
            </li>
        </ul>
    </div>
</nav>