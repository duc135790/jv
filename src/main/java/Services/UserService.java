// Services/UserService.java

package Services;

import dao.UserDAO;
import models.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // --- 1. Logic Đăng nhập ---
    public User login(String username, String password) {
        // Kiểm tra tính hợp lệ cơ bản của đầu vào (ví dụ: không rỗng)
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }

        // Gọi DAO để xác thực
        return userDAO.getUserByUsernameAndPassword(username, password);
    }

    // --- 2. Logic Đăng ký ---
    public boolean register(User user) {
        // Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getFullName() == null || user.getFullName().trim().isEmpty())
                {
            return false;
        }

        // Kiểm tra username đã tồn tại chưa
        if (userDAO.isUsernameTaken(user.getUsername())) {
            // Trả về false để Controller biết rằng Đăng ký thất bại
            return false;
        }

        // 2. Nếu hợp lệ, gọi DAO
        return userDAO.registerUser(user);
    }

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // ... (Phương thức login và register khác) ...

    /**
     * Kiểm tra xem tên đăng nhập đã tồn tại trong DB chưa
     */
    public boolean isUsernameTaken(String username) {
        return userDAO.isUsernameTaken(username);
    }
}