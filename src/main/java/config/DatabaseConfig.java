package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConfig {

    // THÔNG TIN KẾT NỐI (CẦN CHỈNH SỬA NẾU KHÁC)
    private static final String URL = "jdbc:mysql://localhost:3306/QuanLyPhongTro"; 
    private static final String USER = "root"; 
    private static final String PASS = "123456"; // Đổi thành mật khẩu của bạn nếu có

    public static Connection getConnection() {
        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Lấy kết nối
            return DriverManager.getConnection(URL, USER, PASS);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi Driver SQL. Vui lòng kiểm tra lại pom.xml.");
            return null;
        } catch (SQLException e) {
            // Hiển thị lỗi nếu kết nối thất bại
            JOptionPane.showMessageDialog(null, 
                    "Lỗi kết nối CSDL: " + e.getMessage() + 
                    "\nKiểm tra XAMPP (MySQL) và tên Database trong DatabaseConfig.java.", 
                    "Lỗi Kết nối SQL", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}