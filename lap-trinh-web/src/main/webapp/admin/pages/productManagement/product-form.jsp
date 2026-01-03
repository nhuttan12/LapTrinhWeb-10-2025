<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html>
<head>
    <title>
        <fmt:message key="${detail == null ? 'admin.form.title.add' : 'admin.form.productInfo'}"/>
    </title>
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script>
      document.addEventListener("DOMContentLoaded", function () {

        const fileInput = document.querySelector(".file-upload");
        const previewImg = document.querySelector(".avatar"); // your <img>

        if (!fileInput || !previewImg) return;

        fileInput.addEventListener("change", function () {
          const file = this.files[0];

          if (!file) return;

          const reader = new FileReader();

          reader.onload = function (e) {
            previewImg.src = e.target.result;
          };

          reader.readAsDataURL(file);
        });

      });
    </script>
</head>

<body class="p-4">

<h3>
    <fmt:message key="${detail == null ? 'admin.form.title.add' : 'admin.form.title.edit'}"/>
</h3>

<c:set var="formAction" value="${(detail == null || detail.id == null) ? 'create' : 'update'}"/>

<form method="post"
      action="${pageContext.request.contextPath}/admin/products"
      enctype="multipart/form-data">

    <input type="hidden" name="action" value="${formAction}">

    <c:if test="${detail != null}">
        <input type="hidden" name="id" value="${detail.id}">
    </c:if>

    <div class="card p-3 mt-3">

        <h4>
            <fmt:message key="admin.form.productInfo"/>
        </h4>
        <div class="row">
            <!-- Name -->
            <div class="col-md-6 mb-3">
                <label>Tên sản phẩm</label>
                <input type="text" name="name" class="form-control" required
                       value="${detail.name}"/>
            </div>

            <%--            Thumbnail --%>
            <div class="col-md-6 mb-3">
                <div class="text-center">
                    <img src="${empty detail.thumbnailImages ? '' : detail.thumbnailImages}"
                         class="avatar img-circle img-thumbnail"
                         style="width: 150px; height: 150px;"
                         onerror="this.src='http://ssl.gstatic.com/accounts/ui/avatar_2x.png'"
                         alt="${detail.name}"/>

                    <input type="file" name="thumbnailFile" accept="image/*"
                           class="text-center center-block file-upload"/>
                </div>
            </div>

            <!-- Price -->
            <div class="col-md-3 mb-3">
                <label>Giá</label>
                <input type="number" step="0.01" name="price" class="form-control"
                       value="${detail.price}"/>
            </div>

            <!-- Discount -->
            <div class="col-md-3 mb-3">
                <label>Giảm giá</label>
                <input type="number" step="0.01" name="discount" class="form-control"
                       value="${detail.discount}"/>
            </div>

            <c:forEach var="entry" items="${detail.description}" varStatus="loop">
                <div class="col-md-6 mb-3">
                    <label>
                            ${entry.value.label}
                    </label>
                    <input name="${entry.key}"
                           value="${entry.value.value}"
                           class="form-control"
                           required/>
                </div>
            </c:forEach>

            <c:forEach var="entry" items="${specs}" varStatus="loop2">
                <div class="col-md-6 mb-3">
                    <label>
                            ${entry.value.label}
                    </label>

                    <input
                            name="${entry.key}"
                            value="${entry.value.value}"
                            class="form-control"
                            required
                    />
                </div>
            </c:forEach>

            <!-- Brand -->
            <div class="col-md-3 mb-3">
                <label>Thương hiệu</label>
                <input name="brandName" class="form-control"
                       value="${detail.brandName}"/>
            </div>

            <!-- Status -->
            <div class="col-md-3 mb-3">
                <label>Tình trạng</label>
                <select name="status" class="form-control">
                    <option value="active" ${detail != null && detail.status == 'active' ? 'selected' : ''}>Mở</option>
                    <option value="inactive" ${detail != null && detail.status == 'inactive' ? 'selected' : ''}>Đóng
                    </option>
                </select>
            </div>

            <c:if test="${detail.id == null}">
                <div class="d-flex flex-column w-100">
                    <h4>Thông số bổ sung</h4>
                    <div id="dynamic-specs" class="row">
                        <!-- Template field ẩn -->
                        <div class="col-md-6 mb-3 d-none" id="spec-template">
                            <input type="text" name="vi_terminology[]" class="form-control mb-1"
                                   placeholder="Tên (Tiếng Việt)">
                            <input type="text" name="en_terminology[]" class="form-control mb-1"
                                   placeholder="Tên (Tiếng Anh)">
                            <input type="text" name="value[]" class="form-control mb-1" placeholder="Giá trị">
                            <button type="button" class="btn btn-danger btn-sm remove-btn">Xóa</button>
                        </div>
                    </div>

                    <button type="button" class="btn btn-secondary mb-3" id="add-spec-btn">Thêm thông số</button>
                </div>
            </c:if>
        </div>

        <button type="submit" class="btn btn-primary mt-3">
            <fmt:message key="${detail == null ? 'admin.form.button.add' : 'admin.form.button.update'}"/>
        </button>
    </div>
</form>

<c:if test="${detail.id == null}">
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        const addBtn = document.getElementById("add-spec-btn");
        const container = document.getElementById("dynamic-specs");
        const template = document.getElementById("spec-template");

        function addSpecRow() {
          const clone = template.cloneNode(true);
          clone.id = "";
          clone.classList.remove("d-none");
          clone.classList.add("spec-row");

          clone.querySelectorAll("input").forEach(input => input.required = true);

          container.appendChild(clone);

          // Nút xoá
          const removeBtn = clone.querySelector(".remove-btn");
          removeBtn.addEventListener("click", () => clone.remove());
        }

        addBtn.addEventListener("click", addSpecRow);

        const form = container.closest("form");
        form.addEventListener("submit", function () {
          const rows = container.querySelectorAll(".spec-row");
          rows.forEach(row => {
            const inputs = row.querySelectorAll("input");
            const allEmpty = Array.from(inputs).every(input => input.value.trim() === "");
            if (allEmpty) row.remove();
          });
        });
      });
    </script>
</c:if>


<!-- Image preview script -->
<script>
  document.addEventListener("DOMContentLoaded", function () {

    const fileUpload = document.querySelector(".file-upload");
    const avatar = document.querySelector(".avatar");

    if (!fileUpload || !avatar) return;

    fileUpload.addEventListener("change", function () {
      const input = this;

      if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
          avatar.src = e.target.result;
        };

        reader.readAsDataURL(input.files[0]);
      }
    });
  });
</script>
</body>
</html>
