<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle} | B O A Management</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

    <script>
        // Script này sẽ chạy ngay khi trình duyệt đọc đến thẻ <head>
        (function() {
            const THEME_KEY = 'theme-mode';
            const savedTheme = localStorage.getItem(THEME_KEY);

            if (savedTheme === 'dark') {
                document.documentElement.classList.add('dark-mode');
            }
        })();
    </script>

    <style>
        /* Logo với Icon */
        .navbar-brand {
            display: flex;
            align-items: center;
            gap: 10px;
            font-weight: 700;
            font-size: 1.5rem;
        }

        .logo-icon {
            width: 35px;
            height: 35px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.2rem;
        }

        .logo-text {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
    </style>
</head>

<body class="bg-light">

    <nav class="navbar navbar-expand-lg bg-white py-3 border-bottom shadow-sm">
        <div class="container-fluid container-lg">

            <div class="d-flex align-items-center">
                <!-- Logo với Icon -->
                <a href="${pageContext.request.contextPath}/trang-chu" class="navbar-brand text-decoration-none">
                    <div class="logo-icon">
                        <i class="fas fa-home"></i>
                    </div>
                    <span class="logo-text">B O A</span>
                </a>

                <!-- Menu chính - Chỉ hiển thị khi đã đăng nhập -->
                <c:if test="${sessionScope.user != null}">
                    <div class="d-none d-lg-flex gap-4 small fw-semibold ms-4">
                        <a href="${pageContext.request.contextPath}/trang-chu" class="nav-link text-dark">
                            <i class="fas fa-th-large me-1"></i> Bảng Điều Khiển
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-phong" class="nav-link text-dark">
                            <i class="fas fa-building me-1"></i> Phòng
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-khach-thue" class="nav-link text-dark">
                            <i class="fas fa-users me-1"></i> Khách Thuê
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-hop-dong" class="nav-link text-dark">
                            <i class="fas fa-file-contract me-1"></i> Hợp Đồng
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-thanh-toan" class="nav-link text-dark">
                            <i class="fas fa-money-bill-wave me-1"></i> Thanh Toán
                        </a>

                        <!-- Thống Kê - Chỉ cho Admin -->
                        <c:if test="${sessionScope.user.role == 'admin'}">
                            <a href="${pageContext.request.contextPath}/thong-ke" class="nav-link text-dark">
                                <i class="fas fa-chart-line me-1"></i> Thống Kê
                            </a>
                        </c:if>
                    </div>
                </c:if>
            </div>

            <!-- User Dropdown -->
            <div class="dropdown">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <div class="user-dropdown-toggle d-flex align-items-center gap-2" data-bs-toggle="dropdown" aria-expanded="false" style="cursor: pointer;">
                            <i class="bi bi-person-circle fs-5"></i>
                            <span class="fw-semibold text-dark"><c:out value="${sessionScope.user.fullName}" default="Quản trị viên"/></span>
                        </div>

                        <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2">
                            <li class="px-3 py-2 border-bottom bg-light">
                                <div class="fw-bold"><c:out value="${sessionScope.user.fullName}"/></div>
                                <div class="small text-muted">@<c:out value="${sessionScope.user.username}"/></div>
                            </li>

                            <li>
                                <a class="dropdown-item py-2 text-danger fw-bold" href="${pageContext.request.contextPath}/auth?action=logout">
                                    <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                                </a>
                            </li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/auth?action=showLogin" class="btn btn-dark fw-bold me-2 py-2">Đăng Nhập</a>
                        <a href="${pageContext.request.contextPath}/auth?action=showRegister" class="btn btn-outline-dark fw-bold py-2 d-none d-md-inline-block">Đăng Ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <main>