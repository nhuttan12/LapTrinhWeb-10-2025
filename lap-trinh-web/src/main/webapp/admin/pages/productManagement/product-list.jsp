<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="product-list-container" style="padding: 30px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h2 style="margin: 0; color: #333;">Product List</h2>
        <a href="${pageContext.request.contextPath}/admin/products?action=form"
           class="btn-add"
           style="background-color: #4b49ac; color: white; padding: 10px 16px; border-radius: 8px; text-decoration: none; font-weight: 500;">
            + Add New Product
        </a>
    </div>

    <div style="background-color: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
        <table style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead style="background-color: #f7f8fc; color: #333; font-weight: 600;">
            <tr>
                <th style="padding: 12px;">ID</th>
                <th style="padding: 12px;">Thumbnail</th>
                <th style="padding: 12px;">Name</th>
                <th style="padding: 12px;">Price</th>
                <th style="padding: 12px;">Discount</th>
                <th style="padding: 12px;">Description</th>
                <th style="padding: 12px; text-align: center;">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty products}">
                    <tr>
                        <td colspan="7" style="padding: 16px; text-align: center; color: #888;">
                            No products found.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="p" items="${products}">
                        <tr style="border-bottom: 1px solid #eee;">
                            <td style="padding: 12px;">${p.id}</td>
                            <td style="padding: 12px;">
                                <c:if test="${not empty p.thumbnail}">
                                    <img src="${p.thumbnail}" alt="${p.name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px;">
                                </c:if>
                            </td>
                            <td style="padding: 12px;">${p.name}</td>
                            <td style="padding: 12px;">${p.price}</td>
                            <td style="padding: 12px;">${p.discount}</td>
                            <td style="padding: 12px;">${p.description}</td>
                            <td style="padding: 12px; text-align: center;">
                                <a href="${pageContext.request.contextPath}/admin/products?action=form&id=${p.id}"
                                   class="btn-edit"
                                   style="background-color: #ffcc00; color: #000; padding: 6px 14px; border-radius: 6px; text-decoration: none; font-weight: 500; margin-right: 6px;">
                                    Edit
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/products?action=delete&id=${p.id}"
                                   class="btn-delete"
                                   onclick="return confirm('Are you sure you want to delete this product?');"
                                   style="background-color: #f44336; color: white; padding: 6px 14px; border-radius: 6px; text-decoration: none; font-weight: 500;">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>

        <!-- Pagination -->
        <div class="mt-3 text-center">
            <c:if test="${currentPage > 1}">
                <a href="products?page=${currentPage - 1}" class="btn btn-outline-primary btn-sm">Prev</a>
            </c:if>
            <span>Page ${currentPage}</span>
            <a href="products?page=${currentPage + 1}" class="btn btn-outline-primary btn-sm">Next</a>
        </div>
    </div>
</div>
