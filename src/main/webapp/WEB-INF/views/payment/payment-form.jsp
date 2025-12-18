<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${payment != null ? "Chỉnh Sửa" : "Thêm"} Thanh Toán - BOA</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            min-height: 100vh;
            padding: 20px 0;
        }

        .navbar {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }

        .form-container {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .form-header {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            padding: 30px;
            color: white;
            text-align: center;
        }

        .form-header h2 {
            margin: 0;
            font-size: 2rem;
            font-weight: 600;
        }

        .form-body {
            padding: 40px;
        }

        .form-label {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 8px;
        }

        .form-control, .form-select {
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .form-control:focus, .form-select:focus {
            border-color: #11998e;
            box-shadow: 0 0 0 3px rgba(17, 153, 142, 0.1);
        }

        .input-group-text {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
            border: none;
            font-weight: 600;
            border-radius: 10px 0 0 10px;
        }

        /* Buttons: make equal width and no underlines for anchor */
        .btn-action {
            padding: 12px 0;            /* equal vertical padding */
            border-radius: 10px;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            text-decoration: none;     /* remove underline */
        }

        .btn-back {
            background: white;
            color: #11998e;
            border: 2px solid #11998e;
        }
        .btn-back:hover,
        .btn-back:focus,
        .btn-back:active {
            text-decoration: none !important; /* ensure no underline */
            background: #f8f9fa;
            color: #11998e;
            outline: none;
        }

        .btn-submit {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
            border: none;
        }
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(17, 153, 142, 0.4);
        }

        .alert {
            border-radius: 10px;
            border: none;
        }

        .required {
            color: #e74c3c;
        }

        .info-box {
            background: #f8f9fa;
            border-left: 4px solid #11998e;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .info-box i {
            color: #11998e;
            margin-right: 10px;
        }

        /* layout for equal-width buttons */
        .btn-row {
            display: flex;
            gap: 16px;
            width: 100%;
            align-items: stretch;
        }
        .btn-row .flex-eq {
            flex: 1 1 0;
            min-width: 0; /* prevents overflow from long text */
        }

        /* On very small screens stack vertically */
        @media (max-width: 420px) {
            .btn-row {
                flex-direction: column;
            }
        }

        /* ========== Validation feedback: hide by default ========== */
        .invalid-feedback {
            color: #e74c3c;
            font-size: 0.875rem;
            margin-top: 5px;
            display: none; /* HIDE mặc định */
        }

        /* Hiện feedback chỉ khi control có class is-invalid */
        .form-control.is-invalid + .invalid-feedback,
        .form-select.is-invalid + .invalid-feedback,
        textarea.is-invalid + .invalid-feedback {
            display: block;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/trang-chu">
            <i class="fas fa-home"></i> BOA
        </a>
    </div>
</nav>

<div class="form-container">
    <div class="form-header">
        <h2>
            <i class="fas fa-${payment != null ? "edit" : "plus-circle"}"></i>
            ${payment != null ? "Chỉnh Sửa" : "Thêm"} Thanh Toán
        </h2>
    </div>

    <div class="form-body">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            </div>
        </c:if>

        <div class="info-box">
            <i class="fas fa-info-circle"></i>
            <strong>Lưu ý:</strong> Vui lòng kiểm tra kỹ thông tin trước khi lưu. Thanh toán chỉ có thể thực hiện cho các hợp đồng đang hoạt động.
        </div>

        <form method="post" action="${pageContext.request.contextPath}/quan-ly-thanh-toan" id="paymentForm" novalidate>
            <input type="hidden" name="action" value="${payment != null ? 'update' : 'add'}">
            <c:if test="${payment != null}">
                <input type="hidden" name="paymentId" value="${payment.paymentId}">
            </c:if>

            <!-- Mã Hợp Đồng -->
            <div class="mb-3">
                <label for="contractId" class="form-label">
                    Mã Hợp Đồng <span class="required">*</span>
                </label>
                <select class="form-select" id="contractId" name="contractId" required>
                    <option value="">-- Chọn Hợp Đồng --</option>
                    <c:forEach var="cid" items="${contractIds}">
                        <option value="${cid}" ${payment != null && payment.contractId == cid ? 'selected' : ''}>
                            Hợp Đồng #${cid}
                        </option>
                    </c:forEach>
                </select>
                <small class="text-muted">Chỉ hiển thị các hợp đồng đang hoạt động</small>
                <div class="invalid-feedback">Vui lòng chọn hợp đồng</div>
            </div>

            <!-- Số Tiền -->
            <div class="mb-3">
                <label for="amount" class="form-label">
                    Số Tiền (VNĐ) <span class="required">*</span>
                </label>
                <div class="input-group">
                    <span class="input-group-text">
                        <i class="fas fa-dong-sign"></i>
                    </span>
                    <input type="number"
                           class="form-control"
                           id="amount"
                           name="amount"
                           value="${payment != null ? payment.amount : ''}"
                           placeholder="Ví dụ: 5000000"
                           min="1000"
                           step="1000"
                           required>
                </div>
                <div class="invalid-feedback">Vui lòng nhập số tiền hợp lệ (>= 1,000)</div>
            </div>

            <!-- Ngày Thanh Toán -->
            <div class="mb-3">
                <label for="paymentDate" class="form-label">
                    Ngày Thanh Toán <span class="required">*</span>
                </label>
                <input type="date"
                       class="form-control"
                       id="paymentDate"
                       name="paymentDate"
                       value="${payment != null ? payment.paymentDate : ''}"
                       required>
                <div class="invalid-feedback">Vui lòng chọn ngày thanh toán</div>
            </div>

            <!-- Phương Thức Thanh Toán -->
            <div class="mb-3">
                <label for="method" class="form-label">
                    Phương Thức Thanh Toán <span class="required">*</span>
                </label>
                <select class="form-select" id="method" name="method" required>
                    <option value="">-- Chọn Phương Thức --</option>
                    <option value="Tiền mặt" ${payment != null && payment.method == 'Tiền mặt' ? 'selected' : ''}>
                        Tiền mặt
                    </option>
                    <option value="Chuyển khoản" ${payment != null && payment.method == 'Chuyển khoản' ? 'selected' : ''}>
                        Chuyển khoản
                    </option>
                </select>
                <div class="invalid-feedback">Vui lòng chọn phương thức thanh toán</div>
            </div>

            <!-- Mô Tả -->
            <div class="mb-3">
                <label for="description" class="form-label">Mô Tả / Ghi Chú</label>
                <textarea class="form-control"
                          id="description"
                          name="description"
                          rows="3"
                          placeholder="Ví dụ: Thanh toán tiền thuê tháng 12/2024">${payment != null ? payment.description : ''}</textarea>
            </div>

            <!-- Buttons: equal width -->
            <div class="mt-4">
                <div class="btn-row">
                    <a href="${pageContext.request.contextPath}/quan-ly-thanh-toan" class="btn-action btn-back flex-eq" role="button">
                        <i class="fas fa-arrow-left"></i>
                        <span>Quay Lại Danh Sách</span>
                    </a>

                    <button type="submit" class="btn-action btn-submit flex-eq">
                        <i class="fas fa-save"></i>
                        <span>${payment != null ? 'Cập Nhật' : 'Thêm Thanh Toán'}</span>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function() {
        // Set default date to today if adding new payment (server-side c:if outputs JS when needed)
        <c:if test="${payment == null}">
            try {
                const el = document.getElementById('paymentDate');
                if (el) el.valueAsDate = new Date();
            } catch(e) { /* ignore */ }
        </c:if>

        // Helper: validate one control and set classes
        function validateControl(el) {
            if (!el || typeof el.checkValidity !== 'function') return true;
            const ok = el.checkValidity();
            if (ok) {
                el.classList.remove('is-invalid');
                el.classList.add('is-valid');
            } else {
                el.classList.remove('is-valid');
                el.classList.add('is-invalid');
            }
            return ok;
        }

        const form = document.getElementById('paymentForm');
        if (!form) return;

        // Attach blur/input listeners to inputs/selects/textarea
        const controls = form.querySelectorAll('input, select, textarea');
        controls.forEach(function(el) {
            // On blur: validate this field (show error only after user left the field)
            el.addEventListener('blur', function() {
                validateControl(el);
            });

            // On input: if currently invalid and becomes valid, remove invalid
            el.addEventListener('input', function() {
                if (el.classList.contains('is-invalid') && el.checkValidity()) {
                    el.classList.remove('is-invalid');
                    el.classList.add('is-valid');
                }
            });
        });

        // Format amount input on blur (safe guard if element exists)
        const amountInput = document.getElementById('amount');
        if (amountInput) {
            amountInput.addEventListener('blur', function() {
                if (this.value) {
                    const value = parseFloat(String(this.value).replace(/,/g, ''));
                    if (!isNaN(value)) {
                        this.value = Math.round(value);
                    }
                }
            });
        }

        // On submit: validate all, show errors only after attempted submit
        form.addEventListener('submit', function(e) {
            let formValid = true;
            controls.forEach(function(el) {
                if (typeof el.checkValidity === 'function') {
                    const ok = validateControl(el);
                    if (!ok) formValid = false;
                }
            });

            if (!formValid) {
                e.preventDefault();
                e.stopPropagation();
                // Scroll to first invalid field for better UX
                const firstInvalid = form.querySelector('.is-invalid');
                if (firstInvalid && typeof firstInvalid.scrollIntoView === 'function') {
                    firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    try { firstInvalid.focus({ preventScroll: true }); } catch(_) {}
                }
            }
        }, false);
    })();
</script>

</body>
</html>
