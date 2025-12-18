package dao;

import config.DatabaseConfig;
import models.Tenant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TenantDAO {

    // 1. Lấy tất cả khách thuê
    public List<Tenant> getAllTenants(int userId) {
        List<Tenant> tenants = new ArrayList<>();
        String SQL = "SELECT * FROM Tenant WHERE user_id = ? ORDER BY tenant_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    Tenant tenant = new Tenant(
                            rs.getInt("tenant_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("id_card"),
                            rs.getString("address")
                    );
                    tenant.setUserId(rs.getInt("user_id"));
                    tenants.add(tenant);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAllTenants: " + e.getMessage());
            e.printStackTrace();
        }
        return tenants;
    }

    // 2. Thêm khách thuê mới
    public boolean addTenant(Tenant tenant) {
        String SQL = "INSERT INTO Tenant (name, phone, email, id_card, address, user_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tenant.getName());
            pstmt.setString(2, tenant.getPhone());
            pstmt.setString(3, tenant.getEmail());
            pstmt.setString(4, tenant.getIdCard());
            pstmt.setString(5, tenant.getAddress());
            pstmt.setInt(6, tenant.getUserId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        tenant.setTenantId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Thêm khách thuê thành công");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi addTenant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật khách thuê - ✅ ĐÃ SỬA LỖI
    public boolean updateTenant(Tenant tenant) {
        String SQL = "UPDATE Tenant SET name = ?, phone = ?, email = ?, id_card = ?, address = ? WHERE tenant_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            // ✅ QUAN TRỌNG: Set đúng thứ tự tham số
            pstmt.setString(1, tenant.getName());
            pstmt.setString(2, tenant.getPhone());
            pstmt.setString(3, tenant.getEmail());
            pstmt.setString(4, tenant.getIdCard());
            pstmt.setString(5, tenant.getAddress());
            pstmt.setInt(6, tenant.getTenantId());
            pstmt.setInt(7, tenant.getUserId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật khách thuê ID " + tenant.getTenantId() + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không tìm thấy khách thuê ID " + tenant.getTenantId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi updateTenant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa khách thuê - ✅ ĐÃ SỬA LỖI
    public boolean deleteTenant(int tenantId, int userId) {
        String SQL = "DELETE FROM Tenant WHERE tenant_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Xóa khách thuê ID " + tenantId + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không thể xóa khách thuê ID " + tenantId + " (có thể đang có hợp đồng)");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi deleteTenant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lấy thông tin khách thuê theo ID
    public Tenant getTenantById(int tenantId, int userId) {
        String SQL = "SELECT * FROM Tenant WHERE tenant_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tenant tenant = new Tenant(
                            rs.getInt("tenant_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("id_card"),
                            rs.getString("address")
                    );
                    tenant.setUserId(rs.getInt("user_id"));
                    return tenant;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getTenantById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 6. Kiểm tra CMND/CCCD đã tồn tại chưa
    public boolean isIdCardExists(String idCard, int userId) {
        String SQL = "SELECT COUNT(*) FROM Tenant WHERE id_card = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, idCard);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi isIdCardExists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 7. Kiểm tra Số điện thoại đã tồn tại chưa
    public boolean isPhoneExists(String phone, int userId) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        String SQL = "SELECT COUNT(*) FROM Tenant WHERE phone = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, phone);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi isPhoneExists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 8. Kiểm tra CMND/CCCD có trùng với người khác không (trừ tenantId hiện tại)
    public boolean isIdCardExistsExcludingId(String idCard, int currentTenantId, int userId) {
        String SQL = "SELECT COUNT(*) FROM Tenant WHERE id_card = ? AND tenant_id != ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, idCard);
            pstmt.setInt(2, currentTenantId);
            pstmt.setInt(3, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi isIdCardExistsExcludingId: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}