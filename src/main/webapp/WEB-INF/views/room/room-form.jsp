<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${room != null ? "Chỉnh Sửa" : "Thêm"} Phòng Trọ - BOA</title>

    <!-- Bootstrap & Font Awesome -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root{
            --primary-1: #667eea;
            --primary-2: #764ba2;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, var(--primary-1) 0%, var(--primary-2) 100%);
            min-height: 100vh;
            padding: 20px 0;
        }

        .navbar {
            background: rgba(255,255,255,0.95);
            backdrop-filter: blur(8px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.08);
        }

        .form-container {
            max-width: 920px;
            margin: 30px auto;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.18);
            overflow: hidden;
        }

        .form-header {
            background: linear-gradient(135deg, var(--primary-1) 0%, var(--primary-2) 100%);
            padding: 28px 24px;
            color: #fff;
            text-align: center;
        }

        .form-header h2 {
            margin: 0;
            font-size: 1.75rem;
            font-weight: 700;
        }

        .form-body {
            padding: 36px;
        }

        .form-label {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 6px;
        }

        .form-control, .form-select {
            padding: 12px 14px;
            border: 2px solid #e6e9ee;
            border-radius: 10px;
            transition: all .25s;
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--primary-1);
            box-shadow: 0 0 0 4px rgba(102,126,234,0.08);
        }

        .input-group-text {
            background: linear-gradient(135deg, var(--primary-1) 0%, var(--primary-2) 100%);
            color: #fff;
            border: none;
            font-weight: 700;
            border-radius: 8px 0 0 8px;
        }

        .info-box {
            background: #eef2ff;
            border-left: 4px solid var(--primary-1);
            padding: 12px 14px;
            border-radius: 8px;
            margin-bottom: 18px;
            color: #2a2f6f;
        }

        .required { color: #e74c3c; }

        .invalid-feedback { color: #e74c3c; }

        /* BUTTONS: ensure equal width and same padding */
        .btn-action {
            padding: 12px 0;         /* vertical padding only */
            border-radius: 10px;
            font-weight: 600;
            text-align: center;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .btn-back {
            background: #fff;
            color: var(--primary-1);
            border: 2px solid var(--primary-1);
        }
        a.btn-action {
            text-decoration: none !important;
        }


        .btn-back:hover {
            background: #f8f9ff;
            color: var(--primary-1);
        }

        .btn-submit {
            background: linear-gradient(135deg, var(--primary-1) 0%, var(--primary-2) 100%);
            color: #fff;
            border: none;
        }

        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 22px rgba(102,126,234,0.25);
        }

        /* Use flex utilities: each button will flex:1 and thus be equal width */
        .btn-row {
            display:flex;
            gap:16px;
            width:100%;
        }
        .btn-row .flex-eq {
            flex:1 1 0;
            min-width: 0; /* prevents overflow for long text */
        }

        /* smaller screens: keep buttons stacked only if absolutely needed */
        @media (max-width: 420px) {
            .btn-row {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/trang-chu">
            <i class="fas fa-home"></i> BOA
        </a>
    </div>
</nav>

<!-- Form Container -->
<div class="form-container">
    <div class="form-header">
        <h2>
            <i class="fas fa-${room != null ? "edit" : "plus-circle"}"></i>
            ${room != null ? "Chỉnh Sửa" : "Thêm"} Phòng Trọ Mới
        </h2>
    </div>

    <div class="form-body">
        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            </div>
        </c:if>

        <div class="info-box">
            <i class="fas fa-info-circle"></i>
            <strong>Lưu ý:</strong> Vui lòng điền đầy đủ thông tin. Số phòng phải là duy nhất trong hệ thống của bạn.
        </div>

        <form method="post" action="${pageContext.request.contextPath}/quan-ly-phong" id="roomForm" novalidate>
            <input type="hidden" name="action" value="${room != null ? 'update' : 'add'}"/>
            <c:if test="${room != null}">
                <input type="hidden" name="roomId" value="${room.roomId}"/>
            </c:if>

            <div class="row g-3">
                <div class="col-md-6">
                    <label for="roomNumber" class="form-label form-label">Số Phòng <span class="required">*</span></label>
                    <input type="text" id="roomNumber" name="roomNumber"
                           class="form-control"
                           value="${room != null ? room.roomNumber : ''}"
                           placeholder="Ví dụ: A101" required>
                    <div class="invalid-feedback">Vui lòng nhập số phòng (ví dụ: A101, B202)</div>
                </div>

                <div class="col-md-6">
                    <label for="type" class="form-label form-label">Loại Phòng <span class="required">*</span></label>
                    <select id="type" name="type" class="form-select" required>
                        <option value="">-- Chọn Loại Phòng --</option>
                        <option value="Phòng Thường" ${room != null && room.type == 'Phòng Thường' ? 'selected' : ''}>Phòng Thường</option>
                        <option value="Phòng VIP" ${room != null && room.type == 'Phòng VIP' ? 'selected' : ''}>Phòng VIP</option>
                        <option value="Căn Hộ" ${room != null && room.type == 'Căn Hộ' ? 'selected' : ''}>Căn Hộ</option>
                    </select>
                    <div class="invalid-feedback">Vui lòng chọn loại phòng</div>
                </div>

                <div class="col-md-6">
                    <label for="price" class="form-label form-label">Giá Thuê (VNĐ/tháng) <span class="required">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fas fa-dong-sign"></i></span>
                        <input type="number" id="price" name="price" class="form-control"
                               value="${room != null ? room.price : ''}"
                               placeholder="Ví dụ: 3000000" min="100000" step="100000" required>
                    </div>
                    <div class="invalid-feedback">Giá thuê phải từ 100,000 VNĐ trở lên</div>
                </div>

                <div class="col-md-6">
                    <label for="floor" class="form-label form-label">Tầng <span class="required">*</span></label>
                    <input type="number" id="floor" name="floor" class="form-control"
                           value="${room != null ? room.floor : ''}"
                           placeholder="Ví dụ: 1, 2, 3..." min="1" max="50" required>
                    <div class="invalid-feedback">Tầng phải từ 1 đến 50</div>
                </div>

                <div class="col-12">
                    <label for="status" class="form-label form-label">Trạng Thái <span class="required">*</span></label>
                    <select id="status" name="status" class="form-select" required>
                        <option value="">-- Chọn Trạng Thái --</option>
                        <option value="Trống" ${room != null && room.status == 'Trống' ? 'selected' : ''}>Trống</option>
                        <option value="Đang thuê" ${room != null && room.status == 'Đang thuê' ? 'selected' : ''}>Đang thuê</option>
                        <option value="Bảo trì" ${room != null && room.status == 'Bảo trì' ? 'selected' : ''}>Bảo trì</option>
                    </select>
                    <div class="invalid-feedback">Vui lòng chọn trạng thái phòng</div>
                </div>

                <div class="col-12">
                    <label for="description" class="form-label form-label">Mô Tả Chi Tiết</label>
                    <textarea id="description" name="description" class="form-control" rows="4"
                              placeholder="Ví dụ: Phòng có điều hòa, nóng lạnh, WC riêng, ban công...">${room != null ? room.description : ''}</textarea>
                </div>
            </div>

            <!-- Buttons: equal width -->
            <div class="mt-4">
                <div class="btn-row">
                    <a href="${pageContext.request.contextPath}/quan-ly-phong" class="btn-action btn-back flex-eq">
                        <i class="fas fa-arrow-left"></i>
                        <span>Quay Lại Danh Sách</span>
                    </a>

                    <button type="submit" class="btn-action btn-submit flex-eq">
                        <i class="fas fa-save"></i>
                        <span>${room != null ? "Cập Nhật" : "Thêm Phòng"}</span>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    (function() {
        'use strict';

        const form = document.getElementById('roomForm');
        const priceInput = document.getElementById('price');
        const roomNumberInput = document.getElementById('roomNumber');

        // Format price input on blur: round to nearest 100,000
        if (priceInput) {
            priceInput.addEventListener('blur', function() {
                if (this.value) {
                    const v = parseFloat(String(this.value).replace(/,/g, ''));
                    if (!isNaN(v) && v >= 100000) {
                        this.value = Math.round(v / 100000) * 100000;
                    }
                }
            });
        }

        // Validate room number format: letters and numbers only
        if (roomNumberInput) {
            roomNumberInput.addEventListener('input', function() {
                const value = this.value.trim();
                const validPattern = /^[A-Z0-9]+$/i;
                if (value && !validPattern.test(value)) {
                    this.setCustomValidity('Số phòng chỉ được chứa chữ cái và số (ví dụ: A101, B202)');
                } else {
                    this.setCustomValidity('');
                }
            });
        }

        // Bootstrap style validation + add valid/invalid classes for custom styles
        form.addEventListener('submit', function(event) {
            // Trigger HTML5 validation
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            // Add classes
            const inputs = form.querySelectorAll('input, select, textarea, button');
            inputs.forEach(function(input) {
                if (input.checkValidity && input.checkValidity()) {
                    input.classList.remove('is-invalid');
                    input.classList.add('is-valid');
                } else {
                    input.classList.remove('is-valid');
                    // only mark invalid for fields that have validation constraints
                    if (input.willValidate === true) {
                        input.classList.add('is-invalid');
                    }
                }
            });
        }, false);

        // On blur validate individually
        const watchables = form.querySelectorAll('input, select, textarea');
        watchables.forEach(function(el) {
            el.addEventListener('blur', function() {
                if (this.checkValidity()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                    this.classList.add('is-invalid');
                }
            });
        });

    })();
</script>

</body>
</html>
