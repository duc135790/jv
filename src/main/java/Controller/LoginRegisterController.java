// Controller/LoginRegisterController.java

package Controller;

import Services.UserService;
import models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginRegisterController", urlPatterns = {"/auth"})
public class LoginRegisterController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
    }

    // --- Xử lý hiển thị form (GET) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String contextPath = request.getContextPath();

        // 1. Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false);

        if (action == null || action.equals("showLogin")) {
            // Xử lý thông báo đăng ký thành công (nếu có)
            if (session != null && session.getAttribute("successMessage") != null) {
                // Chuyển từ Session sang Request để hiển thị 1 lần
                request.setAttribute("successMessage", session.getAttribute("successMessage"));
                session.removeAttribute("successMessage");
            }
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);

        } else if (action.equals("showRegister")) {
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);

        } else if (action.equals("logout")) {

            if (session != null) {
                session.invalidate(); // ✅ Hủy session
            }

            // Chuyển hướng về trang đăng nhập
            response.sendRedirect(contextPath + "/auth?action=showLogin");
            return;
        }
    }

    // --- Xử lý gửi form (POST) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String contextPath = request.getContextPath();

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter.");
            return;
        }

        switch (action) {
            case "login":
                handleLogin(request, response, contextPath);
                break;
            case "register":
                handleRegister(request, response, contextPath);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter.");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, String contextPath)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. Kiểm tra đăng nhập qua Service Layer
        User user = userService.login(username, password); // Giả định phương thức này trả về đối tượng User hoặc null

        if (user == null) {
            // Đăng nhập thất bại
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");

            // Chuyển tiếp về trang login (Dùng đường dẫn đã sửa: /WEB-INF/views/...)
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } else {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Lưu đối tượng User vào Session

            // Chuyển hướng đến trang chính
            response.sendRedirect(contextPath + "/trang-chu");
        }
    }

    /**
     * Xử lý logic Đăng ký
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, String contextPath)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        boolean registrationSuccess = false;
        String message = "";

        // 1. Kiểm tra username đã tồn tại chưa
        if (userService.isUsernameTaken(username)) {
            message = "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.";
            registrationSuccess = false; // Đảm bảo là thất bại
        } else {
            // ✅ TẠO ĐỐI TƯỢNG USER TRƯỚC KHI GỌI SERVICE
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password); // Lưu ý: Password nên được hash trước khi gọi Service/DAO
            newUser.setFullName(fullName);

            // 2. Thực hiện đăng ký (gọi phương thức register(User user))
            registrationSuccess = userService.register(newUser);

            if (registrationSuccess) {
                message = "Đăng ký thành công! Bạn có thể đăng nhập ngay.";
            } else {
                message = "Đăng ký thất bại do lỗi hệ thống.";
            }
        }

        if (registrationSuccess) {
            // Đăng ký thành công -> Chuyển hướng về trang Đăng nhập
            request.getSession().setAttribute("successMessage", message);
            response.sendRedirect(contextPath + "/auth?action=showLogin");
        } else {
            // Đăng ký thất bại -> Hiển thị lỗi trên trang đăng ký
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}