<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    </main>

    <footer class="text-center text-secondary small mt-5 py-5 border-top bg-white">
        <div class="container">
            <div class="fw-bold fs-5 mb-3">B O A</div>
            <div class="d-flex justify-content-center gap-4">
                <a href="${pageContext.request.contextPath}/trang-chu" class="text-decoration-none text-secondary">Trang chủ</a>
                <a href="#" class="text-decoration-none text-secondary">Hỗ trợ</a>
                <a href="#" class="text-decoration-none text-secondary">Chính sách</a>
            </div>
            <div class="mt-4 text-muted">&copy; 2025 B O A Management System.</div>
        </div>
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Khóa lưu trữ cho theme
            const THEME_KEY = 'theme-mode';
            const body = document.body;
            const themeToggleBtn = document.getElementById('theme-toggle');

            // 1. Tải trạng thái theme từ LocalStorage
            function loadTheme() {
                // Lấy theme đã lưu, mặc định là 'light' nếu chưa có
                const savedTheme = localStorage.getItem(THEME_KEY) || 'light';

                // ✅ Lưu trạng thái theme vào Session/Client để Controller có thể dùng
                // Đây là nơi JavaScript thêm class 'dark-mode'
                if (savedTheme === 'dark') {
                    body.classList.add('dark-mode');
                } else {
                    body.classList.remove('dark-mode');
                }
                updateToggleButtonIcon(savedTheme);
            }

            // 2. Cập nhật icon của nút chuyển đổi
            function updateToggleButtonIcon(theme) {
                if (themeToggleBtn) {
                    if (theme === 'dark') {
                        themeToggleBtn.innerHTML = '<i class="bi bi-sun-fill me-2"></i> Chế độ Sáng';
                    } else {
                        themeToggleBtn.innerHTML = '<i class="bi bi-moon-fill me-2"></i> Chế độ Tối';
                    }
                }
            }

            // 3. Hàm xử lý chuyển đổi khi click
            function toggleTheme() {
                const isDarkMode = body.classList.toggle('dark-mode');
                const newTheme = isDarkMode ? 'dark' : 'light';

                // Lưu trạng thái mới vào LocalStorage
                localStorage.setItem(THEME_KEY, newTheme);
                updateToggleButtonIcon(newTheme);
            }

            // Gán sự kiện cho nút (Chỉ gán nếu nút tồn tại)
            if (themeToggleBtn) {
                themeToggleBtn.addEventListener('click', toggleTheme);
            }

            // Tải theme ngay khi trang được tải
            loadTheme();
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>