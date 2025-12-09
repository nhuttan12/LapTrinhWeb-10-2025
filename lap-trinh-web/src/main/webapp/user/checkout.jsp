<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Thanh Toán</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">
</head>
<body>

<div class="site-wrap">
    <jsp:include page="header.jsp"/>

    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-md-12 mb-0">
                    <a href="${pageContext.request.contextPath}/home">Home</a>
                    <span class="mx-2 mb-0">/</span>
                    <a href="${pageContext.request.contextPath}/cart">Cart</a>
                    <span class="mx-2 mb-0">/</span>
                    <strong class="text-black">Checkout</strong>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">
            <form action="${pageContext.request.contextPath}/checkout/payment" method="post">
                <div class="row">

                    <!-- Thông tin khách hàng -->
                    <div class="col-md-6 mb-5 mb-md-0">
                        <h2 class="h3 mb-3 text-black">Billing Details</h2>
                        <div class="p-3 p-lg-5 border">

                            <div class="form-group">
                                <label class="text-black">Full Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="fullName"
                                       value="${fullName}" required>
                            </div>

                            <div class="form-group">
                                <label class="text-black">Email <span class="text-danger">*</span></label>
                                <input type="email" class="form-control" name="email"
                                       value="${email}" required>
                            </div>

                            <div class="form-group">
                                <label class="text-black">Phone <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="phone"
                                       value="${phone}" required>
                            </div>

                            <div class="form-group">
                                <label class="text-black">Address <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="address"
                                       value="${address}" required>
                            </div>

                            <div class="form-group">
                                <label class="text-black">Order Notes</label>
                                <textarea name="notes" class="form-control" placeholder="Ghi chú đơn hàng"></textarea>
                            </div>

                        </div>
                    </div>

                    <!-- Thông tin sản phẩm và tổng tiền -->
                    <div class="col-md-6">
                        <h2 class="h3 mb-3 text-black">Your Order</h2>
                        <div class="p-3 p-lg-5 border">

                            <table class="table site-block-order-table mb-5">
                                <thead>
                                <th>Product</th>
                                <th>Total</th>
                                </thead>
                                <tbody>
                                <c:set var="total" value="0"/>
                                <c:forEach var="item" items="${cart.cartDetails}">
                                    <tr>
                                        <td>${item.product.name} <strong class="mx-2">x</strong> ${item.quantity}</td>
                                        <td>
                                            <fmt:formatNumber value="${item.product.price * item.quantity}" type="number" groupingUsed="true"/> vnđ
                                        </td>
                                    </tr>
                                    <c:set var="total" value="${total + (item.product.price * item.quantity)}"/>
                                </c:forEach>

                                <tr>
                                    <td class="text-black font-weight-bold"><strong>Cart Subtotal</strong></td>
                                    <td class="text-black font-weight-bold"><fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> vnđ</td>
                                </tr>
                                <tr>
                                    <td class="text-black font-weight-bold"><strong>Order Total</strong></td>
                                    <td class="text-black font-weight-bold"><strong><fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> vnđ</strong></td>
                                </tr>
                                </tbody>
                            </table>

                            <!-- Phương thức thanh toán -->
                            <div class="border p-3 mb-3">
                                <label>
                                    <input type="radio" name="method" value="cod" checked>
                                    Cash on Delivery (COD)
                                </label>
                            </div>

                            <div class="border p-3 mb-3">
                                <label>
                                    <input type="radio" name="method" value="paypal_test">
                                    PayPal Test Payment
                                </label>
                            </div>

                            <div class="border p-3 mb-3">
                                <label>
                                    <input type="radio" name="method" value="vnpay_test">
                                    VNPAY Test Payment
                                </label>
                            </div>

                            <div class="form-group">
                                <button class="btn btn-primary btn-lg py-3 btn-block" type="submit">
                                    Place Order
                                </button>
                            </div>

                        </div>
                    </div>

                </div>
            </form>
        </div>
    </div>

    <jsp:include page="footer.jsp"/>
</div>

<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/main.js"></script>

</body>
</html>
