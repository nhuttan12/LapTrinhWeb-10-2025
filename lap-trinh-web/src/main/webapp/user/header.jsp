<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<header class="site-navbar" role="banner">
    <div class="site-navbar-top">
        <div class="container">
            <div class="row align-items-center">

                <div class="col-6 col-md-4 order-2 order-md-1 site-search-icon text-left">
                    <form class="site-block-top-search" method="get"
                          action="${pageContext.request.contextPath}/product-list">
                        <span class="icon icon-search2"></span>
                        <input name="productName" type="text" class="form-control border-0"
                               placeholder="Tìm kiếm sản phẩm">
                    </form>
                </div>

                <div class="col-12 mb-3 mb-md-0 col-md-4 order-1 order-md-2 text-center">
                    <div class="site-logo">
                        <a href="${pageContext.request.contextPath}/home" class="js-logo-clone">Shoppers</a>
                    </div>
                </div>

                <div class="col-6 col-md-4 order-3 order-md-3 text-right">
                    <div class="site-top-icons">
                        <ul>
                            <!-- Change language -->
                            <li class="language-icon" style="display: inline-block;">
                                <div class="btn-group">
                                    <button type="button"
                                            class="btn btn-outline-primary dropdown-toggle py-1 px-2"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <i class="bi bi-translate" style="font-size: 18px;"></i>
                                    </button>

                                    <div class="dropdown-menu dropdown-menu-right">
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/change-language?lang=vi">
                                            🇻🇳 Tiếng Việt
                                        </a>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/change-language?lang=en">
                                            🇺🇸 English
                                        </a>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/profile">
                                    <span class="icon icon-person"></span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="icon icon-heart-o"></span>
                                </a>
                            </li>
                            <!-- Tính tổng số lượng sản phẩm trong giỏ -->
                            <c:set var="cartQuantity" value="0"/>
                            <c:if test="${not empty cart}">
                                <c:forEach var="detail" items="${cart.cartDetails}">
                                    <c:set var="cartQuantity" value="${cartQuantity + detail.quantity}"/>
                                </c:forEach>
                            </c:if>
                            <li>
                                <a href="${pageContext.request.contextPath}/cart" class="site-cart">
                                    <span class="icon icon-shopping_cart"></span>
                                    <span class="count">${cartQuantity}</span>
                                    <%--                                    <span class="count">${sessionScope.cartQuantity != null ? sessionScope.cartQuantity : 0}</span>--%>
                                </a>
                            </li>
                            <li class="d-inline-block d-md-none ml-md-0">
                                <a href="#" class="site-menu-toggle js-menu-toggle">
                                    <span class="icon-menu"></span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <nav class="site-navigation text-right text-md-center" role="navigation">
        <div class="container">
            <ul class="site-menu js-clone-nav d-none d-md-block">
                <li class="active">
                    <a href="${pageContext.request.contextPath}/home">
                        <fmt:message key="nav.home"/>
                    </a>
                </li>
                <li class="has-children">
                    <a href="#">
                        <fmt:message key="nav.brands"/>
                    </a>
                    <ul class="dropdown">
                        <c:forEach var="brand" items="${brands}">
                            <li>
                                <a href="${pageContext.request.contextPath}/product-list?brandId=${brand.id}">${brand.name}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/product-list">
                        <fmt:message key="nav.products"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/product-list?newProduct=true&page=1&pageSize=12">
                        <fmt:message key="nav.newProducts"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/contact">
                        <fmt:message key="nav.contact"/>
                    </a>
                </li>
            </ul>
        </div>
    </nav>
</header>
