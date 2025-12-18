package dao;

import config.DatabaseConfig;
import models.Payment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO XỬ LÝ THỐNG KÊ - ĐÃ HOÀN THIỆN
 * Cung cấp dữ liệu cho Dashboard thống kê doanh thu
 */
public class StatisticsDAO {

    /**
     * Tổng số phòng của user
     */
    public int getTotalRooms(int userId) {
        String SQL = "SELECT COUNT(*) FROM Room WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getTotalRooms: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Số phòng đang thuê
     */
    public int getOccupiedRooms(int userId) {
        String SQL = "SELECT COUNT(*) FROM Room WHERE user_id = ? AND status = 'Đang thuê'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getOccupiedRooms: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Số phòng trống
     */
    public int getVacantRooms(int userId) {
        String SQL = "SELECT COUNT(*) FROM Room WHERE user_id = ? AND status = 'Trống'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getVacantRooms: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tổng doanh thu từ tất cả thanh toán
     */
    public BigDecimal getTotalRevenue(int userId) {
        String SQL = "SELECT COALESCE(SUM(p.amount), 0) " +
                "FROM Payment p " +
                "INNER JOIN Contract c ON p.contract_id = c.contract_id " +
                "WHERE c.user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getTotalRevenue: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Doanh thu theo tháng cụ thể
     */
    public BigDecimal getRevenueByMonth(int userId, int year, int month) {
        String SQL = "SELECT COALESCE(SUM(p.amount), 0) " +
                "FROM Payment p " +
                "INNER JOIN Contract c ON p.contract_id = c.contract_id " +
                "WHERE c.user_id = ? AND YEAR(p.payment_date) = ? AND MONTH(p.payment_date) = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getRevenueByMonth: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Số hợp đồng đang hoạt động
     */
    public int getActiveContracts(int userId) {
        String SQL = "SELECT COUNT(*) FROM Contract WHERE user_id = ? AND status = 'Đang thuê'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getActiveContracts: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Doanh thu 12 tháng trong năm (Chart data)
     */
    public Map<Integer, BigDecimal> getMonthlyRevenue(int userId, int year) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        // Khởi tạo 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            result.put(i, BigDecimal.ZERO);
        }

        String SQL = "SELECT MONTH(p.payment_date) as month, COALESCE(SUM(p.amount), 0) as total " +
                "FROM Payment p " +
                "INNER JOIN Contract c ON p.contract_id = c.contract_id " +
                "WHERE c.user_id = ? AND YEAR(p.payment_date) = ? " +
                "GROUP BY MONTH(p.payment_date) " +
                "ORDER BY MONTH(p.payment_date)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    BigDecimal total = rs.getBigDecimal("total");
                    result.put(month, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getMonthlyRevenue: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Doanh thu theo từng ngày trong tháng (Chart data chi tiết)
     */
    public Map<Integer, BigDecimal> getDailyRevenueByMonth(int userId, int year, int month) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        // Khởi tạo các ngày trong tháng với giá trị 0
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= daysInMonth; i++) {
            result.put(i, BigDecimal.ZERO);
        }

        String SQL = "SELECT DAY(p.payment_date) as day, COALESCE(SUM(p.amount), 0) as total " +
                "FROM Payment p " +
                "INNER JOIN Contract c ON p.contract_id = c.contract_id " +
                "WHERE c.user_id = ? AND YEAR(p.payment_date) = ? AND MONTH(p.payment_date) = ? " +
                "GROUP BY DAY(p.payment_date) " +
                "ORDER BY DAY(p.payment_date)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int day = rs.getInt("day");
                    BigDecimal total = rs.getBigDecimal("total");
                    result.put(day, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getDailyRevenueByMonth: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Doanh thu theo phòng (Top 10 phòng có doanh thu cao nhất)
     */
    public Map<String, BigDecimal> getRevenueByRoom(int userId) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();

        String SQL = "SELECT r.room_number, COALESCE(SUM(p.amount), 0) as total " +
                "FROM Room r " +
                "LEFT JOIN Contract c ON r.room_id = c.room_id " +
                "LEFT JOIN Payment p ON c.contract_id = p.contract_id " +
                "WHERE r.user_id = ? " +
                "GROUP BY r.room_id, r.room_number " +
                "ORDER BY total DESC " +
                "LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("room_number"), rs.getBigDecimal("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getRevenueByRoom: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Lấy danh sách thanh toán gần đây nhất
     */
    public List<Payment> getRecentPayments(int userId, int limit) {
        List<Payment> payments = new ArrayList<>();

        String SQL = "SELECT p.* " +
                "FROM Payment p " +
                "INNER JOIN Contract c ON p.contract_id = c.contract_id " +
                "WHERE c.user_id = ? " +
                "ORDER BY p.payment_date DESC, p.payment_id DESC " +
                "LIMIT ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getRecentPayments: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * ✅ THỐNG KÊ BỔ SUNG: Tổng số khách thuê
     */
    public int getTotalTenants(int userId) {
        String SQL = "SELECT COUNT(*) FROM Tenant WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getTotalTenants: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ✅ THỐNG KÊ BỔ SUNG: Tỷ lệ lấp đầy phòng (%)
     */
    public double getOccupancyRate(int userId) {
        int total = getTotalRooms(userId);
        if (total == 0) return 0.0;

        int occupied = getOccupiedRooms(userId);
        return (occupied * 100.0) / total;
    }

    /**
     * ✅ THỐNG KÊ BỔ SUNG: Doanh thu trung bình/phòng/tháng
     */
    public BigDecimal getAverageRevenuePerRoom(int userId) {
        String SQL = "SELECT AVG(room_revenue) as avg_revenue FROM (" +
                "SELECT r.room_id, COALESCE(SUM(p.amount), 0) as room_revenue " +
                "FROM Room r " +
                "LEFT JOIN Contract c ON r.room_id = c.room_id " +
                "LEFT JOIN Payment p ON c.contract_id = p.contract_id " +
                "WHERE r.user_id = ? " +
                "GROUP BY r.room_id" +
                ") as room_totals";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal avg = rs.getBigDecimal("avg_revenue");
                    return avg != null ? avg : BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAverageRevenuePerRoom: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}