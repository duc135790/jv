<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${tenant != null ? "Chỉnh Sửa" : "Thêm"} Khách Thuê - BOA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            max-width: 700px;
            margin: 40px auto;
            border: none;
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-align: center;
            padding: 25px;
            font-size: 1.5rem;
            font-weight: 600;
        }
        .card-body {
            padding: 40px;
        }
        .form-label {
            font-weight: 600;
            color: #333;
        }
        .form-control, .form-control:focus {
            border: 2px solid #ddd;
            border-radius: 12px;
            padding: 12px 16px;
            box-shadow: none;
        }
        .form-control.is-valid {
            border-color: #28a745;
            background-image: none;
        }
        .form-control.is-invalid {
            border-color: #dc3545;
        }
        .form-text {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .invalid-feedback {
            color: #dc3545;
            font-size: 0.9rem;
            margin-top: 5px;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 12px;
            padding: 12px;
            font-weight: 600;
        }
        .btn-secondary {
            border-radius: 12px;
            padding: 12px;
        }
        .required { color: #e74c3c; }
    </style>
</head>
<body>

<div class="card">
    <div class="card-header">
        <i class="fas fa-user-${tenant != null ? 'edit' : 'plus'} me-2"></i>
        ${tenant != null ? 'Chỉnh Sửa' : 'Thêm'} Khách Thuê
    </div>

    <div class="card-body">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/quan-ly-khach-thue" id="tenantForm" novalidate>
            <input type="hidden" name="action" value="${tenant != null ? 'update' : 'add'}">
            <c:if test="${tenant != null}">
                <input type="hidden" name="tenantId" value="${tenant.tenantId}">
            </c:if>

            <!-- Họ và Tên -->
            <div class="mb-4">
                <label class="form-label">Họ và Tên <span class="required">*</span></label>
                <input type="text" class="form-control" id="name" name="name"
                       value="${tenant != null ? tenant.name : ''}"
                       placeholder="Ví dụ: Nguyễn Văn A" maxlength="100" required>
                <div class="form-text">Ví dụ: Nguyễn Văn A</div>
                <div class="invalid-feedback">Họ tên phải từ 3-100 ký tự và chỉ chứa chữ cái, khoảng trắng</div>
            </div>

            <!-- CMND/CCCD -->
            <div class="mb-4">
                <label class="form-label">CMND/CCCD <span class="required">*</span></label>
                <input type="text" class="form-control" id="idCard" name="idCard"
                       value="${tenant != null ? tenant.idCard : ''}"
                       placeholder="Ví dụ: 001234567890" required>
                <div class="form-text">Ví dụ: 001234567890</div>
                <div class="invalid-feedback">CMND phải có 9 số hoặc CCCD phải có 12 số</div>
            </div>

            <!-- Số điện thoại -->
            <div class="mb-4">
                <label class="form-label">Số Điện Thoại <span class="required">*</span></label>
                <input type="text" class="form-control" id="phone" name="phone"
                       value="${tenant != null ? tenant.phone : ''}"
                       placeholder="Ví dụ: 0912345678" required>
                <div class="form-text">Ví dụ: 0912345678</div>
                <div class="invalid-feedback">Số điện thoại phải có 10-11 số và bắt đầu bằng 0</div>
            </div>

            <!-- Email -->
            <div class="mb-4">
                <label class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email"
                       value="${tenant != null ? tenant.email : ''}"
                       placeholder="Ví dụ: nguyenvana@email.com">
                <div class="form-text">Ví dụ: nguyenvana@email.com</div>
                <div class="invalid-feedback">Email không hợp lệ</div>
            </div>

            <!-- Địa chỉ -->
            <div class="mb-4">
                <label class="form-label">Địa Chỉ Thường Trú <span class="required">*</span></label>
                <textarea class="form-control" id="address" name="address" rows="3"
                          placeholder="Ví dụ: 123 Đường ABC, Phường XYZ, Quận 1, TP.HCM"
                          maxlength="255" required>${tenant != null ? tenant.address : ''}</textarea>
                <div class="form-text">Ví dụ: 123 Đường ABC, Phường XYZ, Quận 1, TP.HCM</div>
                <div class="invalid-feedback">Địa chỉ phải từ 10-255 ký tự</div>
            </div>

            <div class="d-flex gap-3 justify-content-end">
                <a href="${pageContext.request.contextPath}/quan-ly-khach-thue" class="btn btn-secondary px-5">
                    Quay Lại
                </a>
                <button type="submit" class="btn btn-primary px-5">
                    ${tenant != null ? 'Cập Nhật' : 'Thêm Khách Thuê'}
                </button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        'use strict';
        const form = document.getElementById('tenantForm');

        const valid = el => { el.classList.remove('is-invalid'); el.classList.add('is-valid'); };
        const invalid = el => { el.classList.remove('is-valid'); el.classList.add('is-invalid'); };

        // Họ tên
        const name = document.getElementById('name');
        name.addEventListener('input', function () {
            const v = this.value.trim();
            const regex = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪỬỮỰỲỴÝỶỸ\s]+$/;
            if (v === '') {
                this.setCustomValidity(''); this.classList.remove('is-valid','is-invalid');
            } else if (v.length < 3 || v.length > 100 || !regex.test(v)) {
                this.setCustomValidity('error');
                invalid(this);
            } else {
                this.setCustomValidity('');
                valid(this);
            }
        });

        // CMND/CCCD
        const idCard = document.getElementById('idCard');
        idCard.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g,'');
            const len = this.value.length;
            if (len === 0) { this.setCustomValidity(''); this.classList.remove('is-valid','is-invalid'); }
            else if (len === 9 || len === 12) { this.setCustomValidity(''); valid(this); }
            else { this.setCustomValidity('error'); invalid(this); }
        });

        // Phone
        const phone = document.getElementById('phone');
        phone.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g,'');
            const v = this.value;
            if (v === '') { this.setCustomValidity(''); this.classList.remove('is-valid','is-invalid'); }
            else if (v.startsWith('0') && v.length >=10 && v.length <=11) { this.setCustomValidity(''); valid(this); }
            else { this.setCustomValidity('error'); invalid(this); }
        });

        // Email
        const email = document.getElementById('email');
        email.addEventListener('input', function () {
            if (this.value === '') { this.classList.remove('is-valid','is-invalid'); }
            else { this.checkValidity() ? valid(this) : invalid(this); }
        });

        // Address
        const address = document.getElementById('address');
        address.addEventListener('input', function () {
            const len = this.value.length;
            if (len === 0) { this.classList.remove('is-valid','is-invalid'); }
            else if (len >=10 && len <=255) { this.setCustomValidity(''); valid(this); }
            else { invalid(this); }
        });

        // Submit
        form.addEventListener('submit', e => {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.querySelectorAll('input, textarea').forEach(f => {
                if (f.value.trim() !== '') f.checkValidity() ? valid(f) : invalid(f);
            });
        });

        // Blur
        form.querySelectorAll('input, textarea').forEach(f => {
            f.addEventListener('blur', () => {
                if (f.value.trim() !== '') f.checkValidity() ? valid(f) : invalid(f);
            });
        });
    })();
</script>

</body>
</html>