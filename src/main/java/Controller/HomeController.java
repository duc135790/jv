package Controller;
import models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

@WebServlet(name = "HomeController", urlPatterns = {"/trang-chu"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }

        // 2. Nếu đã đăng nhập, chuyển tiếp đến trang Dashboard
        request.getRequestDispatcher("/WEB-INF/views/room/dashboard.jsp").forward(request, response);
    }
}