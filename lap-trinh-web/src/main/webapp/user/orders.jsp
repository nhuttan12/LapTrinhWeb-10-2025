<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hoá đơn người dùng</title>

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">


</head>

<body>
<div class="container bootstrap snippet">

    <jsp:include page="header.jsp"/>

    <div class="row justify-content-center">

        <div class="col-md-3">
            <jsp:include page="user-profile-side-bar.jsp"/>
        </div>

        <div class="col-sm-9">

            <h4 class="mb-3">Danh sách hoá đơn</h4>

            <table class="table table-hover align-middle shadow-sm rounded">
                <thead class="table-dark">
                <tr>
                    <th>Mã hoá đơn</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Ngày đặt</th>
                    <th class="text-center">Thao tác</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${not empty orders}">
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td class="fw-bold text-primary">#${order.id}</td>

                                <td>
                                    <fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/> đ
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${order.paymentStatus.status == 'pending' && order.shippingStatus.orderStatus == 'pending'}">
                                            <span class="order-status pending">Chờ xử lý</span>
                                        </c:when>
                                        <c:when test="${order.paymentStatus.status == 'paid' && order.shippingStatus.orderStatus == 'pending'}">
                                            <span class="order-status paid">Đã thanh toán</span>
                                        </c:when>
                                        <c:when test="${order.shippingStatus.orderStatus == 'shipped'}">
                                            <span class="order-status shipped">Đang giao</span>
                                        </c:when>
                                        <c:when test="${order.shippingStatus.orderStatus == 'completed'}">
                                            <span class="order-status completed">Hoàn thành</span>
                                        </c:when>
                                        <c:when test="${order.shippingStatus.orderStatus == 'cancelled'}">
                                            <span class="order-status cancelled">Đã huỷ</span>
                                        </c:when>
                                    </c:choose>
                                </td>

                                <td>
                                    <fmt:formatDate value="${order.createdAt}"/>
                                </td>

                                <td class="text-center">
                                    <div class="action-icons">

                                        <!-- Xem chi tiết -->
                                        <a href="${pageContext.request.contextPath}/order-detail?orderId=${order.id}"
                                           class="action-icon view"
                                           title="Xem chi tiết">
                                            <i class="bi bi-eye"></i>
                                        </a>

                                        <!-- Huỷ đơn -->
                                        <c:if test="${order.shippingStatus.orderStatus == 'pending'}">
                                        <form action="${pageContext.request.contextPath}/cancel-order"
                                                  method="post"
                                                  class="d-inline">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit"
                                                        class="action-icon cancel"
                                                        title="Huỷ đơn">
                                                    <i class="bi bi-x-lg"></i>
                                                </button>
                                            </form>
                                        </c:if>


                                    </div>
                                </td>

                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="text-center text-muted fst-italic py-4">
                                Không có hoá đơn nào được tìm thấy.
                            </td>
                        </tr>
                    </c:otherwise>

                </c:choose>
                </tbody>
            </table>

        </div>
    </div>

    <jsp:include page="footer.jsp"/>

</div>

</body>
</html>
