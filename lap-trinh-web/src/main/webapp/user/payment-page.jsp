<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payment for Order #${order.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">
</head>
<body>

<div class="container my-5">

    <h2 class="mb-4">Payment for Order #${order.id}</h2>

    <div class="alert alert-info">
        <strong>Total Amount: </strong> $${order.price}
    </div>

    <form action="${pageContext.request.contextPath}/checkout/payment" method="post">
        <input type="hidden" name="orderId" value="${order.id}">
        <input type="hidden" name="amount" value="${order.price}">

        <!-- Payment Methods -->
        <div class="border p-3 mb-3">
            <label>
                <input type="radio" name="method" value="COD" checked>
                Cash on Delivery (COD)
            </label>
        </div>

        <div class="border p-3 mb-3">
            <label data-toggle="collapse" href="#momo-info" role="button" aria-expanded="false" aria-controls="momo-info">
                <input type="radio" name="method" value="MOMO"> MOMO
            </label>
            <div class="collapse mt-2" id="momo-info">
                <div class="alert alert-secondary">
                    Scan the QR code in your MOMO app or use your MOMO wallet to pay. Make sure to include your Order ID in the payment note.
                </div>
            </div>
        </div>

        <div class="border p-3 mb-3">
            <label data-toggle="collapse" href="#vnpay-info" role="button" aria-expanded="false" aria-controls="vnpay-info">
                <input type="radio" name="method" value="VNPAY"> VNPAY
            </label>
            <div class="collapse mt-2" id="vnpay-info">
                <div class="alert alert-secondary">
                    You will be redirected to VNPAY payment page. Follow instructions to complete the payment.
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-primary mt-3">Pay Now</button>
    </form>

</div>

<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/aos.js"></script>
<script>
    $(document).ready(function () {
        $('[data-toggle="collapse"]').click(function () {
            var target = $(this).attr('href');
            $(target).collapse('toggle');
        });
    });
</script>
</body>
</html>
