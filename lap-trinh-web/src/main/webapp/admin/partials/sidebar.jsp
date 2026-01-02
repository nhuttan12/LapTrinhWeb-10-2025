<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<nav class="sidebar sidebar-offcanvas" id="sidebar">
    <ul class="nav">
        <li class="nav-item">
            <a class="nav-link" href="index.jsp">
                <i class="icon-grid menu-icon"></i>
                <span class="menu-title">Dashboard</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#ui-basic" aria-expanded="false" aria-controls="ui-basic">
                <i class="icon-layout menu-icon"></i>
                <span class="menu-title">UI Elements</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="ui-basic">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/ui-features/buttons.jsp">Buttons</a>
                    </li>
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/ui-features/dropdowns.jsp">Dropdowns</a>
                    </li>
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/ui-features/typography.jsp">Typography</a>
                    </li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#form-elements" aria-expanded="false"
               aria-controls="form-elements">
                <i class="icon-columns menu-icon"></i>
                <span class="menu-title">Form elements</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="form-elements">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/forms/basic_elements.jsp">Basic
                        Elements</a>
                    </li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#charts" aria-expanded="false" aria-controls="charts">
                <i class="icon-bar-graph menu-icon"></i>
                <span class="menu-title">Charts</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="charts">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/charts/chartjs.jsp">ChartJs</a>
                    </li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#tables" aria-expanded="false" aria-controls="tables">
                <i class="icon-grid-2 menu-icon"></i>
                <span class="menu-title">Tables</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="tables">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/tables/basic-table.jsp">Basic
                        table</a></li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#icons" aria-expanded="false" aria-controls="icons">
                <i class="icon-contract menu-icon"></i>
                <span class="menu-title">Icons</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="icons">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/icons/mdi.jsp">Mdi
                        icons</a></li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#auth" aria-expanded="false" aria-controls="auth">
                <i class="icon-head menu-icon"></i>
                <span class="menu-title">User Pages</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="auth">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/samples/login.jsp">
                        Login </a></li>
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/samples/register.jsp">
                        Register </a></li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="collapse" href="#error" aria-expanded="false" aria-controls="error">
                <i class="icon-ban menu-icon"></i>
                <span class="menu-title">Error pages</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="error">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/samples/error-404.jsp">
                        404 </a></li>
                    <li class="nav-item"><a class="nav-link"
                                            href="${pageContext.request.contextPath}/admin/pages/samples/error-500.jsp">
                        500 </a></li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/documentation/documentation.jsp">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Documentation</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Usermanagement</span>
            </a>
        </li>
        <li class="nav-item">
<%--            <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">--%>
    <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">ProductManagement</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders">
                <i class="icon-paper menu-icon"></i>
                <span class="menu-title">Order Management</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/banners">
                <i class="icon-image menu-icon"></i>
                <span class="menu-title">Banner Management</span>
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