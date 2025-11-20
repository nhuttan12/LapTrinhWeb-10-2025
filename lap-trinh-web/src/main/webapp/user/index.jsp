<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Shoppers &mdash; Colorlib e-Commerce Template</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">

    <!-- bootstrap icon -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap-icons.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">

</head>
<style>
    .block-2-item .image {
        width: 100%;
        height: 400px;
        overflow: hidden;
    }

    .block-2-item .image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
    }
</style>
<body>

<div class="site-wrap">
    <jsp:include page="header.jsp"/>

    <div class="site-blocks-cover"
         style="background-image: url('https://www.bechtle.com/dam/jcr:3664adb4-bc64-46a5-b0f8-1a48852e1150/cw04_mainbanner_samsung-eu.jpg');"
         data-aos="fade">
        <div class="container">
            <div class="row align-items-start align-items-md-center justify-content-end">
                <div class="col-md-5 text-center text-md-left pt-5 pt-md-0">
                    <h1 class="mb-2 text-white">
                        <fmt:message key="hero.title"/>
                    </h1>
                    <div class="intro-text text-center text-md-left">
                        <p class="mb-4 text-white">
                            <fmt:message key="hero.description"/>
                        </p>
                        <p>
                            <a href="${pageContext.request.contextPath}/product-list"
                               class="btn btn-sm btn-primary">
                                <fmt:message key="hero.button"/>
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section site-section-sm site-blocks-1">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-lg-4 d-lg-flex mb-4 mb-lg-0 pl-4" data-aos="fade-up" data-aos-delay="">
                    <div class="icon mr-4 align-self-start">
                        <span class="icon-truck"></span>
                    </div>
                    <div class="text">
                        <h2 class="text-uppercase"><fmt:message key="shipping.title"/></h2>
                        <p><fmt:message key="shipping.content"/></p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4 d-lg-flex mb-4 mb-lg-0 pl-4" data-aos="fade-up" data-aos-delay="100">
                    <div class="icon mr-4 align-self-start">
                        <span class="icon-refresh2"></span>
                    </div>
                    <div class="text">
                        <h2 class="text-uppercase"><fmt:message key="refund.title"/></h2>
                        <p><fmt:message key="refund.content"/></p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4 d-lg-flex mb-4 mb-lg-0 pl-4" data-aos="fade-up" data-aos-delay="200">
                    <div class="icon mr-4 align-self-start">
                        <span class="icon-help"></span>
                    </div>
                    <div class="text">
                        <h2 class="text-uppercase"><fmt:message key="support.title"/></h2>
                        <p>
                            <fmt:message key="support.content"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section site-blocks-2">
        <div class="container">
            <div class="row">
                <c:forEach var="b" items="${brands}" varStatus="loop">
                    <div class="col-sm-6 col-md-6 col-lg-4 mb-4 mb-lg-0"
                         data-aos="fade"
                         data-aos-delay="${loop.index * 100}">
                        <a class="block-2-item"
                           href="${pageContext.request.contextPath}/product-list?brandId=${b.id}&page=1&pageSize=12">
                            <figure class="image">
                                <img src="${b.image}" alt="${b.name}" class="img-fluid">
                            </figure>
                            <div class="text">
                                <span class="text-uppercase">
                                    <fmt:message key="brand.label"/>
                                </span>
                                <h3>${b.name}</h3>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="site-section block-3 site-blocks-2 bg-light">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-7 site-section-heading text-center pt-4">
                    <h2>
                        <fmt:message key="featured.title"/>
                    </h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="nonloop-block-3 owl-carousel">
                        <c:forEach var="product" items="${randomProducts}">
                            <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                                <div class="item">
                                    <div class="block-4 text-center">
                                        <figure class="block-4-image">
                                            <img src="${product.imageUrl}"
                                                 alt="${product.name}" class="img-fluid">
                                        </figure>
                                        <div class="block-4-text p-4">
                                            <h3>
                                                    ${product.name}
                                            </h3>

                                            <p class="text-primary font-weight-bold">
                                                <c:choose>
                                                    <c:when test="${product.discount > 0}">
                                                    <span class="text-muted" style="text-decoration: line-through;">
                                                        <fmt:formatNumber
                                                                value="${product.price}"
                                                                type="number"
                                                                maxFractionDigits="2"
                                                        /> vnđ
                                                    </span>
                                                        <span class="text-primary" style="margin-left: 8px;">
                                                        <fmt:formatNumber
                                                                value="${product.price - product.discount}"
                                                                type="number"
                                                                maxFractionDigits="2"
                                                        /> vnđ
                                                    </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber
                                                                value="${product.price}"
                                                                type="number"
                                                                maxFractionDigits="2"
                                                        /> vnđ
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp"/>
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