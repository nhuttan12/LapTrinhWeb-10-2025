<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<nav class="sidebar sidebar-offcanvas" id="sidebar">
    <ul class="nav">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                <i class="icon-head menu-icon"></i>
                <span class="menu-title">Quản lý người dùng</span>
            </a>
        </li>
        <li class="nav-item">
            <%--            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">--%>
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">
                <i class="ti-package menu-icon"></i>
                <span class="menu-title">Quản lý sản phẩm</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders">
                <i class="ti-receipt menu-icon"></i>
                <span class="menu-title">Quản lý hoá đơn</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/banners">
                <i class="icon-image menu-icon"></i>
                <span class="menu-title">Quản lý banner</span>
            </a>
        </li>

    </ul>

</nav>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const currentPath = window.location.pathname;

        document.querySelectorAll('.sidebar .nav-link').forEach(link => {
            const linkPath = link.getAttribute('href');

            if (linkPath && currentPath.startsWith(linkPath)) {
                link.classList.add('active');
                link.closest('.nav-item').classList.add('active');
            }
        });
    });
</script>