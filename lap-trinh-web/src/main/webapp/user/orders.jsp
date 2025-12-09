<%--
  Created by IntelliJ IDEA.
  User: NhutTan
  Date: 10/23/2025
  Time: 3:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="messages" />

<html>
<head>
    <title><fmt:message key="orders.title" /></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/magnific-popup.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/owl.theme.default.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/fonts/icomoon/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/user/css/style.css">
</head>
<body>
<div class="container bootstrap snippet">
    <%--    Header--%>
    <jsp:include page="header.jsp"/>

    <%--    Content    --%>
    <div class="row justify-content-center">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="user-profile-side-bar.jsp"/>
        </div>

        <div class="col-sm-9">
            <div class="tab-content">
                <div class="tab-pane active" id="home">
                    <table class="table table-striped table-bordered">
                        <thead class="thead-dark">
                        <tr>
                            <th>
                                <fmt:message key="orders.id"/>
                            </th>
                            <th>
                                <fmt:message key="orders.total"/>
                            </th>
                            <th>
                                <fmt:message key="orders.status"/>
                            </th>
                            <th>
                                <fmt:message key="orders.date"/>
                            </th>
                            <th class="text-center">
                                <fmt:message key="orders.actions"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td class="fw-bold text-primary">#${order.id}</td>

                                        <td>
                                            <fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/> đ
                                        </td>

                                        <td>
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${order.status == 'PENDING'}">bg-warning text-dark</c:when>
                                            <c:when test="${order.status == 'PAID'}">bg-info text-dark</c:when>
                                            <c:when test="${order.status == 'SHIPPED'}">bg-primary</c:when>
                                            <c:when test="${order.status == 'COMPLETED'}">bg-success</c:when>
                                            <c:when test="${order.status == 'CANCELLED'}">bg-danger</c:when>
                                            <c:otherwise>bg-secondary</c:otherwise>
                                        </c:choose>
                                    ">
                                            ${order.status}
                                    </span>
                                        </td>

                                        <td>
                                            <fmt:formatDate value="${order.createdAt}"/>
                                        </td>

                                        <td class="text-center">
                                            <div class="d-flex justify-content-center gap-2">
                                                <form action="${pageContext.request.contextPath}/order-detail" method="get">
                                                    <input type="hidden" name="orderId" value="${order.id}">
                                                    <button class="btn btn-sm btn-primary">
                                                        <i class="bi bi-file-text"></i> Xem
                                                    </button>
                                                </form>

                                                <c:if test="${order.status == 'PENDING'}">
                                                    <form action="${pageContext.request.contextPath}/cancel-order" method="post">
                                                        <input type="hidden" name="orderId" value="${order.id}">
                                                        <button class="btn btn-sm btn-outline-danger">
                                                            <i class="bi bi-x-circle"></i> Hủy
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="text-center text-muted fst-italic py-4">
                                        <fmt:message key="orders.empty"/>
                                    </td>
                                </tr>
                            </c:otherwise>

                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <%--    Footer--%>
    <jsp:include page="footer.jsp"/>
</div>

<%--Import library--%>
<script src="${pageContext.request.contextPath}/user/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/user/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/user/js/jquery.magnific-popup.min.js"></script>

</body>
</html>
