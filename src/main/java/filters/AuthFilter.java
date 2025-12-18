// filters/AuthFilter.java

package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Áp dụng Filter cho tất cả các URL trừ /auth (Login/Register)
@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    // Danh sách các URL được phép truy cập công khai (WHITELIST)
    private static final String[] PUBLIC_URLS = {
            "/auth", // Controller Login/Register
            "/assets/", // CSS, JS, Images, v.v.
            "/WEB-INF/" // Không thể truy cập trực tiếp
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // --- 1. Kiểm tra các URL công khai (Public Access) ---
        if (isPublicResource(path)) {
            // Nếu là tài nguyên công khai, cho phép đi tiếp
            chain.doFilter(request, response);
            return;
        }

        // --- 2. Kiểm tra Session/Đăng nhập ---
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // Nếu đã đăng nhập, cho phép truy cập tài nguyên
            chain.doFilter(request, response);
        } else {
            // Nếu chưa đăng nhập và cố gắng truy cập trang quản lý
            // Chuyển hướng về trang Đăng nhập
            res.sendRedirect(req.getContextPath() + "/auth?action=showLogin");
        }
    }

    /**
     * Kiểm tra xem đường dẫn có nằm trong danh sách public hay không.
     */
    private boolean isPublicResource(String path) {
        if (path.isEmpty() || path.equals("/")) {
            // Nếu người dùng truy cập gốc /, chúng ta coi đó là trang công khai (sẽ chuyển hướng đến /auth)
            return true;
        }
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo (nếu cần)
    }

    @Override
    public void destroy() {
        // Hủy bỏ (nếu cần)
    }
}