<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${contract != null ? "Chỉnh Sửa" : "Tạo"} Hợp Đồng - BOA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            min-height: 100vh;
            padding: 20px 0;
        }

        .navbar {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }

        .form-container {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .form-header {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
            border-color: #4facfe;
            box-shadow: 0 0 0 3px rgba(79, 172, 254, 0.1);
        }

        .input-group-text {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            border: none;
            font-weight: 600;
            border-radius: 10px 0 0 10px;
        }

        /* Buttons: unified style so both have equal width */
        .btn-action {
            padding: 12px 0;            /* vertical padding only to keep widths equal */
            border-radius: 10px;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            text-decoration: none;     /* remove underline on anchor */
        }

        .btn-back {
            background: white;
            color: #4facfe;
            border: 2px solid #4facfe;
        }
        .btn-back:hover,
        .btn-back:focus,
        .btn-back:active {
            text-decoration: none !important; /* ensure no underline */
            background: #f8f9fa;
            color: #4facfe;
            outline: none;
        }

        .btn-submit {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            border: none;
        }
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(79, 172, 254, 0.4);
        }

        .alert {
            border-radius: 10px;
            border: none;
        }

        .required {
            color: #e74c3c;
        }

        .info-box {
            background: #e8f8ff;
            border-left: 4px solid #4facfe;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .info-box i {
            color: #4facfe;
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
            min-width: 0; /* prevents overflow when text is long */
        }

        /* On very small screens stack vertically to avoid squishing */
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
    <!-- Form Header -->
    <div class="form-header">
        <h2>
            <i class="fas fa-${contract != null ? 'edit' : 'plus-circle'}"></i>
            ${contract != null ? 'Chỉnh Sửa' : 'Tạo'} Hợp Đồng
        </h2>
    </div>

    <!-- Form Body -->
    <div class="form-body">
        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
            </div>
        </c:if>

        <!-- Info Box -->
        <div class="info-box">
            <i class="fas fa-info-circle"></i>
            <strong>Lưu ý:</strong> Chỉ có thể chọn khách thuê và phòng trống. Sau khi tạo hợp đồng, trạng thái phòng sẽ tự động chuyển sang "Đang thuê".
        </div>

        <!-- Form -->
        <form method="post" action="${pageContext.request.contextPath}/quan-ly-hop-dong" id="contractForm" novalidate>
            <!-- Hidden Fields -->
            <input type="hidden" name="action" value="${contract != null ? 'update' : 'add'}">
            <c:if test="${contract != null}">
                <input type="hidden" name="contractId" value="${contract.contractId}">
            </c:if>

            <div class="row g-3">
                <!-- Khách Thuê -->
                <div class="col-md-6">
                    <label for="tenantId" class="form-label">
                        Khách Thuê <span class="required">*</span>
                    </label>
                    <select class="form-select" id="tenantId" name="tenantId" required>
                        <option value="">-- Chọn Khách Thuê --</option>
                        <c:forEach var="tenant" items="${availableTenants}">
                            <option value="${tenant.tenantId}"
                                    ${contract != null && contract.tenantId == tenant.tenantId ? 'selected' : ''}>
                                ${tenant.name} - ${tenant.idCard}
                            </option>
                        </c:forEach>
                    </select>
                    <small class="text-muted">Chọn khách thuê từ danh sách</small>
                </div>

                <!-- Phòng -->
                <div class="col-md-6">
                    <label for="roomId" class="form-label">
                        Phòng <span class="required">*</span>
                    </label>
                    <select class="form-select" id="roomId" name="roomId" required>
                        <option value="">-- Chọn Phòng --</option>
                        <c:forEach var="room" items="${availableRooms}">
                            <option value="${room.roomId}"
                                    ${contract != null && contract.roomId == room.roomId ? 'selected' : ''}>
                                ${room.roomNumber} - <fmt:formatNumber value="${room.price}" type="number"/> VNĐ/tháng
                            </option>
                        </c:forEach>
                    </select>
                    <small class="text-muted">Chỉ hiển thị phòng trống</small>
                </div>

                <!-- Ngày Bắt Đầu -->
                <div class="col-md-6">
                    <label for="startDate" class="form-label">
                        Ngày Bắt Đầu <span class="required">*</span>
                    </label>
                    <input type="date"
                           class="form-control"
                           id="startDate"
                           name="startDate"
                           value="${contract != null ? contract.startDate : ''}"
                           required>
                </div>

                <!-- Ngày Kết Thúc -->
                <div class="col-md-6">
                    <label for="endDate" class="form-label">
                        Ngày Kết Thúc Dự Kiến <span class="required">*</span>
                    </label>
                    <input type="date"
                           class="form-control"
                           id="endDate"
                           name="endDate"
                           value="${contract != null ? contract.endDate : ''}"
                           required>
                </div>

                <!-- Số Tiền Đặt Cọc -->
                <div class="col-12">
                    <label for="depositAmount" class="form-label">
                        Số Tiền Đặt Cọc (VNĐ) <span class="required">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-dong-sign"></i>
                        </span>
                        <input type="number"
                               class="form-control"
                               id="depositAmount"
                               name="depositAmount"
                               value="${contract != null ? contract.depositAmount : ''}"
                               placeholder="Ví dụ: 3000000"
                               min="0"
                               step="100000"
                               required>
                    </div>
                    <small class="text-muted">Thường bằng 1-2 tháng tiền thuê</small>
                </div>

                <!-- Trạng Thái (chỉ khi chỉnh sửa) -->
                <c:if test="${contract != null}">
                    <div class="col-12">
                        <label for="status" class="form-label">
                            Trạng Thái <span class="required">*</span>
                        </label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="Đang thuê" ${contract.status == 'Đang thuê' ? 'selected' : ''}>Đang thuê</option>
                            <option value="Đã kết thúc" ${contract.status == 'Đã kết thúc' ? 'selected' : ''}>Đã kết thúc</option>
                        </select>
                    </div>
                </c:if>
            </div>

            <!-- Buttons: equal width -->
            <div class="mt-4">
                <div class="btn-row">
                    <a href="${pageContext.request.contextPath}/quan-ly-hop-dong" class="btn-action btn-back flex-eq" role="button">
                        <i class="fas fa-arrow-left"></i>
                        <span>Quay Lại Danh Sách</span>
                    </a>

                    <button type="submit" class="btn-action btn-submit flex-eq">
                        <i class="fas fa-save"></i>
                        <span>${contract != null ? 'Cập Nhật' : 'Tạo Hợp Đồng'}</span>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function() {
        // Set default dates only if creating new contract; guard element access
        <c:if test="${contract == null}">
            try {
                const startEl = document.getElementById('startDate');
                const endEl = document.getElementById('endDate');
                if (startEl) startEl.valueAsDate = new Date();

                if (endEl) {
                    const endDate = new Date();
                    endDate.setFullYear(endDate.getFullYear() + 1);
                    endEl.valueAsDate = endDate;
                }
            } catch(e) { /* ignore if DOM not ready or elements absent */ }
        </c:if>

        // Validate end date > start date safely (guard for missing elements)
        const startInput = document.getElementById('startDate');
        const endInput = document.getElementById('endDate');
        if (startInput && endInput) {
            startInput.addEventListener('change', function() {
                const s = new Date(this.value);
                const e = new Date(endInput.value || 0);
                if (endInput.value && e <= s) {
                    alert('Ngày kết thúc phải sau ngày bắt đầu!');
                    endInput.value = '';
                }
            });

            endInput.addEventListener('change', function() {
                const e = new Date(this.value);
                const s = new Date(startInput.value || 0);
                if (startInput.value && e <= s) {
                    alert('Ngày kết thúc phải sau ngày bắt đầu!');
                    this.value = '';
                }
            });
        }

        // Simple client-side validation feedback (optional)
        const form = document.getElementById('contractForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
                const controls = form.querySelectorAll('input, select, textarea');
                controls.forEach(function(c) {
                    if (c.checkValidity && c.checkValidity()) {
                        c.classList.remove('is-invalid');
                        c.classList.add('is-valid');
                    } else {
                        c.classList.remove('is-valid');
                        if (c.willValidate === true) c.classList.add('is-invalid');
                    }
                });
            }, false);
        }
    })();
</script>

</body>
</html>
