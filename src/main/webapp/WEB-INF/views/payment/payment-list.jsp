<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Thanh Toán - BOA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --accent-color: #27ae60;
            --success-color: #27ae60;
            --danger-color: #e74c3c;
        }

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

        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
            color: var(--primary-color);
        }

        .main-container {
            max-width: 1400px;
            margin: 30px auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .header-section {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            padding: 30px;
            color: white;
        }

        .header-section h2 {
            margin: 0;
            font-size: 2rem;
            font-weight: 600;
        }

        .header-section p {
            margin: 10px 0 0 0;
            opacity: 0.9;
        }

        .content-section {
            padding: 30px;
        }

        .action-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            flex-wrap: wrap;
            gap: 15px;
        }

        .btn-add {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            border: none;
            padding: 12px 30px;
            border-radius: 10px;
            color: white;
            font-weight: 600;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }

        .btn-add:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(17, 153, 142, 0.4);
            color: white;
        }

        .search-box {
            position: relative;
            max-width: 400px;
            flex: 1;
        }

        .search-box input {
            width: 100%;
            padding: 12px 20px 12px 45px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .search-box input:focus {
            border-color: #11998e;
            outline: none;
            box-shadow: 0 0 0 3px rgba(17, 153, 142, 0.1);
        }

        .search-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #999;
        }

        .table-container {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .table {
            margin: 0;
        }

        .table thead {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }

        .table thead th {
            border: none;
            padding: 18px 15px;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody tr {
            transition: all 0.3s;
            border-bottom: 1px solid #f0f0f0;
        }

        .table tbody tr:hover {
            background: linear-gradient(90deg, rgba(17, 153, 142, 0.05) 0%, rgba(56, 239, 125, 0.05) 100%);
            transform: scale(1.01);
        }

        .table tbody td {
            padding: 18px 15px;
            vertical-align: middle;
        }

        .badge-method {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .badge-cash {
            background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
            color: white;
        }

        .badge-transfer {
            background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
            color: white;
        }

        .amount-badge {
            background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
            color: white;
            padding: 8px 15px;
            border-radius: 10px;
            font-weight: bold;
            font-size: 1rem;
        }

        .btn-action {
            padding: 8px 15px;
            border-radius: 8px;
            border: none;
            font-size: 0.9rem;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            margin: 0 3px;
        }

        .btn-edit {
            background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
            color: white;
        }

        .btn-edit:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
            color: white;
        }

        .btn-delete {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            color: white;
        }

        .btn-delete:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(231, 76, 60, 0.4);
            color: white;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.3;
        }

        .alert {
            border-radius: 10px;
            border: none;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .action-bar {
                flex-direction: column;
                align-items: stretch;
            }

            .search-box {
                max-width: 100%;
            }

            .table-container {
                overflow-x: auto;
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
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/trang-chu">Bảng Điều Khiển</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/quan-ly-phong">Phòng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/quan-ly-khach-thue">Khách Thuê</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/quan-ly-hop-dong">Hợp Đồng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/quan-ly-thanh-toan">Thanh Toán</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle"></i> ${sessionScope.user.username}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth?action=logout">
                            <i class="fas fa-sign-out-alt"></i> Đăng Xuất
                        </a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<!-- Main Container -->
<div class="main-container">
    <!-- Header Section -->
    <div class="header-section">
        <h2><i class="fas fa-money-bill-wave"></i> Quản Lý Thanh Toán</h2>
        <p>Theo dõi công nợ, hóa đơn điện nước và thực hiện thanh toán.</p>
    </div>

    <!-- Content Section -->
    <div class="content-section">
        <!-- Alert Messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-warning alert-dismissible fade show">
                <i class="fas fa-exclamation-triangle"></i> ${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fas fa-times-circle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Action Bar -->
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/quan-ly-thanh-toan?action=showAddForm" class="btn-add">
                <i class="fas fa-plus-circle"></i> Thêm Thanh Toán Mới
            </a>
            <div class="search-box">
                <i class="fas fa-search"></i>
                <input type="text" id="searchInput" placeholder="Tìm kiếm thanh toán..." class="form-control">
            </div>
        </div>

        <!-- Table -->
        <div class="table-container">
            <c:choose>
                <c:when test="${empty payments}">
                    <div class="empty-state">
                        <i class="fas fa-receipt"></i>
                        <h4>Chưa có thanh toán nào</h4>
                        <p>Bắt đầu bằng cách thêm giao dịch thanh toán đầu tiên.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover" id="paymentTable">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Mã HĐ</th>
                                <th>Số Tiền</th>
                                <th>Ngày Thanh Toán</th>
                                <th>Phương Thức</th>
                                <th>Mô Tả</th>
                                <th>Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="payment" items="${payments}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td><strong>#${payment.contractId}</strong></td>
                                    <td>
                                        <span class="amount-badge">
                                            <fmt:formatNumber value="${payment.amount}" type="number" groupingUsed="true"/> VNĐ
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <span class="badge-method ${payment.method == 'Tiền mặt' ? 'badge-cash' : 'badge-transfer'}">
                                            <i class="fas fa-${payment.method == 'Tiền mặt' ? 'money-bill' : 'credit-card'}"></i>
                                            ${payment.method}
                                        </span>
                                    </td>
                                    <td>${payment.description}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/quan-ly-thanh-toan?action=showEditForm&id=${payment.paymentId}"
                                           class="btn-action btn-edit">
                                            <i class="fas fa-edit"></i> Sửa
                                        </a>
                                        <a href="#"
                                           onclick="confirmDelete(${payment.paymentId})"
                                           class="btn-action btn-delete">
                                            <i class="fas fa-trash-alt"></i> Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Search Functionality
    document.getElementById('searchInput').addEventListener('keyup', function() {
        const searchValue = this.value.toLowerCase();
        const tableRows = document.querySelectorAll('#paymentTable tbody tr');

        tableRows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(searchValue) ? '' : 'none';
        });
    });

    // Confirm Delete
    function confirmDelete(paymentId) {
        if (confirm('Bạn có chắc chắn muốn xóa thanh toán này?')) {
            window.location.href = '${pageContext.request.contextPath}/quan-ly-thanh-toan?action=delete&id=' + paymentId;
        }
    }
</script>

</body>
</html>