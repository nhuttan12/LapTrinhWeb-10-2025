<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body class="p-4">

<div class="d-flex justify-content-between mb-3">
    <h3>Product Management</h3>
    <a href="/admin/products/add" class="btn btn-success">+ Add Product</a>
</div>

<table class="table table-bordered text-center align-middle">
    <thead class="thead-dark">
    <tr>
        <th>ID</th>
        <th>Thumbnail</th>
        <th>Name</th>
        <th>Price</th>
        <th>Discount (%)</th>
        <th>Brand</th>
        <th style="width:150px;">Action</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="p" items="${products}">
        <tr>
            <td>${p.id}</td>

            <td>
                <img src="${p.thumbnail}" style="width:60px;height:60px;object-fit:cover;">
            </td>

            <td>${p.name}</td>
            <td>${p.price}</td>
            <td>${p.discount}</td>
            <td>${p.brand}</td>

            <td>
                <a href="/admin/products/edit?id=${p.id}" class="btn btn-primary btn-sm">
                    Edit
                </a>

                <form action="/admin/products" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${p.id}">
                    <button class="btn btn-danger btn-sm"
                            onclick="return confirm('Delete this product?');">
                        Delete
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination -->
<nav>
    <ul class="pagination justify-content-center">
        <c:forEach begin="1" end="${totalPages}" var="page">
            <li class="page-item ${page == currentPage ? 'active' : ''}">
                <a class="page-link" href="?page=${page}">${page}</a>
            </li>
        </c:forEach>
    </ul>
</nav>

</body>
</html>
