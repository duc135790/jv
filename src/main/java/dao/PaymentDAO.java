package dao;

import config.DatabaseConfig;
import models.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PaymentDAO {

    // Lấy tất cả thanh toán
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String SQL = "SELECT * FROM Payment ORDER BY payment_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("contract_id"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("method"),
                        rs.getString("description")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAllPayments: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    // Lấy thanh toán theo ID
    public Payment getPaymentById(int paymentId) {
        String SQL = "SELECT * FROM Payment WHERE payment_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, paymentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("contract_id"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("payment_date"),
                            rs.getString("method"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getPaymentById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Thêm thanh toán mới
    public boolean addPayment(Payment payment) {
        String SQL = "INSERT INTO Payment (contract_id, amount, payment_date, method, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, payment.getContractId());
            pstmt.setBigDecimal(2, payment.getAmount());
            pstmt.setDate(3, payment.getPaymentDate());
            pstmt.setString(4, payment.getMethod());
            pstmt.setString(5, payment.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Thêm thanh toán thành công");
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi addPayment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thanh toán
    public boolean updatePayment(Payment payment) {
        String SQL = "UPDATE Payment SET contract_id = ?, amount = ?, payment_date = ?, method = ?, description = ? WHERE payment_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, payment.getContractId());
            pstmt.setBigDecimal(2, payment.getAmount());
            pstmt.setDate(3, payment.getPaymentDate());
            pstmt.setString(4, payment.getMethod());
            pstmt.setString(5, payment.getDescription());
            pstmt.setInt(6, payment.getPaymentId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật thanh toán ID " + payment.getPaymentId() + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không tìm thấy thanh toán ID " + payment.getPaymentId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi updatePayment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // XÓA THANH TOÁN - ✅ ĐÃ SỬA LỖI
    public boolean deletePayment(int paymentId) {
        String SQL = "DELETE FROM Payment WHERE payment_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, paymentId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Xóa thanh toán ID " + paymentId + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không thể xóa thanh toán ID " + paymentId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi deletePayment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tất cả Contract ID (Để dùng cho ComboBox trong PaymentPanel)
    public List<Integer> getAllContractIds() {
        List<Integer> contractIds = new ArrayList<>();
        String SQL = "SELECT contract_id FROM Contract WHERE status = 'Đang thuê'";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                contractIds.add(rs.getInt("contract_id"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAllContractIds: " + e.getMessage());
            e.printStackTrace();
        }
        return contractIds;
    }
}