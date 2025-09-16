<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Shoppers &mdash; Colorlib e-Commerce Template</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">


    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">

</head>
<body>

<div class="site-wrap">
    <%@ include file="header.jsp" %>

    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-md-12 mb-0"><a href="index.jsp">Home</a> <span class="mx-2 mb-0">/</span> <strong
                        class="text-black">Contact</strong></div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">
            <div class="row">
                <div class="col-md-12 text-center">
                    <span class="icon-check_circle display-3 text-success"></span>
                    <h2 class="display-3 text-black">Thank you!</h2>
                    <p class="lead mb-5">Your order was successfuly completed.</p>
                    <p><a href="/product-list" class="btn btn-sm btn-primary">Back to shop</a></p>
                </div>
            </div>
        </div>
    </div>

    <%@ include file="footer.jsp" %>
</div>

<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/user/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery.magnific-popup.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/aos.js"></script>

<script src="${pageContext.request.contextPath}/user/js/main.js"></script>

</body>
</html>