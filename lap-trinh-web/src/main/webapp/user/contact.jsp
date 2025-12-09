<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>fmt:message key="title.contactPage"/></title>
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
                    <a href="${pageContext.request.contextPath}/home">
                        <fmt:message key="nav.home"/>
                    </a>
                    <span class="mx-2 mb-0">
                        /
                    </span>
                    <strong class="text-black">
                        <fmt:message key="nav.contact"/>
                    </strong>
                </div>
            </div>
        </div>
    </div>

    <div class="site-section">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h2 class="h3 mb-3 text-black">
                        <fmt:message key="contact.title"/>
                    </h2>
                </div>
                <div class="col-md-7">

                    <form action="#" method="post">

                        <div class="p-3 p-lg-5 border">
                            <div class="form-group row">
                                <div class="col-md-6">
                                    <label for="c_fname" class="text-black">
                                        <fmt:message key="contact.firstName"/>
                                        <span class="text-danger">
                                            *
                                        </span>
                                    </label>
                                    <input type="text" class="form-control" id="c_fname" name="c_fname">
                                </div>
                                <div class="col-md-6">
                                    <label for="c_lname" class="text-black">
                                        <fmt:message key="contact.lastName"/>
                                        <span class="text-danger">
                                            *
                                        </span>
                                    </label>
                                    <input type="text" class="form-control" id="c_lname" name="c_lname">
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-md-12">
                                    <label for="c_email" class="text-black">
                                        <fmt:message key="contact.email"/>
                                        <span class="text-danger">
                                            *
                                        </span>
                                    </label>
                                    <input type="email" class="form-control" id="c_email" name="c_email" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-md-12">
                                    <label for="c_subject" class="text-black">
                                        <fmt:message key="contact.subject"/>
                                    </label>
                                    <input type="text" class="form-control" id="c_subject" name="c_subject">
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-md-12">
                                    <label for="c_message" class="text-black">
                                        <fmt:message key="contact.message"/>
                                    </label>
                                    <textarea name="c_message" id="c_message" cols="30" rows="7"
                                              class="form-control"></textarea>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-lg-12">
                                    <input type="submit" class="btn btn-primary btn-lg btn-block"
                                           value="<fmt:message key='contact.send'/>">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5 ml-auto">
                    <div class="p-4 border mb-3">
                        <span class="d-block text-primary h6 text-uppercase">
                            <fmt:message key="contact.city.newyork"/>
                        </span>
                        <p class="mb-0"><fmt:message key="contact.address.sample"/></p>
                    </div>

                    <div class="p-4 border mb-3">
                        <span class="d-block text-primary h6 text-uppercase">
                            <fmt:message key="contact.city.london"/>
                        </span>
                        <p class="mb-0"><fmt:message key="contact.address.sample"/></p>
                    </div>

                    <div class="p-4 border mb-3">
                        <span class="d-block text-primary h6 text-uppercase">
                            <fmt:message key="contact.city.canada"/>
                        </span>
                        <p class="mb-0"><fmt:message key="contact.address.sample"/></p>
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