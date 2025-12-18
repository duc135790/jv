<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Phòng - BOA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .header-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 30px;
            color: white;
        }

        .header-section h2 {
            margin: 0;
            font-size: 2rem;
            font-weight: 600;
        }

        .action-toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 30px;
            background: #f8f9fa;
            border-bottom: 2px solid #e0e0e0;
            flex-wrap: wrap;
            gap: 15px;
        }

        .btn-add-new {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 25px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }

        .btn-add-new:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            color: white;
        }

        .search-box {
            position: relative;
            max-width: 350px;
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
            border-color: #667eea;
            outline: none;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .search-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #999;
        }

        .table-container {
            padding: 30px;
        }

        .table thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .table thead th {
            border: none;
            padding: 15px;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
        }

        .table tbody tr {
            transition: all 0.3s;
            border-bottom: 1px solid #f0f0f0;
        }

        .table tbody tr:hover {
            background: linear-gradient(90deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
            transform: scale(1.01);
        }

        .table tbody td {
            padding: 15px;
            vertical-align: middle;
        }

        .badge-status {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .badge-vacant {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }

        .badge-occupied {
            background: linear-gradient(135deg, #f2994a 0%, #f2c94c 100%);
            color: white;
        }

        .badge-maintenance {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            color: white;
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

        .price-tag {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 5px 12px;
            border-radius: 8px;
            font-weight: bold;
            font-size: 0.95rem;
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/quan-ly-phong">Phòng</a>
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
        <h2><i class="fas fa-building"></i> Quản Lý Phòng Trọ</h2>
        <p>Quản lý, thêm hoặc chỉnh sửa các phòng của bạn.</p>
    </div>

    <!-- Action Toolbar -->
    <div class="action-toolbar">
        <a href="${pageContext.request.contextPath}/quan-ly-phong?action=showAddForm" class="btn-add-new">
            <i class="fas fa-plus-circle"></i> Thêm Phòng Mới
        </a>
        <div class="search-box">
            <i class="fas fa-search"></i>
            <input type="text" id="searchInput" placeholder="Tìm kiếm phòng..." class="form-control">
        </div>
    </div>

    <!-- Content Section -->
    <div class="table-container">
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

        <!-- Table -->
        <c:choose>
            <c:when test="${empty rooms}">
                <div class="empty-state">
                    <i class="fas fa-door-closed"></i>
                    <h4>Chưa có phòng nào</h4>
                    <p>Hãy nhấn <strong>"Thêm Phòng Mới"</strong> để bắt đầu quản lý.</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="table table-hover" id="roomTable">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Số Phòng</th>
                            <th>Loại Phòng</th>
                            <th>Giá Thuê</th>
                            <th>Tầng</th>
                            <th>Trạng Thái</th>
                            <th>Mô Tả</th>
                            <th>Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="room" items="${rooms}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td><strong>${room.roomNumber}</strong></td>
                                <td>${room.type}</td>
                                <td>
                                    <span class="price-tag">
                                        <fmt:formatNumber value="${room.price}" type="number" groupingUsed="true"/> VNĐ
                                    </span>
                                </td>
                                <td>Tầng ${room.floor}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${room.status == 'Trống'}">
                                            <span class="badge-status badge-vacant">
                                                <i class="fas fa-check-circle"></i> Trống
                                            </span>
                                        </c:when>
                                        <c:when test="${room.status == 'Đang thuê'}">
                                            <span class="badge-status badge-occupied">
                                                <i class="fas fa-user"></i> Đang thuê
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-maintenance">
                                                <i class="fas fa-wrench"></i> ${room.status}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${room.description}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/quan-ly-phong?action=showEditForm&id=${room.roomId}"
                                       class="btn-action btn-edit">
                                        <i class="fas fa-edit"></i> Sửa
                                    </a>
                                    <a href="#"
                                       onclick="confirmDelete(${room.roomId}, '${room.roomNumber}')"
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Search Functionality
    document.getElementById('searchInput').addEventListener('keyup', function() {
        const searchValue = this.value.toLowerCase();
        const tableRows = document.querySelectorAll('#roomTable tbody tr');

        tableRows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(searchValue) ? '' : 'none';
        });
    });

    // Confirm Delete
    function confirmDelete(roomId, roomNumber) {
        if (confirm('Bạn có chắc chắn muốn xóa phòng "' + roomNumber + '"?\n\nLưu ý: Không thể xóa phòng đang có hợp đồng hoạt động.')) {
            window.location.href = '${pageContext.request.contextPath}/quan-ly-phong?action=delete&id=' + roomId;
        }
    }
</script>

</body>
</html>