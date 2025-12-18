// dao/UserDAO.java

package dao;

import models.User;
import config.DatabaseConfig; // Đảm bảo lớp kết nối DB của bạn là config.DBConfig

import java.sql.*;

public class UserDAO {

    // --- 1. Phương thức Đăng nhập ---
    public User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, full_name, role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    // Lưu ý: Không cần lấy password ra sau khi xác thực
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- 2. Phương thức Kiểm tra Username đã tồn tại ---
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        boolean isTaken = false;

        try (Connection connection = config.DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Nếu ResultSet có dữ liệu (rs.next() trả về true), nghĩa là username đã tồn tại
                if (rs.next()) {
                    isTaken = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isTaken;
    }
    // --- 3. Phương thức Đăng ký Người dùng mới ---
    public boolean registerUser(User user) {
        // Mặc định vai trò là 'manager'
        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, 'manager')";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu chèn thành công

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}