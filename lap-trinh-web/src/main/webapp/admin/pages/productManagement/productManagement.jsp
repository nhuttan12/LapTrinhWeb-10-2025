<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý sản phẩm</title>

    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">

    <!-- layout css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>
</head>

<body>
<div class="container-scroller">

    <!-- Navbar -->
    <jsp:include page="../../partials/navbar.jsp"/>

    <div class="container-fluid page-body-wrapper">

        <!-- Settings panel -->
        <jsp:include page="../../partials/settings-panel.jsp"/>

        <!-- Sidebar -->
        <jsp:include page="../../partials/sidebar.jsp"/>

        <!-- Main content -->
        <div class="main-panel">
            <div class="content-wrapper">

                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">

                                <div class="d-flex justify-content-between mb-3">
                                    <h3>
                                        <fmt:message key="admin.title"/>
                                    </h3>
                                    <a href="/admin/products/add" class="btn btn-success">
                                        <fmt:message key="admin.add"/>
                                    </a>
                                </div>

                                <table class="table table-bordered text-center align-middle">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>
                                            <fmt:message key="admin.table.id"/>
                                        </th>
                                        <th>
                                            <fmt:message key="admin.table.thumbnail"/>
                                        </th>
                                        <th>
                                            <fmt:message key="admin.table.name"/>
                                        </th>
                                        <th>
                                            <fmt:message key="admin.table.price"/>
                                        </th>
                                        <th>
                                            <fmt:message key="admin.table.discount"/>
                                        </th>
                                        <th>
                                            <fmt:message key="admin.table.brand"/>
                                        </th>
                                        <th style="width:150px;">
                                            <fmt:message key="admin.table.action"/>
                                        </th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="p" items="${products}">
                                        <tr>
                                            <td>${p.id}</td>

                                            <td>
                                                <img src="${p.thumbnail}" style="width:60px;height:60px;object-fit:cover;">
                                            </td>

                                            <td>${p.name}</td>
                                            <td>
                                                <fmt:formatNumber value="${p.price}" type="number" minFractionDigits="0" maxFractionDigits="2"/>
                                            </td>
                                            <td>${p.discount}</td>
                                            <td>${p.brand}</td>

                                            <td>
                                                <a href="/admin/products/edit?id=${p.id}" class="btn btn-primary btn-sm">
                                                    <fmt:message key="admin.action.edit"/>
                                                </a>

                                                <form action="/admin/products" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${p.id}">
                                                    <button class="btn btn-danger btn-sm"
                                                            onclick="return confirm('<fmt:message key="admin.confirm.delete"/>');">
                                                        <fmt:message key="admin.action.delete"/>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Pagination -->
                                <nav>
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="page">
                                            <li class="page-item ${page == currentPage ? 'active' : ''}">
                                                <a class="page-link" href="?page=${page}">${page}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>

                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Footer -->
            <jsp:include page="../../partials/footer.jsp"/>
        </div>
        <!-- main-panel ends -->
    </div>
</div>

<!-- Edit Modal -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <form id="editForm" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Product</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="editProductId" name="id">
                <div class="form-group">
                    <label>Name</label>
                    <input class="form-control" id="editName" name="name">
                </div>
                <div class="form-group">
                    <label>Price</label>
                    <input class="form-control" id="editPrice" name="price">
                </div>
                <div class="form-group">
                    <label>Discount</label>
                    <input class="form-control" id="editDiscount" name="discount">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="submit">Update</button>
            </div>
        </form>
    </div>
</div>

<!-- plugins:js -->
<%--<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>--%>
<!-- endinject -->
<!-- inject:js -->

<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<%--<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>--%>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>

<script>
    function openEditModal(id) {
        fetch(`/admin/products/get?id=` + id)
            .then(res => res.json())
            .then(p => {
                editProductId.value = p.id;
                editName.value = p.name;
                editPrice.value = p.price;
                editDiscount.value = p.discount;
                $('#editModal').modal('show');
            });
    }

    document.getElementById("editForm").addEventListener("submit", function (e) {
        e.preventDefault();
        fetch('/admin/products?action=update', {
            method: 'POST',
            body: new FormData(this)
        }).then(() => location.reload());
    });

    function deleteProduct(id) {
        if (confirm("Delete this product?")) {
            fetch(`/admin/products?action=delete&id=${id}`, {method: 'POST'})
                .then(() => document.getElementById("productRow" + id).remove());
        }
    }
</script>

</body>
</html>
