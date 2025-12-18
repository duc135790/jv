package Services;

import models.Room;
import dao.RoomDAO;
import java.util.List;
import java.math.BigDecimal;
import models.User; // Import User nếu cần, nhưng không dùng trong code này

public class RoomService {

    private RoomDAO roomDAO;
    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    // --- Phương thức truy vấn ---

    // ✅ 1. getAllRooms: Đã đúng, nhận userId
    public List<Room> getAllRooms(int userId) {
        return roomDAO.getAllRooms(userId);
    }

    // ✅ 2. getRoomById: PHẢI NHẬN userId từ Controller
    public Room getRoomById(int roomId, int userId) { // SỬA: Thêm tham số userId
        if (roomId <= 0 || userId <= 0) {
            return null;
        }
        // Gọi DAO với cả 2 tham số: roomId và userId
        return roomDAO.getRoomById(roomId, userId);
    }

    // --- Phương thức Thêm (Bao gồm Logic nghiệp vụ) ---
    public boolean addRoom(Room room) {

        // Kiểm tra dữ liệu cơ bản. Room object phải chứa userId ở Controller
        if (room == null || room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty() ||
                room.getPrice() == null || room.getUserId() <= 0) {
            System.err.println("Lỗi nghiệp vụ: Dữ liệu phòng hoặc User ID không hợp lệ.");
            return false;
        }

        // ✅ 3. isRoomNumberExists: Kiểm tra trùng lặp TRONG PHẠM VI USER
        if (roomDAO.isRoomNumberExists(room.getRoomNumber(), room.getUserId())) {
            System.err.println("Lỗi nghiệp vụ: Số phòng " + room.getRoomNumber() + " đã tồn tại cho người dùng này.");
            return false;
        }

        // Gọi DAO để thêm phòng
        return roomDAO.addRoom(room);
    }

    // --- Phương thức Cập nhật (Bao gồm Logic nghiệp vụ) ---
    // ✅ 4. updateRoom: PHẢI NHẬN userId nếu logic kiểm tra cần (hoặc dùng userId từ room object)
    // Giả định userId được lấy từ room object (được Controller gán vào)
//    public boolean updateRoom(Room room) {
//
//        // Kiểm tra cơ bản
//        if (room == null || room.getRoomId() <= 0 || room.getUserId() <= 0) {
//            return false;
//        }
//
//        // Logic 1: Kiểm tra phòng có tồn tại và thuộc về user này không
//        // SỬA LỖI: Gọi getRoomById(roomId, userId) với tham số userId lấy từ room object
//        if (getRoomById(room.getRoomId(), room.getUserId()) == null) {
//            System.err.println("Lỗi nghiệp vụ: Phòng cần cập nhật không tồn tại hoặc không thuộc về người dùng.");
//            return false;
//        }
//
//        // Gọi DAO (DAO sẽ dùng userId đã có trong room object để lọc trong WHERE clause)
//        return roomDAO.updateRoom(room);
//    }
    // === SỬA CHỖ NÀY TRONG RoomService.java ===
    public boolean updateRoom(Room room) {
        if (room == null || room.getRoomId() <= 0 || room.getUserId() <= 0) {
            return false;
        }

        // Lấy thông tin phòng cũ từ CSDL
        Room existingRoom = getRoomById(room.getRoomId(), room.getUserId());
        if (existingRoom == null) {
            System.err.println("Phòng không tồn tại hoặc không thuộc về user");
            return false;
        }

        // CHỈ KIỂM TRA TRÙNG NẾU NGƯỜI DÙNG THAY ĐỔI SỐ PHÒNG
        if (!existingRoom.getRoomNumber().equals(room.getRoomNumber())) {
            // Có đổi số phòng → kiểm tra xem số mới có bị trùng không
            if (roomDAO.isRoomNumberExists(room.getRoomNumber(), room.getUserId())) {
                System.err.println("Số phòng mới '" + room.getRoomNumber() + "' đã tồn tại!");
                return false; // ← Đây là lúc báo lỗi "Số phòng có thể đã trùng"
            }
        }

        // Không trùng hoặc không đổi số phòng → cho phép cập nhật
        return roomDAO.updateRoom(room);
    }
    public boolean isRoomNumberExists(String roomNumber, int userId) {
        return roomDAO.isRoomNumberExists(roomNumber, userId);
    }

    // --- Phương thức Xóa (Bao gồm Logic nghiệp vụ) ---
    // ✅ 5. deleteRoom: PHẢI NHẬN userId từ Controller
    public boolean deleteRoom(int roomId, int userId) { // SỬA: Thêm tham số userId

        // Logic kiểm tra quyền sở hữu
        // SỬA LỖI: Gọi getRoomById(roomId, userId)
        if (getRoomById(roomId, userId) == null) {
            System.err.println("Lỗi nghiệp vụ: Phòng cần xóa không tồn tại hoặc không thuộc về người dùng.");
            return false;
        }

        // Gọi DAO với cả 2 tham số: roomId và userId
        return roomDAO.deleteRoom(roomId, userId); // SỬA: Truyền userId
    }

    // --- Phương thức Cập nhật Trạng thái ---
    // ✅ 6. updateRoomStatus: PHẢI NHẬN userId để kiểm tra quyền
    public boolean updateRoomStatus(int roomId, String newStatus, int userId) {

        if (newStatus == null || newStatus.trim().isEmpty() || roomId <= 0 || userId <= 0) {
            System.err.println("Lỗi nghiệp vụ: Dữ liệu trạng thái, ID phòng hoặc User ID không hợp lệ.");
            return false;
        }

        // Tốt nhất là kiểm tra phòng đó có thuộc về user này không trước khi update
        // SỬA LỖI: Gọi getRoomById(roomId, userId)
        if (getRoomById(roomId, userId) == null) {
            System.err.println("Lỗi bảo mật: Phòng không thuộc về người dùng.");
            return false;
        }

        // Gọi trực tiếp DAO để cập nhật
        return roomDAO.updateRoomStatus(roomId, newStatus);
    }
    public List<Room> getVacantRooms(int userId) {
        return roomDAO.getVacantRooms(userId);
    }
}