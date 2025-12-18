<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống Kê Doanh Thu - BOA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            min-height: 100vh;
            padding: 20px 0;
        }

        .navbar {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }

        .main-container {
            max-width: 1400px;
            margin: 30px auto;
        }

        .header-section {
            background: white;
            border-radius: 20px 20px 0 0;
            padding: 30px;
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }

        .stats-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: all 0.3s;
            margin-bottom: 20px;
        }

        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .stats-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.8rem;
            margin-bottom: 15px;
        }

        .icon-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .icon-success {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }

        .icon-warning {
            background: linear-gradient(135deg, #f2994a 0%, #f2c94c 100%);
            color: white;
        }

        .icon-danger {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }

        .stats-value {
            font-size: 2rem;
            font-weight: bold;
            color: #2c3e50;
            margin: 10px 0;
        }

        .stats-label {
            color: #7f8c8d;
            font-size: 0.9rem;
        }

        .chart-container {
            background: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .filter-section {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .btn-filter {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
            border: none;
            padding: 10px 25px;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-filter:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 107, 107, 0.4);
            color: white;
        }

        .table-container {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .table thead {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }

        .table thead th {
            border: none;
            padding: 15px;
            font-weight: 600;
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/quan-ly-hop-dong">Hợp Đồng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/quan-ly-thanh-toan">Thanh Toán</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/thong-ke">Thống Kê</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
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
    <!-- Header -->
    <div class="header-section">
        <h2 style="margin: 0; font-size: 2rem; font-weight: 600;">
            <i class="fas fa-chart-line"></i> Thống Kê Doanh Thu
        </h2>
        <p style="margin: 10px 0 0 0; opacity: 0.9;">Theo dõi doanh thu và hiệu quả kinh doanh</p>
    </div>

    <!-- ✅ KIỂM TRA DỮ LIỆU -->
    <c:if test="${empty overview}">
        <div class="alert alert-warning m-3">
            <i class="fas fa-exclamation-triangle"></i> Không có dữ liệu thống kê. Vui lòng kiểm tra lại kết nối database.
        </div>
    </c:if>

    <!-- Overview Cards -->
    <div style="background: white; padding: 30px; border-radius: 0 0 20px 20px; margin-bottom: 20px; box-shadow: 0 5px 15px rgba(0,0,0,0.1);">
        <div class="row">
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-primary">
                        <i class="fas fa-door-open"></i>
                    </div>
                    <div class="stats-value">${overview.totalRooms != null ? overview.totalRooms : 0}</div>
                    <div class="stats-label">Tổng Số Phòng</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-success">
                        <i class="fas fa-home"></i>
                    </div>
                    <div class="stats-value">${overview.occupiedRooms != null ? overview.occupiedRooms : 0}</div>
                    <div class="stats-label">Phòng Đang Thuê</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-warning">
                        <i class="fas fa-door-closed"></i>
                    </div>
                    <div class="stats-value">${overview.vacantRooms != null ? overview.vacantRooms : 0}</div>
                    <div class="stats-label">Phòng Trống</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stats-card">
                    <div class="stats-icon icon-danger">
                        <i class="fas fa-file-contract"></i>
                    </div>
                    <div class="stats-value">${overview.activeContracts != null ? overview.activeContracts : 0}</div>
                    <div class="stats-label">Hợp Đồng Hoạt Động</div>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-md-6">
                <div class="stats-card" style="background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%); color: white;">
                    <h5 style="margin: 0 0 10px 0;">Tổng Doanh Thu</h5>
                    <div class="stats-value" style="color: white; font-size: 2.5rem;">
                        <fmt:formatNumber value="${overview.totalRevenue != null ? overview.totalRevenue : 0}" type="number" groupingUsed="true"/> VNĐ
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="stats-card" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white;">
                    <h5 style="margin: 0 0 10px 0;">Doanh Thu Tháng Này</h5>
                    <div class="stats-value" style="color: white; font-size: 2.5rem;">
                        <fmt:formatNumber value="${overview.currentMonthRevenue != null ? overview.currentMonthRevenue : 0}" type="number" groupingUsed="true"/> VNĐ
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filter Section -->
    <div class="filter-section">
        <form method="get" action="${pageContext.request.contextPath}/thong-ke" class="row g-3 align-items-end">
            <div class="col-md-3">
                <label class="form-label fw-bold">Chọn Năm</label>
                <select name="year" class="form-select">
                    <c:forEach var="y" begin="2020" end="2030">
                        <option value="${y}" ${y == selectedYear ? 'selected' : ''}>${y}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-bold">Chọn Tháng (Tùy chọn)</label>
                <select name="month" class="form-select">
                    <option value="">Tất cả các tháng</option>
                    <c:forEach var="m" begin="1" end="12">
                        <option value="${m}" ${m == selectedMonth ? 'selected' : ''}>Tháng ${m}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-filter">
                    <i class="fas fa-filter"></i> Lọc Thống Kê
                </button>
            </div>
        </form>
    </div>

    <!-- Revenue Chart -->
    <c:if test="${not empty monthlyRevenue}">
        <div class="chart-container">
            <h4 class="mb-4">
                <i class="fas fa-chart-bar"></i>
                ${viewType == 'daily' ? 'Doanh Thu Theo Ngày - Tháng ' += selectedMonth += '/' += selectedYear : 'Doanh Thu Theo Tháng - Năm ' += selectedYear}
            </h4>
            <canvas id="revenueChart" height="80"></canvas>
        </div>
    </c:if>

    <!-- Room Revenue Chart -->
    <c:if test="${not empty roomRevenue}">
        <div class="chart-container">
            <h4 class="mb-4"><i class="fas fa-chart-pie"></i> Doanh Thu Theo Phòng (Top 10)</h4>
            <canvas id="roomRevenueChart" height="80"></canvas>
        </div>
    </c:if>

    <!-- Recent Payments -->
    <c:if test="${not empty recentPayments}">
        <div class="table-container">
            <h4 class="mb-4"><i class="fas fa-history"></i> Thanh Toán Gần Đây</h4>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Mã HĐ</th>
                        <th>Số Tiền</th>
                        <th>Ngày</th>
                        <th>Phương Thức</th>
                        <th>Mô Tả</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="payment" items="${recentPayments}">
                        <tr>
                            <td><strong>#${payment.contractId}</strong></td>
                            <td>
                                <span style="color: #27ae60; font-weight: bold;">
                                    <fmt:formatNumber value="${payment.amount}" type="number" groupingUsed="true"/> VNĐ
                                </span>
                            </td>
                            <td><fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${payment.method}</td>
                            <td>${payment.description}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // ✅ KIỂM TRA DỮ LIỆU TRƯỚC KHI VẼ CHART
    <c:if test="${not empty monthlyRevenue}">
    // Revenue Chart Data
    const revenueLabels = [
        <c:forEach var="entry" items="${monthlyRevenue}" varStatus="status">
            '${viewType == "daily" ? entry.key : "Tháng " += entry.key}'${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const revenueData = [
        <c:forEach var="entry" items="${monthlyRevenue}" varStatus="status">
            ${entry.value}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    // Create Revenue Chart
    const ctxRevenue = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctxRevenue, {
        type: 'line',
        data: {
            labels: revenueLabels,
            datasets: [{
                label: 'Doanh Thu (VNĐ)',
                data: revenueData,
                borderColor: 'rgb(255, 107, 107)',
                backgroundColor: 'rgba(255, 107, 107, 0.1)',
                borderWidth: 3,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return 'Doanh thu: ' + context.parsed.y.toLocaleString('vi-VN') + ' VNĐ';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString('vi-VN') + ' VNĐ';
                        }
                    }
                }
            }
        }
    });
    </c:if>

    // Room Revenue Chart
    <c:if test="${not empty roomRevenue}">
    const roomLabels = [
        <c:forEach var="entry" items="${roomRevenue}" varStatus="status">
            'Phòng ${entry.key}'${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const roomData = [
        <c:forEach var="entry" items="${roomRevenue}" varStatus="status">
            ${entry.value}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const ctxRoom = document.getElementById('roomRevenueChart').getContext('2d');
    new Chart(ctxRoom, {
        type: 'bar',
        data: {
            labels: roomLabels,
            datasets: [{
                label: 'Doanh Thu (VNĐ)',
                data: roomData,
                backgroundColor: [
                    'rgba(255, 107, 107, 0.8)',
                    'rgba(102, 126, 234, 0.8)',
                    'rgba(17, 153, 142, 0.8)',
                    'rgba(242, 153, 74, 0.8)',
                    'rgba(240, 147, 251, 0.8)',
                    'rgba(79, 172, 254, 0.8)',
                    'rgba(56, 239, 125, 0.8)',
                    'rgba(242, 201, 76, 0.8)',
                    'rgba(231, 76, 60, 0.8)',
                    'rgba(155, 89, 182, 0.8)'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return 'Doanh thu: ' + context.parsed.y.toLocaleString('vi-VN') + ' VNĐ';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString('vi-VN') + ' VNĐ';
                        }
                    }
                }
            }
        }
    });
    </c:if>
</script>

</body>
</html>