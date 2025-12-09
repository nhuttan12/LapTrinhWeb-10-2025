<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><fmt:message key="title.productList"/></title>
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
<body>

<div class="site-wrap">
    <jsp:include page="header.jsp"/>

    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-md-12 mb-0">
                    <a href="${pageContext.request.contextPath}/home"><fmt:message key="nav.home"/></a>
                    <span class="mx-2 mb-0">/</span>
                    <strong class="text-black"><fmt:message key="products.list"/></strong>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">

            <div class="row mb-5">

                <!-- Left filter -->
                <div class="col-md-3 order-1 mb-5 mb-md-0">

                    <!-- Brand -->
                    <div class="border p-4 rounded mb-4">
                        <h3 class="mb-3 h6 text-uppercase text-black d-block">
                            <fmt:message key="filter.brand"/>
                        </h3>
                        <ul class="list-unstyled mb-0">
                            <c:forEach var="brand" items="${brands}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/product-list?brandId=${brand.id}&page=1&pageSize=12"
                                       class="d-flex">
                                        <span>${brand.name}</span>
                                        <span class="text-black ml-auto">(${brand.productCount})</span></a></li>
                            </c:forEach>
                        </ul>
                    </div>

                    <!-- Filters -->
                    <form action="${pageContext.request.contextPath}/product-filter" method="get">
                        <div class="border p-4 rounded mb-4">

                            <!-- Price range -->
                            <div class="mb-4">
                                <h3 class="mb-3 h6 text-uppercase text-black d-block">
                                    <fmt:message key="filter.price"/>
                                </h3>

                                <div
                                        id="slider-range"
                                        class="border-primary"
                                        data-min="${criteria.minPrice != null && criteria.minPrice > 0 ? criteria.minPrice : 0}"
                                        data-max="${criteria.maxPrice != null && criteria.maxPrice > 0 ? criteria.maxPrice : 30000000}">
                                </div>

                                <!-- Giá tiền -->
                                <input
                                        type="text"
                                        name="priceRange"
                                        id="amount"
                                        class="form-control border-0 pl-0 bg-white"
                                        readonly
                                        value="<c:out value='${criteria.minPrice > 0 ? criteria.minPrice : 0} - ${criteria.maxPrice > 0 ? criteria.maxPrice : 30000000}'/>"/>
                            </div>

                            <!-- OS -->
                            <div class="mb-4">
                                <h3 class="mb-3 h6 text-uppercase text-black d-block">
                                    <fmt:message key="filter.os"/>
                                </h3>

                                <c:set var="osOptions" value="${['Android', 'iOS']}"/>
                                <c:set var="selectedOs" value="${fn:join(criteria.osList, ',')}"/>

                                <c:forEach var="os" items="${osOptions}">
                                    <label class="d-flex">
                                        <input type="checkbox" name="os" value="${os}" class="mr-2 mt-1"
                                                <c:if test="${fn:contains(selectedOs, os)}">
                                                    checked
                                                </c:if>
                                        />
                                        <span class="text-black">${os}</span>
                                    </label>
                                </c:forEach>
                            </div>

                            <!-- RAM -->
                            <div class="mb-4">
                                <h3 class="mb-3 h6 text-uppercase text-black d-block">
                                    <fmt:message key="filter.ram"/>
                                </h3>

                                <c:set var="ramOptions" value="${['4', '6', '8', '12', '16']}"/>
                                <c:set var="selectedRam" value="${fn:join(criteria.ramList, ',')}"/>

                                <c:forEach var="ram" items="${ramOptions}">
                                    <label class="d-flex">
                                        <input type="checkbox" name="ram" value="${ram}" class="mr-2 mt-1"
                                                <c:if test="${fn:contains(selectedRam, ram)}">
                                                    checked
                                                </c:if>
                                        />
                                        <span class="text-black">${ram} GB</span>
                                    </label>
                                </c:forEach>
                            </div>

                            <!-- Storage -->
                            <div class="mb-4">
                                <h3 class="mb-3 h6 text-uppercase text-black d-block">
                                    <fmt:message key="filter.storage"/>
                                </h3>

                                <c:set var="storageOptions" value="${['64', '128', '256', '512']}"/>
                                <c:set var="selectedStorage" value="${fn:join(criteria.storageList, ',')}"/>

                                <c:forEach var="storage" items="${storageOptions}">
                                    <label class="d-flex">
                                        <input type="checkbox" name="storage" value="${storage}" class="mr-2 mt-1"
                                                <c:if test="${fn:contains(selectedStorage, storage)}">
                                                    checked
                                                </c:if>
                                        />
                                        <span class="text-black">${storage} GB</span>
                                    </label></c:forEach>
                            </div>

                            <!-- Max wat charge -->
                            <div class="mb-4">
                                <h3 class="mb-3 h6 text-uppercase text-black d-block">
                                    <fmt:message key="filter.charge"/>
                                </h3>

                                <c:set var="chargeOptions" value="${['20', '25', '40', '45', '80']}"/>
                                <c:set var="selectedCharge" value="${fn:join(criteria.chargeList, ',')}"/>

                                <c:forEach items="${chargeOptions}" var="charge">
                                    <label class="d-flex">
                                        <input type="checkbox" name="charge" value="${charge}" class="mr-2 mt-1"
                                                <c:if test="${fn:contains(selectedCharge, charge)}">
                                                    checked
                                                </c:if>
                                        />
                                        <span class="text-black">${charge} W</span>
                                    </label>
                                </c:forEach>
                            </div>

                            <button type="submit" class="btn btn-sm btn-primary">
                                <fmt:message key="filter.apply"/>
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Right product list -->
                <div class="col-md-9 order-2">

                    <!-- Sort bar -->
                    <div class="row">
                        <div class="col-md-12 mb-5">
                            <div class="float-md-left mb-4">
                                <h2 class="text-black h5"><fmt:message key="products.list"/></h2>
                            </div>

                            <div class="d-flex">

                                <!-- Price sort -->
                                <div class="dropdown mr-1 ml-md-auto">
                                    <button class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">
                                        <fmt:message key="sort.price"/>
                                    </button>

                                    <div class="dropdown-menu">
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=price&sortDirection=asc">
                                            <fmt:message key="sort.lowToHigh"/>
                                        </a>

                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=price&sortDirection=desc">
                                            <fmt:message key="sort.highToLow"/>
                                        </a>
                                    </div>
                                </div>

                                <!-- Name sort -->
                                <div class="btn-group">
                                    <button class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">
                                        <fmt:message key="sort.order"/>
                                    </button>

                                    <div class="dropdown-menu">
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=name&sortDirection=asc">
                                            <fmt:message key="sort.aToZ"/>
                                        </a>

                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=name&sortDirection=desc">
                                            <fmt:message key="sort.zToA"/>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Product List -->
                    <div class="row mb-5">
                        <c:forEach var="product" items="${products}">
                            <div class="col-sm-6 col-lg-4 mb-4" data-aos="fade-up">
                                <div class="block-4 text-center border">
                                    <figure class="block-4-image">
                                        <a href="${pageContext.request.contextPath}/product-detail?productId=${product.id}"><img
                                                src="${product.thumbnail}"
                                                alt="${product.name}"
                                                class="img-fluid"></a>
                                    </figure>
                                    <div class="block-4-text p-4">
                                        <h3>
                                            <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
                                               class="fw-light">${product.name}</a>
                                        </h3>
                                        <p class="text-primary font-weight-bold pt-1">
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
                        </c:forEach>
                    </div>
                    <div class="row" data-aos="fade-up">
                        <div class="col-md-12 text-center">
                            <div class="site-block-27">
                                <ul>
                                    <!-- Base URL with context path and pageSize -->
                                    <c:set var="baseUrl"
                                           value="${pageContext.request.contextPath}/product-list?pageSize=${meta.pageSize}"/>

                                    <!-- Keep sortBy and sortDir in URL -->
                                    <c:if test="${not empty meta.sortBy}">
                                        <c:forEach var="i" begin="0" end="${fn:length(meta.sortBy) - 1}">
                                            <c:set var="baseUrl"
                                                   value="${baseUrl}&sortBy=${meta.sortBy[i]}&sortDir=${meta.sortDirections[i]}"/>
                                        </c:forEach>
                                    </c:if>

                                    <!-- 🧩 Preserve filters (criteria) -->
                                    <c:if test="${not empty criteria.osList}">
                                        <c:forEach var="os" items="${criteria.osList}">
                                            <c:set var="baseUrl" value="${baseUrl}&os=${os}"/>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty criteria.ramList}">
                                        <c:forEach var="ram" items="${criteria.ramList}">
                                            <c:set var="baseUrl" value="${baseUrl}&ram=${ram}"/>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty criteria.storageList}">
                                        <c:forEach var="storage" items="${criteria.storageList}">
                                            <c:set var="baseUrl" value="${baseUrl}&storage=${storage}"/>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty criteria.chargeList}">
                                        <c:forEach var="charge" items="${criteria.chargeList}">
                                            <c:set var="baseUrl" value="${baseUrl}&charge=${charge}"/>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty productName}">
                                        <c:set var="baseUrl" value="${baseUrl}&productName=${productName}"/>
                                    </c:if>

                                    <!-- Preserve price range -->
                                    <c:if test="${not empty param.priceRange}">
                                        <c:set var="baseUrl"
                                               value="${baseUrl}&priceRange=${fn:escapeXml(param.priceRange)}"/>
                                    </c:if>

                                    <!-- Previous Button -->
                                    <c:choose>
                                        <c:when test="${meta.currentPage > 1}">
                                            <li>
                                                <a href="${baseUrl}&page=${meta.currentPage - 1}">&lt;</a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="disabled"><span>&lt;</span></li>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Page Numbers -->
                                    <c:set var="startPage" value="${meta.currentPage - 2}"/>
                                    <c:set var="endPage" value="${meta.currentPage + 2}"/>
                                    <c:if test="${startPage < 1}">
                                        <c:set var="startPage" value="1"/>
                                    </c:if>
                                    <c:if test="${endPage > meta.totalPages}">
                                        <c:set var="endPage" value="${meta.totalPages}"/>
                                    </c:if>

                                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                        <c:choose>
                                            <c:when test="${i == meta.currentPage}">
                                                <li class="active"><span>${i}</span></li>
                                            </c:when>
                                            <c:otherwise>
                                                <li><a href="${baseUrl}&page=${i}">${i}</a></li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <!-- Next Button -->
                                    <c:choose>
                                        <c:when test="${meta.currentPage < meta.totalPages}">
                                            <li>
                                                <a href="${baseUrl}&page=${meta.currentPage + 1}">&gt;</a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="disabled"><span>&gt;</span></li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
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