<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.Model.Order" %>
<%@ page import="java.util.List" %>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin - Orders</title>
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
                                            <th>ID</th>
                                            <th>User ID</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                            <th>Created At</th>
                                            <th>Updated At</th>
                                            <th>Action</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <% for (Order order : orders) { %>
                                        <tr>
                                            <td><%= order.getId() %></td>
                                            <td><%= order.getUserId() %></td>
                                            <td><%= order.getPrice() %></td>
                                            <td><%= order.getStatus() %></td>
                                            <td><%= order.getCreatedAt() %></td>
                                            <td><%= order.getUpdatedAt() %></td>
                                            <td>
                                                <form action="/admin/orders" method="post" class="form-inline">
                                                    <input type="hidden" name="orderId" value="<%= order.getId() %>"/>
                                                    <select name="status" class="form-control mr-2">
                                                        <option value="pending" <%= order.getStatus().toString().equals("pending") ? "selected" : "" %>>Pending</option>
                                                        <option value="paid" <%= order.getStatus().toString().equals("paid") ? "selected" : "" %>>Paid</option>
                                                        <option value="shipped" <%= order.getStatus().toString().equals("shipped") ? "selected" : "" %>>Shipped</option>
                                                        <option value="completed" <%= order.getStatus().toString().equals("completed") ? "selected" : "" %>>Completed</option>
                                                        <option value="cancelled" <%= order.getStatus().toString().equals("cancelled") ? "selected" : "" %>>Cancelled</option>
                                                    </select>
                                                    <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                                </form>
                                            </td>
                                        </tr>
                                        <% } %>
                                        </tbody>
                                    </table>
                                </div>
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
