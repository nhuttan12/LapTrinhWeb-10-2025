<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<nav class="sidebar sidebar-offcanvas" id="sidebar">
    <ul class="nav">
        <li class="nav-item">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Quản lý thông tin người dùng</span>
            </a>
        </li>
        <li class="nav-item">
<%--            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">--%>
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Quản lý thông tin sản phẩm</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Quản lý thông tin hoá đơn</span>
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
  // Xóa active mặc định
  document.querySelectorAll('.sidebar .nav-link').forEach(link => {
    link.classList.remove('active');
  });

  // Lấy pathname hiện tại
  let currentPath = window.location.pathname;

  // Set active cho link khớp
  document.querySelectorAll('.sidebar .nav-link').forEach(link => {
    let href = link.getAttribute('href');
    if (href && currentPath.endsWith(href.replace(window.location.origin, ''))) {
      link.classList.add('active');

      // mở collapse cha nếu có
      let collapseParent = link.closest('.collapse');
      if (collapseParent) {
        collapseParent.classList.add('show');

        // thêm active cho link collapse toggle cha
        let toggleLink = document.querySelector(`[data-toggle="collapse"][href="#${collapseParent.id}"]`);
        if (toggleLink) toggleLink.classList.add('active');
      }
    }
  });

</script>