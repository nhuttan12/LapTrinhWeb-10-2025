<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${product != null ? "Edit Product" : "Add Product"}</title>
</head>
<body>
<h2>${product != null ? "Edit Product" : "Add Product"}</h2>

<form action="${pageContext.request.contextPath}/admin/products?action=save" method="post">
    <input type="hidden" name="id" value="${product.id}" />

    <label>Name:</label>
    <input type="text" name="name" value="${product.name}" required/><br/>

    <label>Description:</label>
    <textarea name="description">${product.productDetail != null ? product.productDetail.description : ''}</textarea><br/>

    <label>Price:</label>
    <input type="number" name="price" value="${product.price}" required/><br/>

    <label>Discount:</label>
    <input type="number" name="discount" value="${product.discount}"/><br/>

    <label>Category:</label>
    <input type="text" name="category" value="${product.category}"/><br/>

    <label>Thumbnail URL:</label>
    <input type="text" name="thumbnail" value="${product.productImage != null ? product.productImage.image.url : ''}"/><br/>

    <label>Status:</label>
    <select name="status">
        <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
        <option value="INACTIVE" ${product.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
    </select><br/>

    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/admin/products?action=list">Cancel</a>
</form>
</body>
</html>
