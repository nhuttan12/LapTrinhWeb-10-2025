<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

    <!-- bootstrap icon -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap-icons.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">


    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">

    <!-- lightgallery plugins -->
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/user/lightgallery/css/lg-zoom.css"/>
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/user/lightgallery/css/lg-thumbnail.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/lightgallery/css/lightgallery-bundle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/lightgallery/css/lightgallery-bundle.min.css">

    <style>
        .star-rating i {
            font-size: 1.5rem;
            color: #ffc107;
            cursor: pointer;
            transition: transform 0.2s;
        }

        .star-rating i:hover {
            transform: scale(1.2);
        }

        #lightgallery {
            display: flex;
            flex-wrap: nowrap;
            gap: 5px;
            overflow-x: auto;
            overflow-y: hidden;
            max-width: 100%;
            cursor: grab;
        }

        #lightgallery a img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
    </style>
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
                    <strong class="text-black">${detail.name}</strong>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">
            <div class="row">
                <div class="col col-md-6">
                    <div>
                        <img src="${detail.thumbnailImages}" alt="${detail.name}" class="img-fluid">
                    </div>

                    <div id="lightgallery">
                        <c:forEach var="image" items="${detail.detailImages}">
                            <a href="${image}">
                                <img src="${image}" alt="${detail.name}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>

                <div class="col-md-6">
                    <h2 class="text-black">${detail.name}</h2>

                    <h4 class="mt-4 mb-3">Thông số kỹ thuật</h4>
                    <table class="table table-bordered">
                        <tbody>
                        <c:forEach var="entry" items="${detail.description}" varStatus="loop">
                            <tr class="${loop.index >= 5 ? 'collapse extra-spec' : ''}">
                                <td><strong>${entry.key}</strong></td>
                                <td>${entry.value}</td>
                            </tr>
                        </c:forEach>
                        <c:forEach var="entry" items="${specs}" varStatus="loop2">
                            <tr class="${loop2.index + fn:length(detail.description) >= 5 ? 'collapse extra-spec' : ''}">
                                <td>${entry.key}</td>
                                <td>${entry.value}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="text-center mt-3">
                        <button class="btn btn-outline-primary" type="button" id="toggleSpecsBtn">
                            Hiển thị thêm
                        </button>
                    </div>

                    <strong class="text-primary h4">
                        <c:choose>
                            <c:when test="${detail.discount > 0}">
                                <span class="text-muted" style="text-decoration: line-through;">
                                    <fmt:formatNumber
                                            value="${detail.price}"
                                            type="number"
                                            maxFractionDigits="2"
                                    /> vnđ
                                </span>
                                <span class="text-primary" style="margin-left: 8px;">
                                    <fmt:formatNumber
                                            value="${detail.price - detail.discount}"
                                            type="number"
                                            maxFractionDigits="2"
                                    /> vnđ
                                </span>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber
                                        value="${detail.price}"
                                        type="number"
                                        maxFractionDigits="2"
                                /> vnđ
                            </c:otherwise>
                        </c:choose>
                    </strong>

                    <div class="star-rating">
                        <c:forEach var="i" begin="1" end="5">
                            <c:choose>
                                <c:when test="${i <= detail.rating}">
                                    <i class="bi bi-star-fill text-warning"></i>
                                </c:when>

                                <c:when test="${i - detail.rating <= 0.5 && i - detail.rating > 0}">
                                    <i class="bi bi-star-half text-warning"></i>
                                </c:when>

                                <c:otherwise>
                                    <i class="bi bi-star text-warning"></i>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span class="ml-2 text-muted">
                            (<fmt:formatNumber value="${detail.rating}" maxFractionDigits="1"/>)
                        </span>
                    </div>

                    <div class="mb-5">
                        <div class="input-group mb-3" style="max-width: 120px;">
                            <div class="input-group-prepend">
                                <button class="btn btn-outline-primary js-btn-minus" type="button">&minus;</button>
                            </div>
                            <input type="text" class="form-control text-center" value="1" placeholder=""
                                   aria-label="Example text with button addon" aria-describedby="button-addon1">
                            <div class="input-group-append">
                                <button class="btn btn-outline-primary js-btn-plus" type="button">&plus;</button>
                            </div>
                        </div>
                    </div>

                    <p>
                        <a href="${pageContext.request.contextPath}/cart"
                           class="buy-now btn btn-sm btn-primary">
                            Add To Cart
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section block-3 site-blocks-2 bg-light">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-7 site-section-heading text-center pt-4">
                    <h2>Sản phẩm tương tự</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="nonloop-block-3 owl-carousel">
                        <c:forEach var="product" items="${sameBrandProducts}">
                            <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                                <div class="item">
                                    <div class="block-4 text-center">
                                        <figure class="block-4-image">
                                                <%--                                            <img src="${product.imageUrl}"--%>
                                                <%--                                                 alt="${product.name}"--%>
                                                <%--                                                 class="img-fluid object-fit-contain p-3"--%>
                                                <%--                                                 style="width: 100%; height: 300px"/>--%>
                                            <img src="${product.imageUrl}"
                                                 alt="${product.name}" class="img-fluid">
                                        </figure>
                                        <div class="block-4-text p-4">
                                            <h3>
                                                    ${product.name}
                                            </h3>
                                            <p class="text-primary font-weight-bold">
                                                <c:choose>
                                                    <c:when test="${detail.discount > 0}">
                                                    <span class="text-muted" style="text-decoration: line-through;">
                                                        <fmt:formatNumber
                                                                value="${detail.price}"
                                                                type="number"
                                                                maxFractionDigits="2"
                                                        /> vnđ
                                                    </span>
                                                        <span class="text-primary" style="margin-left: 8px;">
                                                        <fmt:formatNumber
                                                                value="${detail.price - detail.discount}"
                                                                type="number"
                                                                maxFractionDigits="2"
                                                        /> vnđ
                                                    </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber
                                                                value="${detail.price}"
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

<%--Light gallery--%>
<script src="${pageContext.request.contextPath}/user/lightgallery/lightgallery.umd.js"></script>

<!-- lightgallery plugins -->
<script src="${pageContext.request.contextPath}/user/lightgallery/plugins/thumbnail/lg-thumbnail.umd.js"></script>
<script src="${pageContext.request.contextPath}/user/lightgallery/plugins/zoom/lg-zoom.umd.js"></script>

<script>
  // Kiểm tra số lượng không âm
  document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('quantityInput');
    const minus = document.querySelector('.js-btn-minus');
    const plus = document.querySelector('.js-btn-plus');

    minus.addEventListener('click', () => {
      let val = parseInt(input.value) || 0;
      if (val <= 1) {
        alert('Số lượng của sản phẩm không được là số âm');
        input.value = 1;
      } else {
        input.value = val - 1;
      }
    });

    plus.addEventListener('click', () => {
      let val = parseInt(input.value) || 0;
      input.value = val + 1;
    });

    input.addEventListener('input', () => {
      let val = parseInt(input.value);
      if (isNaN(val) || val <= 0) {
        alert('Số lượng của sản phẩm không được là số âm');
        input.value = 1;
      }
    });
  });
</script>

<script>
  const toggleBtn = document.getElementById('toggleSpecsBtn');
  const extraSpecs = document.querySelectorAll('.extra-spec');
  let expanded = false;

  toggleBtn.addEventListener('click', () => {
    extraSpecs.forEach(row => row.classList.toggle('show'));
    expanded = !expanded;
    toggleBtn.textContent = expanded ? 'Thu gọn' : 'Hiển thị thêm';
  });
</script>

<script>
  lightGallery(document.getElementById('lightgallery'), {
    plugins: [lgThumbnail, lgZoom],
    licenseKey: '0000-0000-000-0000',
    thumbnail: true,
  });
</script>

<script>
  const stars = document.querySelectorAll('#ratingStars i');
  stars.forEach(star => {
    star.addEventListener('click', () => {
      const rating = star.getAttribute('data-value');
      stars.forEach(s => {
        s.classList.remove('fas');
        s.classList.add('far');
      });
      for (let i = 0; i < rating; i++) {
        stars[i].classList.remove('far');
        stars[i].classList.add('fas');
      }
      console.log("User rating:", rating);
    });
  });
</script>

<script>
  const gallery = document.getElementById('lightgallery');

  gallery.addEventListener('mouseenter', () => {
    gallery.style.overflowX = 'auto';
  });

  gallery.addEventListener('mouseleave', () => {
    gallery.style.overflowX = 'hidden';
  });

  gallery.addEventListener('wheel', (e) => {
    e.preventDefault();
    gallery.scrollLeft += e.deltaY;
  });
</script>

</body>
</html>