<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<footer class="site-footer border-top">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 mb-5 mb-lg-0">
                <div class="row">
                    <div class="col-md-12">
                        <h3 class="footer-heading mb-4">
                            <fmt:message key="footer.navigation"/>
                        </h3>
                    </div>
                    <div class="col-md-6 col-lg-4">
                        <ul class="list-unstyled">
                            <li>
                                <a href="#">
                                    <fmt:message key="footer.productsList"/>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <fmt:message key="footer.contact"/>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-6 col-lg-4">
                        <ul class="list-unstyled">
                            <li>
                                <a href="#">
                                    <fmt:message key="footer.cart"/>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <fmt:message key="footer.invoice"/>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-6 col-lg-4">
                        <ul class="list-unstyled">
                            <li>
                                <a href="#">
                                    <fmt:message key="footer.profile"/>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 mb-4 mb-lg-0">
                <h3 class="footer-heading mb-4">
                    <fmt:message key="footer.ads"/>
                </h3>
                <a href="#" class="block-6">
                    <img src="${pageContext.request.contextPath}/user/images/hero_1.jpg" alt="Image placeholder"
                         class="img-fluid rounded mb-4">
                    <h3 class="font-weight-light mb-0">
                        <fmt:message key="footer.ads.title"/>
                    </h3>
                    <p>
                        <fmt:message key="footer.ads.date"/>
                    </p>
                </a>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="block-5 mb-5">
                    <h3 class="footer-heading mb-4">
                        <fmt:message key="footer.contactInfo"/>
                    </h3>
                    <ul class="list-unstyled">
                        <li class="address">
                            <fmt:message key="footer.address"/>
                        </li>
                        <li class="phone">
                            <a href="tel://0854072069">
                                <fmt:message key="footer.phone"/>
                            </a>
                        </li>
                        <li class="email">
                            <fmt:message key="footer.email"/>
                        </li>
                    </ul>
                </div>

                <%--                <div class="block-7">--%>
                <%--                    <form action="#" method="post">--%>
                <%--                        <label for="email_subscribe" class="footer-heading">Subscribe</label>--%>
                <%--                        <div class="form-group">--%>
                <%--                            <input type="text" class="form-control py-4" id="email_subscribe" placeholder="Email">--%>
                <%--                            <input type="submit" class="btn btn-sm btn-primary" value="Send">--%>
                <%--                        </div>--%>
                <%--                    </form>--%>
                <%--                </div>--%>
            </div>
        </div>
    </div>
</footer>
