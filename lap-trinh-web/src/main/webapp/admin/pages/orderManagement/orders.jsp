<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin - Quản lý thôgn tin hoá đơn</title>
    <!-- plugins:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/feather/feather.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/ti-icons/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/vendors/css/vendor.bundle.base.css">
    <!-- endinject -->
    <!-- inject:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/vertical-layout-light/style.css">
    <!-- endinject -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/admin/images/favicon.png"/>
</head>

<body>
<div class="container-scroller">

    <!-- Navbar -->
    <jsp:include page="../../partials/navbar.jsp" />

    <div class="container-fluid page-body-wrapper">

        <!-- Settings panel (nếu có) -->
        <jsp:include page="../../partials/settings-panel.jsp" />

        <!-- Sidebar -->
        <jsp:include page="../../partials/sidebar.jsp" />

        <!-- Main content -->
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Danh sách đơn hàng</h4>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th>Mã hoá đơn</th>
                                            <th>Tài khoản người đặt hàng</th>
                                            <th>Tổng giá tiền</th>
                                            <th>Trạng thái thanh toán</th> <!-- payment -->
                                            <th>Trạng thái vận chuyển</th> <!-- shipping -->
                                            <th>Ngày khởi tạo hoá đơn</th>
                                            <th>Ngày điều chỉnh hoá đơn</th>
                                            <th>Thao tác</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>${order.id}</td>
                                                <td>${order.username}</td>
                                                <td>${order.price}</td>

                                                <!-- Payment Status -->
<%--                                                PENDING, COMPLETED, FAILED, REFUNDED, UNPAID, PAID;--%>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.paymentStatus == 'COMPLETED'}">Đã thanh toán hoàn tất</c:when>
                                                        <c:when test="${order.paymentStatus == 'UNPAID'}">Chưa thanh toán</c:when>
                                                        <c:when test="${order.paymentStatus == 'PAID'}">Đã thanh toán</c:when>
                                                        <c:when test="${order.paymentStatus == 'PENDING'}">Chờ thanh toán</c:when>
                                                        <c:when test="${order.paymentStatus == 'FAILED'}">Thất bại</c:when>
                                                        <c:when test="${order.paymentStatus == 'REFUNDED'}">Hoàn tiền</c:when>
                                                        <c:otherwise>${order.paymentStatus}</c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Shipping Status -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.shippingStatus == 'PENDING'}">Chờ xử lý</c:when>
                                                        <c:when test="${order.shippingStatus == 'SHIPPED'}">Đang giao</c:when>
                                                        <c:when test="${order.shippingStatus == 'COMPLETED'}">Hoàn tất</c:when>
                                                        <c:when test="${order.shippingStatus == 'CANCELLED'}">Đã huỷ</c:when>
                                                        <c:otherwise>${order.shippingStatus}</c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>${order.createdAt}</td>
                                                <td>${order.updatedAt}</td>

                                                <!-- Action -->
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/admin/orders"
                                                          method="post"
                                                          class="form-inline">

                                                        <input type="hidden" name="orderId" value="${order.id}"/>

                                                        <!-- Payment Status -->
                                                        <select name="paymentStatus" class="form-control mr-2">
                                                            <c:forEach var="ps" items="${['PENDING','COMPLETED','FAILED','REFUNDED']}">
                                                                <option value="${ps}"
                                                                    ${ps == order.paymentStatus ? 'selected' : ''}>
                                                                        ${ps}
                                                                </option>
                                                            </c:forEach>
                                                        </select>

                                                        <!-- Shipping Status -->
                                                        <select name="shippingStatus" class="form-control mr-2">
                                                            <c:forEach var="ss" items="${['PENDING','SHIPPED','COMPLETED','CANCELLED']}">
                                                                <option value="${ss}"
                                                                    ${ss == order.shippingStatus ? 'selected' : ''}>
                                                                        ${ss}
                                                                </option>
                                                            </c:forEach>
                                                        </select>

                                                        <button type="submit" class="btn btn-primary btn-sm">
                                                            Update
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination -->
                                <nav class="mt-3">
                                    <ul class="pagination justify-content-center">

                                        <!-- Prev -->
                                        <li class="page-item ${!meta.hasPrevious ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="?page=${meta.currentPage - 1}&pageSize=${meta.pageSize}">
                                                Prev
                                            </a>
                                        </li>

                                        <!-- Page numbers -->
                                        <c:forEach begin="1" end="${meta.totalPages}" var="i">
                                            <li class="page-item ${i == meta.currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="?page=${i}&pageSize=${meta.pageSize}">
                                                        ${i}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <!-- Next -->
                                        <li class="page-item ${!meta.hasNext ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="?page=${meta.currentPage + 1}&pageSize=${meta.pageSize}">
                                                Next
                                            </a>
                                        </li>

                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Footer -->
            <jsp:include page="../../partials/footer.jsp" />
        </div>
        <!-- main-panel ends -->
    </div>
    <!-- page-body-wrapper ends -->
</div>
<!-- container-scroller ends -->

<!-- plugins:js -->
<script src="${pageContext.request.contextPath}/admin/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/admin/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/template.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/todolist.js"></script>
<!-- endinject -->

</body>
</html>
