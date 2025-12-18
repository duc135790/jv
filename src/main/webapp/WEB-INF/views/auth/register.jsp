<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Đăng Ký Tài Khoản" scope="request"/>
<jsp:include page="../layout/_header.jsp" />

<style>
    /* ================================================= */
    /* 1. RESET BODY */
    /* ================================================= */
    body {
        /* Bỏ background image khỏi body */
        background-image: none !important;
        background-color: #f8f9fa;
        min-height: 100vh;
        margin: 0;
        display: none; /* Giữ lại để dùng fadeIn */
        display: flex;
        flex-direction: column;
    }

    /* ================================================= */
    /* 2. BACKGROUND CHO NỘI DUNG GIỮA (Giống hệt Login) */
    /* ================================================= */
    .main-content-background {
        flex-grow: 1;
        width: 100%;

        /* Áp dụng hình ảnh nền (Dùng cùng ảnh với trang Login) */
        background-image: linear-gradient(rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.1)),
                          url('https://goghepminhcuong.com/wp-content/uploads/2023/12/AdobeStock_621015737-scaled.jpeg');

        background-size: cover;
        background-position: center;
        background-attachment: scroll; /* Cuộn theo trang */

        /* CĂN GIỮA FORM */
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 40px 0; /* Giảm padding nếu form Register dài hơn */
    }

    /* 3. Tinh chỉnh form đăng ký (auth-container) */
    .auth-container {
        max-width: 420px;
        margin-top: 0;
        padding: 40px;
        border-radius: 12px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
        background: rgba(255, 255, 255, 1.0);
        border: 1px solid rgba(0, 0, 0, 0.1);
    }
    .logo {
        font-size: 2.2rem;
        font-weight: bold;
        color: #343a40;
    }
</style>

<div class="main-content-background">
    <div class="container-lg">
        <div class="row justify-content-center">
            <div class="col-md-7 auth-container">
                <div class="text-center mb-5">
                    <div class="logo">B O A</div>
                    <h1 class="fs-4 fw-bold mt-3">Đăng Ký Tài Khoản<br> Quản Lý Mới</h1>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger fw-semibold" role="alert">
                        <i class="bi bi-x-octagon-fill me-2"></i> ${errorMessage}
                    </div>
                </c:if>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success fw-semibold" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i> ${successMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/auth" method="POST">
                    <input type="hidden" name="action" value="register"/>

                    <div class="mb-3">
                        <label for="fullName" class="form-label fw-semibold">Họ và Tên</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control" id="fullName" name="fullName" required aria-label="Họ và Tên">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold">Tên đăng nhập</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person-circle"></i></span>
                            <input type="text" class="form-control" id="username" name="username" required aria-label="Tên đăng nhập">
                        </div>
                    </div>

                     <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email (Không bắt buộc)</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" aria-label="Email">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Mật khẩu</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" required aria-label="Mật khẩu">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 fw-bold py-2">Đăng Ký</button>
                </form>

                <div class="text-center mt-4 small">
                    Đã có tài khoản?
                    <a href="#" id="login-link" class="text-decoration-none fw-semibold">Đăng Nhập</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
    $(document).ready(function() {
        // 1. HIỆN NỘI DUNG MƯỢT MÀ KHI TẢI TRANG
        $('body').fadeIn(400);

        // 2. XỬ LÝ CHUYỂN TRANG MƯỢT MÀ
        $('#login-link').on('click', function(e) {
            e.preventDefault();

            var targetUrl = "${pageContext.request.contextPath}/auth?action=showLogin";

            // Ẩn nội dung body bằng hiệu ứng mờ dần trước khi chuyển hướng
            $('body').fadeOut(400, function() {
                window.location.href = targetUrl;
            });
        });
    });
</script>
<jsp:include page="../layout/_footer.jsp" />