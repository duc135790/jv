package dao;

import config.DatabaseConfig;
import models.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class RoomDAO {

    // 1. Lấy tất cả phòng của một người dùng
    public List<Room> getAllRooms(int userId) {
        List<Room> rooms = new ArrayList<>();
        String SQL = "SELECT * FROM Room WHERE user_id = ? ORDER BY room_id ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room(
                            rs.getInt("room_id"),
                            rs.getString("room_number"),
                            rs.getString("type"),
                            rs.getBigDecimal("price"),
                            rs.getString("status"),
                            rs.getInt("floor"),
                            rs.getString("description")
                    );
                    room.setUserId(rs.getInt("user_id"));
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getAllRooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    // 2. Thêm phòng mới
    public boolean addRoom(Room room) {
        String SQL = "INSERT INTO Room (room_number, type, price, status, floor, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setBigDecimal(3, room.getPrice());
            pstmt.setString(4, room.getStatus());
            pstmt.setInt(5, room.getFloor());
            pstmt.setString(6, room.getDescription());
            pstmt.setInt(7, room.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Thêm phòng thành công. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi addRoom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật phòng - ✅ ĐÃ SỬA LỖI
    public boolean updateRoom(Room room) {
        String SQL = "UPDATE Room SET room_number = ?, type = ?, price = ?, status = ?, floor = ?, description = ? WHERE room_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            // ✅ QUAN TRỌNG: Set tất cả các tham số theo đúng thứ tự
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setBigDecimal(3, room.getPrice());
            pstmt.setString(4, room.getStatus());
            pstmt.setInt(5, room.getFloor());
            pstmt.setString(6, room.getDescription());
            pstmt.setInt(7, room.getRoomId());
            pstmt.setInt(8, room.getUserId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật phòng ID " + room.getRoomId() + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không tìm thấy phòng ID " + room.getRoomId() + " hoặc không thuộc về user " + room.getUserId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi updateRoom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa phòng - ✅ ĐÃ SỬA LỖI
    public boolean deleteRoom(int roomId, int userId) {
        String SQL = "DELETE FROM Room WHERE room_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, roomId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Xóa phòng ID " + roomId + " thành công");
                return true;
            } else {
                System.err.println("⚠️ Không thể xóa phòng ID " + roomId + " (có thể đang có hợp đồng hoặc không thuộc về user)");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi deleteRoom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 5. Cập nhật trạng thái phòng
    public boolean updateRoomStatus(int roomId, String newStatus) {
        String SQL = "UPDATE Room SET status = ? WHERE room_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, roomId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Cập nhật trạng thái phòng ID " + roomId + " thành " + newStatus);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi updateRoomStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 6. Lấy thông tin phòng theo ID
    public Room getRoomById(int roomId, int userId) {
        String SQL = "SELECT * FROM Room WHERE room_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, roomId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room(
                            rs.getInt("room_id"),
                            rs.getString("room_number"),
                            rs.getString("type"),
                            rs.getBigDecimal("price"),
                            rs.getString("status"),
                            rs.getInt("floor"),
                            rs.getString("description")
                    );
                    room.setUserId(rs.getInt("user_id"));
                    return room;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getRoomById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 7. Kiểm tra số phòng đã tồn tại chưa
    public boolean isRoomNumberExists(String roomNumber, int userId) {
        String SQL = "SELECT COUNT(*) FROM Room WHERE room_number = ? AND user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, roomNumber);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi isRoomNumberExists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 8. Lấy danh sách các phòng trống
    public List<Room> getVacantRooms(int userId) {
        List<Room> rooms = new ArrayList<>();
        String SQL = "SELECT * FROM Room WHERE status = 'Trống' AND user_id = ? ORDER BY room_number ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room(
                            rs.getInt("room_id"),
                            rs.getString("room_number"),
                            rs.getString("type"),
                            rs.getBigDecimal("price"),
                            rs.getString("status"),
                            rs.getInt("floor"),
                            rs.getString("description")
                    );
                    room.setUserId(rs.getInt("user_id"));
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi getVacantRooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
}