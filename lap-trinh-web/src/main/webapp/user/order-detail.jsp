<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chi tiết hoá đơn</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">

    <!-- bootstrap icon -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap-icons.css">
</head>

<body>

<div class="container bootstrap snippet">

    <%-- Header --%>
    <jsp:include page="header.jsp"/>

    <div class="row justify-content-center mt-4">

        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="user-profile-side-bar.jsp"/>
        </div>

        <!-- MAIN CONTENT -->
        <div class="col-md-9">

            <!-- ==================== THÔNG TIN HÓA ĐƠN ==================== -->
            <div class="card mb-4">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">Thông tin đơn hàng #${order.id}</h5>
                </div>
                <div class="card-body">

                    <table class="table table-bordered">
                        <tr>
                            <th width="200">Mã đơn hàng</th>
                            <td>#${order.id}</td>
                        </tr>

                        <tr>
                            <th>Trạng thái đơn</th>
                            <td>${order.status}</td>
                        </tr>

                        <tr>
                            <th>Phương thức thanh toán</th>
                            <td>${payment.method}</td>
                        </tr>

                        <tr>
                            <th>Trạng thái thanh toán</th>
                            <td>${payment.status}</td>
                        </tr>

                        <tr>
                            <th>Tổng tiền</th>
                            <td>
                                <fmt:formatNumber value="${order.price}"
                                                  type="number"
                                                  groupingUsed="true"/> ₫
                            </td>
                        </tr>

                        <tr>
                            <th>Ngày đặt hàng</th>
                            <td>
                                <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                        </tr>

                        <tr>
                            <th>Người đặt hàng</th>
                            <td>${user.fullName}</td>
                        </tr>

                        <tr>
                            <th>Số điện thoại</th>
                            <td>${userDetail.phone}</td>
                        </tr>

                        <tr>
                            <th>Địa chỉ giao hàng</th>
                            <td>${userDetail.address}</td>
                        </tr>
                    </table>

                </div>
            </div>

            <!-- ==================== CHI TIẾT SẢN PHẨM ==================== -->
            <div class="card">
                <div class="card-header bg-secondary text-white">
                    <h5 class="mb-0">Danh sách sản phẩm</h5>
                </div>
                <div class="card-body">

                    <table class="table table-striped table-bordered">
                        <thead class="thead-dark">
                        <tr>
                            <th scope="col">Sản phẩm</th>
                            <th scope="col">Số lượng</th>
                            <th scope="col">Giá tiền</th>
                            <th scope="col">Tổng giá</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="d" items="${orderDetails}">
                            <tr>
                                <td>
                                    <img src="${d.productImage}" width="60" class="mr-2">
                                        ${d.productName}
                                </td>

                                <td>${d.quantity}</td>

                                <td>
                                    <fmt:formatNumber value="${d.price}" type="number" groupingUsed="true"/> ₫
                                </td>

                                <td>
                                    <fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true"/> ₫
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>

                </div>
            </div>

        </div>
    </div>

    <%-- Footer --%>
    <jsp:include page="footer.jsp"/>

</div>

<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>

</body>
</html>
