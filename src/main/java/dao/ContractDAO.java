package dao;

import config.DatabaseConfig;
import models.Contract;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {

    // 1. Lấy tất cả hợp đồng
    public List<Contract> getAllContracts(int userId) {
        List<Contract> contracts = new ArrayList<>();

        String SQL = "SELECT c.*, t.name as tenant_name, r.room_number " +
                "FROM Contract c " +
                "LEFT JOIN Tenant t ON c.tenant_id = t.tenant_id " +
                "LEFT JOIN Room r ON c.room_id = r.room_id " +
                "WHERE c.user_id = ? " +
                "ORDER BY c.contract_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract(
                            rs.getInt("contract_id"),
                            rs.getInt("tenant_id"),
                            rs.getInt("room_id"),
                            rs.getInt("staff_id"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("status"),
                            rs.getBigDecimal("deposit_amount")
                    );
                    contract.setUserId(rs.getInt("user_id"));
                    contract.setTenantName(rs.getString("tenant_name"));
                    contract.setRoomNumber(rs.getString("room_number"));

                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAllContracts: " + e.getMessage());
            e.printStackTrace();
        }
        return contracts;
    }

    // 2. Thêm hợp đồng mới
    public boolean addContract(Contract contract) {
        String SQL = "INSERT INTO Contract (tenant_id, room_id, staff_id, start_date, end_date, status, deposit_amount, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setInt(3, contract.getStaffId());
            pstmt.setDate(4, contract.getStartDate());
            pstmt.setDate(5, contract.getEndDate());
            pstmt.setString(6, contract.getStatus());
            pstmt.setBigDecimal(7, contract.getDepositAmount());
            pstmt.setInt(8, contract.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Thêm hợp đồng thành công");
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi addContract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật hợp đồng
    public boolean updateContract(Contract contract) {
        String SQL = "UPDATE Contract SET tenant_id = ?, room_id = ?, staff_id = ?, " +
                "start_date = ?, end_date = ?, status = ?, deposit_amount = ? " +
                "WHERE contract_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setInt(3, contract.getStaffId());
            pstmt.setDate(4, contract.getStartDate());
            pstmt.setDate(5, contract.getEndDate());
            pstmt.setString(6, contract.getStatus());
            pstmt.setBigDecimal(7, contract.getDepositAmount());
            pstmt.setInt(8, contract.getContractId());
            pstmt.setInt(9, contract.getUserId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật hợp đồng ID " + contract.getContractId() + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không tìm thấy hợp đồng ID " + contract.getContractId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi updateContract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. XÓA HỢP ĐỒNG - ✅ ĐÃ SỬA LỖI
    public boolean deleteContract(int contractId, int userId) {
        String SQL = "DELETE FROM Contract WHERE contract_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, contractId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Xóa hợp đồng ID " + contractId + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không thể xóa hợp đồng ID " + contractId + " (có thể có thanh toán liên quan)");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi deleteContract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 5. Kết thúc hợp đồng
    public boolean endContract(int contractId, Date endDate, int userId) {
        String SQL = "UPDATE Contract SET status = 'Đã kết thúc', end_date = ? " +
                "WHERE contract_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDate(1, endDate);
            pstmt.setInt(2, contractId);
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Kết thúc hợp đồng ID " + contractId);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi endContract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 6. Lấy thông tin hợp đồng theo ID
    public Contract getContractById(int contractId, int userId) {
        String SQL = "SELECT * FROM Contract WHERE contract_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, contractId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Contract contract = new Contract(
                            rs.getInt("contract_id"),
                            rs.getInt("tenant_id"),
                            rs.getInt("room_id"),
                            rs.getInt("staff_id"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("status"),
                            rs.getBigDecimal("deposit_amount")
                    );
                    contract.setUserId(rs.getInt("user_id"));
                    return contract;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getContractById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 7. Kiểm tra phòng có hợp đồng đang hoạt động
    public boolean hasActiveContractForRoom(int roomId, int userId) {
        String SQL = "SELECT COUNT(*) FROM Contract " +
                "WHERE room_id = ? AND status = 'Đang thuê' AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, roomId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi hasActiveContractForRoom: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 8. Kiểm tra khách thuê có hợp đồng đang hoạt động
    public boolean hasActiveContractForTenant(int tenantId, int userId) {
        String SQL = "SELECT COUNT(*) FROM Contract " +
                "WHERE tenant_id = ? AND user_id = ? AND status = 'Đang thuê'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi hasActiveContractForTenant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}