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

                                <h4 class="card-title">Danh sách sản phẩm</h4>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th>Mã sản phẩm</th>
                                            <th>Hình ảnh</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Giá</th>
                                            <th>Giảm giá</th>
                                            <th>Thương hiệu</th>
                                            <th>Thao tác</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="product" items="${products}">
                                            <tr id="productRow${product.id}">
                                                <td>${product.id}</td>
                                                <td>
                                                    <img src="${not empty product.thumbnail ? product.thumbnail : 'default.jpg'}"
                                                         style="width:60px;height:60px;object-fit:cover;border-radius:5px">
                                                </td>
                                                <td>${product.name}</td>
                                                <td>${product.price}</td>
                                                <td>${product.discount}</td>
                                                <td>${product.brand}</td>
                                                <td>
                                                    <button class="btn btn-success btn-sm"
                                                            onclick="openEditModal(${product.id})">
                                                        Edit
                                                    </button>
                                                    <button class="btn btn-danger btn-sm"
                                                            onclick="deleteProduct(${product.id})">
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination -->
                                <nav class="mt-3">
                                    <ul class="pagination justify-content-center">
                                        <c:if test="${currentPage > 1}">
                                            <li class="page-item">
                                                <a class="page-link" href="?page=${currentPage - 1}">Prev</a>
                                            </li>
                                        </c:if>

                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <a class="page-link" href="?page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <li class="page-item">
                                                <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                                            </li>
                                        </c:if>
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
<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
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
