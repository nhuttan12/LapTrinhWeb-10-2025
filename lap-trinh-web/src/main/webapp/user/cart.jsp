<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>
        <fmt:message key="cart.title"/>
    </title>
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

    <!-- bootstrap icon -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap-icons.css">
</head>
<body>

<div class="site-wrap">
    <jsp:include page="header.jsp"/>

    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-md-12 mb-0">
                    <a href="${pageContext.request.contextPath}/home">
                        <fmt:message key="nav.home"/>
                    </a>
                    <span class="mx-2 mb-0">/</span>
                    <strong class="text-black">
                        <fmt:message key="cart.title"/>
                    </strong>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">

            <!-- Giỏ Hàng -->
            <form class="col-md-12 mb-5" method="post" action="${pageContext.request.contextPath}/cart">
                <input type="hidden" name="action" value="update">
                <div class="site-blocks-table">
                    <table class="table table-bordered table-hover">
                        <thead class="thead-dark">
                        <tr>
                            <th class="product-thumbnail">Hình Ảnh</th>
                            <th class="product-name">
                                <fmt:message key="cart.product"/>
                            </th>
                            <th class="product-price">
                                <fmt:message key="cart.price"/>
                            </th>
                            <th class="product-quantity">
                                <fmt:message key="cart.quantity"/>
                            </th>
                            <th class="product-total">
                                <fmt:message key="cart.total"/>
                            </th>
                            <th class="product-remove">
                                <fmt:message key="cart.remove"/>
                            </th>
                            <th class="product-buy">
                                <fmt:message key="cart.buy"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${cart != null}">
                            <c:forEach var="detail" items="${cartDetails}">
                                <tr>
                                    <td class="product-thumbnail">
                                        <a href="${pageContext.request.contextPath}/product-detail?productId=${detail.product.id}">
                                            <img src="${detail.product.thumbnail}" alt="Image" class="img-fluid">
                                        </a>
                                    </td>

                                    <td class="product-name">
                                        <h2 class="h5 text-black">${detail.product.name}</h2>
                                    </td>

                                    <td>
                                        <fmt:formatNumber value="${detail.product.price}" type="number"
                                                          groupingUsed="true"/> vnđ
                                    </td>

                                    <td>
                                        <div class="input-group mb-3" style="max-width: 120px;">
                                            <div class="input-group-prepend">
                                                <button class="btn btn-outline-primary js-btn-minus" type="button">
                                                    &minus;
                                                </button>
                                            </div>
                                            <input type="text" name="quantity_${detail.product.id}"
                                                   class="form-control text-center" value="${detail.quantity}"
                                                   min="1">
                                            <div class="input-group-append">
                                                <button class="btn btn-outline-primary js-btn-plus" type="button">
                                                    &plus;
                                                </button>
                                            </div>
                                        </div>
                                    </td>

                                    <td>
                                        <fmt:formatNumber value="${detail.product.price * detail.quantity}"
                                                          type="number" groupingUsed="true"/> vnđ
                                    </td>

                                    <td>
                                        <a href="${pageContext.request.contextPath}/cart?action=remove&productId=${detail.product.id}"
                                           class="btn btn-danger btn-sm">
                                            Xóa
                                        </a>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/checkout-single"
                                              method="post">
                                            <input type="hidden" name="productId" value="${detail.product.id}">
                                            <input type="hidden" name="quantity" value="${detail.quantity}">
                                            <button type="submit" class="btn btn-success btn-sm">
                                                <fmt:message key="cart.buy"/>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <div class="row mb-5">
                    <div class="col-md-6 mb-3 mb-md-0">
                        <button type="submit" class="btn btn-primary btn-block">
                            <fmt:message key="cart.update"/>
                        </button>
                    </div>
                    <div class="col-md-6">
                        <a href="${pageContext.request.contextPath}/product-list"
                           class="btn btn-outline-primary btn-block">
                            <fmt:message key="cart.continue"/>
                        </a>
                    </div>
                </div>
            </form>

            <!-- Mã Giảm Giá -->
            <div class="row mb-5">
                <div class="col-md-6">
                    <label class="text-black h4" for="coupon">
                        <fmt:message key="cart.coupon"/>
                    </label>
                    <p>
                        <fmt:message key="cart.enterCouponIfHaveOne"/>
                    </p>
                    <div class="input-group">
                        <input type="text" class="form-control py-3" id="coupon"
                               placeholder="<fmt:message key="cart.coupon.placeholder"/>">
                        <div class="input-group-append">
                            <button class="btn btn-primary btn-sm">
                                <fmt:message key="cart.coupon.apply"/>
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Tổng Tiền -->
                <div class="col-md-6 pl-5">
                    <div class="row justify-content-end">
                        <div class="col-md-7">

                            <div class="row mb-4">
                                <div class="col-md-12 text-right border-bottom">
                                    <h3 class="text-black h4 text-uppercase">
                                        <fmt:message key="cart.grandtotal"/>
                                    </h3>
                                </div>
                            </div>

                            <c:set var="subtotal" value="0"/>
                            <c:forEach var="detail" items="${cartDetails}">
                                <c:set var="subtotal" value="${subtotal + (detail.product.price * detail.quantity)}"/>
                            </c:forEach>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <span class="text-black">
                                        <fmt:message key="cart.provisionalCalculation"/>
                                    </span>
                                </div>
                                <div class="col-md-6 text-right">
                                    <strong class="text-black">
                                        <fmt:formatNumber value="${subtotal}" type="number"
                                                                                 groupingUsed="true"/> vnđ
                                    </strong>
                                </div>
                            </div>

                            <div class="row mb-5">
                                <div class="col-md-6">
                                    <span class="text-black">
                                        <fmt:message key="cart.grandtotal"/>
                                    </span>
                                </div>
                                <div class="col-md-6 text-right">
                                    <strong class="text-black"><fmt:formatNumber value="${subtotal}" type="number"
                                                                                 groupingUsed="true"/> vnđ</strong>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <a href="${pageContext.request.contextPath}/checkout"
                                       class="btn btn-primary btn-lg py-3 btn-block">
                                        <fmt:message key="cart.checkout"/>
                                    </a>
                                </div>
                            </div>

                        </div>
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
