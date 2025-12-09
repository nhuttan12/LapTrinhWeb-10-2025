<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment</title>
</head>
<body>

<h2>Choose Payment Method</h2>

<h3>Order #${orderId}</h3>
<h3>Total: ${amount} VND</h3>

<form action="${pageContext.request.contextPath}/payment/momo" method="post">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <button type="submit">Pay with MoMo</button>
</form>

<br>
<form action="${pageContext.request.contextPath}/payment/vnpay" method="post">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <button type="submit" class="btn btn-primary">Thanh toán bằng VNPAY</button>
</form>
<br>

<form action="${pageContext.request.contextPath}/payment/vnpay" method="post">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <button type="submit">Pay with VNPAY</button>
</form>

<br>

<form action="${pageContext.request.contextPath}/checkout/payment" method="post">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <input type="hidden" name="method" value="cod">
    <button type="submit">Cash on Delivery</button>
</form>

</body>
</html>
