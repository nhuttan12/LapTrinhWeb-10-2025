<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Detail</title>
</head>
<body>
<h2>Product Detail</h2>

<p><strong>ID:</strong> ${product.id}</p>
<p><strong>Name:</strong> ${product.name}</p>
<p><strong>Description:</strong> ${product.productDetail != null ? product.productDetail.description : ''}</p>
<p><strong>Price:</strong> ${product.price}</p>
<p><strong>Discount:</strong> ${product.discount}</p>
<p><strong>Category:</strong> ${product.category}</p>
<p><strong>Status:</strong> ${product.status}</p>
<p><strong>Thumbnail:</strong>
    <c:if test="${product.productImage != null}">
        <img src="${product.productImage.image.url}" alt="${product.name}" style="width: 100px; height: 100px; object-fit: cover;">
    </c:if>
</p>

<a href="${pageContext.request.contextPath}/admin/products?action=list">Back to List</a>
</body>
</html>
