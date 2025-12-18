package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.IOException;

/**
 * ⚠️ FILTER NÀY CÓ THỂ GÂY XUNG ĐỘT!
 *
 * CÁCH SỬA:
 * 1. Tắt filter này (comment @WebFilter)
 * 2. Hoặc đổi logic kiểm tra để không redirect
 *
 * Hiện tại đang TẮT để xử lý quyền trong Controller
 */
// @WebFilter(urlPatterns = {"/thong-ke"})  // ✅ ĐÃ TẮT
public class StatisticsAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 1. Kiểm tra đã đăng nhập chưa
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Chưa đăng nhập -> Chuyển về login
            res.sendRedirect(req.getContextPath() + "/auth?action=showLogin");
            return;
        }

        // 2. Kiểm tra role có phải ADMIN không
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            // Không phải admin -> Hiển thị lỗi 403 Forbidden
            res.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này. Chỉ Admin mới được xem Thống Kê.");
            return;
        }

        // 3. Nếu là admin -> Cho phép truy cập
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    @Override
    public void destroy() {
        // Hủy filter
    }
}