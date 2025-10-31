<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <jsp:include page="header.jsp"/>

    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-md-12 mb-0"><a href="${pageContext.request.contextPath}/home">Trang chủ</a> <span
                        class="mx-2 mb-0">/</span> <strong
                        class="text-black">Danh sách sản phẩm</strong></div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">

            <div class="row mb-5">
                <div class="col-md-9 order-2">

                    <div class="row">
                        <div class="col-md-12 mb-5">
                            <div class="float-md-left mb-4"><h2 class="text-black h5">Danh sách sản phẩm</h2></div>
                            <div class="d-flex">
                                <div class="dropdown mr-1 ml-md-auto">
                                    <button type="button" class="btn btn-secondary btn-sm dropdown-toggle"
                                            id="dropdownMenuOffset" data-toggle="dropdown" aria-haspopup="true"
                                            aria-expanded="false">
                                        Giá
                                    </button>
                                    <div class="dropdown-menu" aria-labelledby="dropdownMenuOffset">
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1
                                           &pageSize=${meta.pageSize}
                                           &sortBy=price
                                           &sortDirection=asc">Thấp
                                            tới cao</a>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1
                                           &pageSize=${meta.pageSize}
                                           &sortBy=price
                                           &sortDirection=desc">Cao
                                            tới thấp</a>
                                    </div>
                                </div>
                                <div class="btn-group">
                                    <button type="button" class="btn btn-secondary btn-sm dropdown-toggle"
                                            id="dropdownMenuReference" data-toggle="dropdown">Thứ tự
                                    </button>
                                    <div class="dropdown-menu" aria-labelledby="dropdownMenuReference">
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=name&sortDirection=asc">A
                                            to Z</a>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/product-list?page=1&pageSize=${meta.pageSize}&sortBy=name&sortDirection=desc">Z
                                            to A</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-5">
                        <c:forEach var="product" items="${products}">
                            <div class="col-sm-6 col-lg-4 mb-4" data-aos="fade-up">
                                <div class="block-4 text-center border">
                                    <figure class="block-4-image">
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"><img
                                                src="${product.thumbnail}"
                                                alt="${product.name}"
                                                class="img-fluid"></a>
                                    </figure>
                                    <div class="block-4-text p-4">
                                        <h3>
                                            <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
                                               class="fw-light">${product.name}</a>
                                        </h3>
                                        <p class="mb-0">${product.description}</p>
                                        <p class="text-primary font-weight-bold pt-1">
                                                ${product.price} vnđ
                                            <c:if test="${product.discount > 0}">
                                                <span class="text-muted" style="text-decoration:line-through;">
                                                    ${product.price + product.discount} vnđ
                                                </span>
                                            </c:if>
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

                <div class="col-md-3 order-1 mb-5 mb-md-0">
                    <div class="border p-4 rounded mb-4">
                        <h3 class="mb-3 h6 text-uppercase text-black d-block">Thương hiệu</h3>
                        <ul class="list-unstyled mb-0">
                            <c:forEach var="brand" items="${brands}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/product-list?brandId=${brand.id}&page=1&pageSize=12"
                                       class="d-flex">
                                        <span>${brand.name}</span>
                                        <span
                                                class="text-black ml-auto">(${brand.productCount})</span></a></li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div class="border p-4 rounded mb-4">
                        <div class="mb-4">
                            <h3 class="mb-3 h6 text-uppercase text-black d-block">Lọc sản phẩm</h3>
                            <div id="slider-range" class="border-primary"></div>
                            <input type="text" name="text" id="amount" class="form-control border-0 pl-0 bg-white"
                                   disabled=""/>
                        </div>

                        <div class="mb-4">
                            <h3 class="mb-3 h6 text-uppercase text-black d-block">Size</h3>
                            <label for="s_sm" class="d-flex">
                                <input type="checkbox" id="s_sm" class="mr-2 mt-1"> <span class="text-black">Small (2,319)</span>
                            </label>
                            <label for="s_md" class="d-flex">
                                <input type="checkbox" id="s_md" class="mr-2 mt-1"> <span class="text-black">Medium (1,282)</span>
                            </label>
                            <label for="s_lg" class="d-flex">
                                <input type="checkbox" id="s_lg" class="mr-2 mt-1"> <span class="text-black">Large (1,392)</span>
                            </label>
                        </div>

                        <div class="mb-4">
                            <h3 class="mb-3 h6 text-uppercase text-black d-block">Color</h3>
                            <a href="#" class="d-flex color-item align-items-center">
                                <span class="bg-danger color d-inline-block rounded-circle mr-2"></span> <span
                                    class="text-black">Red (2,429)</span>
                            </a>
                            <a href="#" class="d-flex color-item align-items-center">
                                <span class="bg-success color d-inline-block rounded-circle mr-2"></span> <span
                                    class="text-black">Green (2,298)</span>
                            </a>
                            <a href="#" class="d-flex color-item align-items-center">
                                <span class="bg-info color d-inline-block rounded-circle mr-2"></span> <span
                                    class="text-black">Blue (1,075)</span>
                            </a>
                            <a href="#" class="d-flex color-item align-items-center">
                                <span class="bg-primary color d-inline-block rounded-circle mr-2"></span> <span
                                    class="text-black">Purple (1,075)</span>
                            </a>
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