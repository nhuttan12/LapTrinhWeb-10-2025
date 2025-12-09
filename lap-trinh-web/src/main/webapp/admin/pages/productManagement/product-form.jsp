<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product Form</title>
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body class="p-4">

<h3>${product == null ? "Add New Product" : "Edit Product"}</h3>

<form method="post" action="/admin/products">
    <input type="hidden" name="action"
           value="${product == null ? 'create' : 'update'}">

    <c:if test="${product != null}">
        <input type="hidden" name="id" value="${product.id}">
    </c:if>

    <div class="card p-3 mt-3">

        <h4>Product Info</h4>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label>Name</label>
                <input name="name" class="form-control" required
                       value="${product.name}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Price</label>
                <input type="number" step="0.01" name="price"
                       class="form-control"
                       value="${product.price}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Discount</label>
                <input type="number" name="discount" class="form-control"
                       value="${product.discount}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="active" ${product.status.productStatus == 'active' ? 'selected' : ''}>Active</option>
                    <option value="inactive" ${product.status.productStatus == 'inactive' ? 'selected' : ''}>Inactive</option>

                </select>
            </div>

            <div class="col-md-6 mb-3">
                <label>Thumbnail URL</label>
                <input name="thumbnail" class="form-control"
                       value="${product.thumbnail}">
            </div>
        </div>

        <hr>

        <h4>Product Detail</h4>

        <div class="row">

            <div class="col-md-3 mb-3">
                <label>Brand</label>
                <select name="brandId" class="form-control">
                    <c:forEach var="b" items="${brands}">
                        <option value="${b.id}"
                            ${detail.brand.id == b.id ? 'selected' : ''}>
                                ${b.name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-3 mb-3">
                <label>OS</label>
                <input name="os" class="form-control" value="${detail.os}">
            </div>

            <div class="col-md-3 mb-3">
                <label>RAM</label>
                <input type="number" name="ram" class="form-control"
                       value="${detail.ram}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Storage</label>
                <input type="number" name="storage" class="form-control"
                       value="${detail.storage}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Battery</label>
                <input type="number" name="batteryCapacity" class="form-control"
                       value="${detail.batteryCapacity}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Screen Size</label>
                <input type="number" step="0.1" name="screenSize" class="form-control"
                       value="${detail.screenSize}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Resolution</label>
                <input name="screenResolution" class="form-control"
                       value="${detail.screenResolution}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Mobile Network</label>
                <input name="mobileNetwork" class="form-control"
                       value="${detail.mobileNetwork}">
            </div>

            <div class="col-md-3 mb-3">
                <label>CPU</label>
                <input name="cpu" class="form-control" value="${detail.cpu}">
            </div>

            <div class="col-md-3 mb-3">
                <label>GPU</label>
                <input name="gpu" class="form-control" value="${detail.gpu}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Water Resistance</label>
                <input name="waterResistance" class="form-control"
                       value="${detail.waterResistance}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Max Charge Watt</label>
                <input type="number" name="maxChargeWatt" class="form-control"
                       value="${detail.maxChargeWatt}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Design</label>
                <input name="design" class="form-control"
                       value="${detail.design}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Memory Card</label>
                <input name="memoryCard" class="form-control"
                       value="${detail.memoryCard}">
            </div>

            <div class="col-md-3 mb-3">
                <label>CPU Speed</label>
                <input type="number" step="0.01" name="cpuSpeed" class="form-control"
                       value="${detail.cpuSpeed}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Release Date</label>
                <input type="date" name="releaseDate" class="form-control"
                       value="${detail.releaseDate}">
            </div>

            <div class="col-md-3 mb-3">
                <label>Rating</label>
                <input type="number" step="0.1" name="rating" class="form-control"
                       value="${detail.rating}">
            </div>

            <div class="col-md-12 mb-3">
                <label>Description</label>
                <textarea name="description" class="form-control"
                          rows="3">${detail.description}</textarea>
            </div>
        </div>

        <button class="btn btn-primary mt-3">
            ${product == null ? "Create Product" : "Update Product"}
        </button>

    </div>
</form>

</body>
</html>
