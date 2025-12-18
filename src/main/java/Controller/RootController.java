package Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet xử lý yêu cầu truy cập đường dẫn gốc ("/") của ứng dụng.
 * Mục đích là chuyển hướng người dùng đến luồng xử lý chính.
 */
@WebServlet(urlPatterns = {"/"})
public class RootController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 1. LẤY CONTEXT PATH: Ví dụ: /QuanLyPhongTroMV
        String contextPath = request.getContextPath();

        // 2. CHUYỂN HƯỚNG: Chuyển hướng Browser đến /trang-chu.
        // Tại /trang-chu, HomeController sẽ kiểm tra Session.
        // - Nếu đã đăng nhập: Hiển thị dashboard.jsp.
        // - Nếu chưa đăng nhập: Redirect sang /auth?action=showLogin.
        // Đây là cách chuẩn để khởi động luồng kiểm tra bảo mật (security flow).
        response.sendRedirect(contextPath + "/trang-chu");
    }
}