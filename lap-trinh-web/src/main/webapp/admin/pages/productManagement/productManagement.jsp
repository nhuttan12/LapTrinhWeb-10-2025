<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Management</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px 15px;
            text-align: center;
        }
        th {
            background-color: #f8f8f8;
        }
        img.thumbnail {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 6px;
        }
        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 25px;
            gap: 5px;
        }
        .pagination a {
            padding: 8px 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
            color: #333;
            text-decoration: none;
        }
        .pagination a.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .pagination a:hover {
            background-color: #e9ecef;
        }
        .action-btn {
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .action-btn button {
            padding: 6px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            color: white;
        }
        .edit-btn { background-color: #28a745; }
        .delete-btn { background-color: #dc3545; }

        /* Modal style */
        #editModal {
            display: none;
            position: fixed;
            z-index: 1000;
            left:0;
            top:0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
        }
        #editModal .modal-content {
            background: white;
            width: 500px;
            margin: 10% auto;
            padding: 20px;
            position: relative;
            border-radius: 6px;
        }
        #editModal .close {
            position: absolute;
            top:10px;
            right:10px;
            cursor:pointer;
            font-size:18px;
            font-weight:bold;
        }
        #editModal input, #editModal select {
            width: 100%;
            margin: 5px 0 10px 0;
            padding: 6px;
        }
    </style>
</head>
<body>
<h2>Product List</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Thumbnail</th>
        <th>Name</th>
        <th>Price</th>
        <th>Discount</th>
        <th>Brand</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${products}">
        <tr id="productRow${product.id}">
            <td>${product.id}</td>
            <td>
                <c:choose>
                    <c:when test="${not empty product.thumbnail}">
                        <img class="thumbnail" src="${product.thumbnail}" alt="${product.name}">
                    </c:when>
                    <c:otherwise>
                        <img class="thumbnail" src="default.jpg" alt="${product.name}">
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.discount}</td>
            <td>${product.brand}</td>
            <td class="action-btn">
                <button class="edit-btn" onclick="openEditModal(${product.id})">Edit</button>
                <button class="delete-btn" onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination -->
<div class="pagination">
    <c:if test="${currentPage > 1}">
        <a href="?page=${currentPage - 1}">&laquo; Prev</a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
        <a href="?page=${currentPage + 1}">Next &raquo;</a>
    </c:if>
</div>

<!-- Edit Modal -->
<div id="editModal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h3>Edit Product</h3>
        <form id="editForm">
            <input type="hidden" name="id" id="editProductId">
            Name: <input type="text" name="name" id="editName"><br>
            Price: <input type="text" name="price" id="editPrice"><br>
            Discount: <input type="text" name="discount" id="editDiscount"><br>
            <!-- Thêm các field khác theo DTO -->
            <button type="submit">Update</button>
        </form>
    </div>
</div>

<script>
    function openEditModal(id) {
        fetch(`/admin/products/get?id=${id}`)
            .then(res => res.json())
            .then(product => {
                document.getElementById("editProductId").value = product.id;
                document.getElementById("editName").value = product.name;
                document.getElementById("editPrice").value = product.price;
                document.getElementById("editDiscount").value = product.discount;
                // thêm các field khác
                document.getElementById("editModal").style.display = "block";
            });
    }

    function closeModal() {
        document.getElementById("editModal").style.display = "none";
    }

    document.getElementById("editForm").addEventListener("submit", function(e){
        e.preventDefault();
        const formData = new FormData(this);
        fetch('/admin/products?action=update', {
            method: 'POST',
            body: formData
        })
            .then(res => res.text())
            .then(data => {
                alert("Product updated!");
                closeModal();
                location.reload(); // hoặc update row trực tiếp
            });
    });

    function deleteProduct(id){
        if(confirm("Are you sure to delete?")){
            fetch(`/admin/products?action=delete&id=${id}`, { method:'POST' })
                .then(res => res.text())
                .then(data => {
                    alert("Product deleted!");
                    document.getElementById(`productRow${id}`).remove();
                });
        }
    }
</script>
</body>
</html>
