package Controller;

import jakarta.servlet.http.HttpSession;
import models.Room;
import Services.RoomService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "RoomController", urlPatterns = {"/quan-ly-phong"})
public class RoomController extends HttpServlet {

    private RoomService roomService;
    private static final String ROOM_LIST_JSP = "/WEB-INF/views/room/room-list.jsp";
    private static final String ROOM_FORM_JSP = "/WEB-INF/views/room/room-form.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        this.roomService = new RoomService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int userId = getCurrentUserId(request);
        String contextPath = request.getContextPath();

        // 1. Kiểm tra trạng thái đăng nhập
        if (userId < 0) {
            response.sendRedirect(contextPath + "/auth?action=showLogin");
            return;
        }

        // Đặt hành động mặc định là "list" nếu không có tham số action
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "showAddForm":
                    showAddForm(request, response);
                    break;

                case "showEditForm":
                    showEditForm(request, response, userId);
                    break;

                case "delete":
                    deleteRoomViaGet(request, response, userId);
                    break;

                case "list":
                default:
                    listRooms(request, response, userId);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý yêu cầu GET: " + ex.getMessage());
            listRooms(request, response, userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String contextPath = request.getContextPath();

        if (action == null) {
            response.sendRedirect(contextPath + "/quan-ly-phong");
            return;
        }

        try {
            switch (action) {
                case "add":
                    insertRoom(request, response);
                    break;
                case "update":
                    updateRoom(request, response);
                    break;
                case "delete":
                    deleteRoom(request, response);
                    break;
                default:
                    response.sendRedirect(contextPath + "/quan-ly-phong");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý dữ liệu: " + ex.getMessage());
            int userId = getCurrentUserId(request);
            listRooms(request, response, userId);
        }
    }

    // -------------------------------------------------------------
    // LOGIC XỬ LÝ GET HELPERS
    // -------------------------------------------------------------

    private void listRooms(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        List<Room> listRooms = roomService.getAllRooms(userId);
        request.setAttribute("rooms", listRooms);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ROOM_LIST_JSP);
        dispatcher.forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher(ROOM_FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Room existingRoom = roomService.getRoomById(id, userId);

        if (existingRoom == null) {
            request.setAttribute("errorMessage", "Không tìm thấy phòng hoặc bạn không có quyền truy cập.");
            listRooms(request, response, userId);
            return;
        }

        request.setAttribute("room", existingRoom);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ROOM_FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void deleteRoomViaGet(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int roomId = Integer.parseInt(request.getParameter("id"));

        boolean success = roomService.deleteRoom(roomId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa phòng, có thể phòng đang có hợp đồng thuê.");
        } else {
            request.getSession().setAttribute("message", "Xóa phòng thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-phong");
    }

    // -------------------------------------------------------------
    // LOGIC XỬ LÝ POST (CRUD) HELPERS
    // -------------------------------------------------------------

    private void insertRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int userId = getCurrentUserId(request);
        Room newRoom = extractRoomData(request, true);

        newRoom.setUserId(userId);

        boolean success = roomService.addRoom(newRoom);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-phong");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Số phòng đã tồn tại hoặc dữ liệu không hợp lệ.");
            request.setAttribute("room", newRoom);
            request.getRequestDispatcher(ROOM_FORM_JSP).forward(request, response);
        }
    }

//    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
//            throws IOException, ServletException {
//
//        int userId = getCurrentUserId(request);
//        Room updatedRoom = extractRoomData(request, false);
//
//        updatedRoom.setUserId(userId);
//
//        // Kiểm tra phòng tồn tại
//        Room existingRoom = roomService.getRoomById(updatedRoom.getRoomId(), userId);
//        if (existingRoom == null) {
//            request.setAttribute("errorMessage", "Lỗi: Không tìm thấy phòng hoặc bạn không có quyền chỉnh sửa.");
//            request.setAttribute("room", updatedRoom);
//            request.getRequestDispatcher(ROOM_FORM_JSP).forward(request, response);
//            return;
//        }
//
//        boolean success = roomService.updateRoom(updatedRoom);
//
//        if (success) {
//            response.sendRedirect(request.getContextPath() + "/quan-ly-phong");
//        } else {
//            request.setAttribute("errorMessage", "Lỗi: Không thể cập nhật phòng. Số phòng có thể đã trùng.");
//            request.setAttribute("room", updatedRoom);
//            request.getRequestDispatcher(ROOM_FORM_JSP).forward(request, response);
//        }
//    }
private void updateRoom(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    int userId = getCurrentUserId(request);
    Room updatedRoom = extractRoomData(request, false);
    updatedRoom.setUserId(userId);

    boolean success = roomService.updateRoom(updatedRoom);

    if (success) {
        response.sendRedirect(request.getContextPath() + "/quan-ly-phong?msg=updateSuccess");
    } else {
        Room existingRoom = roomService.getRoomById(updatedRoom.getRoomId(), userId);

        if (existingRoom == null) {
            request.setAttribute("errorMessage", "Không tìm thấy phòng hoặc bạn không có quyền chỉnh sửa.");
        }
        // ĐÃ SỬA: dùng roomService. thay vì RoomService.
        else if (!existingRoom.getRoomNumber().equals(updatedRoom.getRoomNumber()) &&
                roomService.isRoomNumberExists(updatedRoom.getRoomNumber(), userId)) {
            request.setAttribute("errorMessage", "Không thể cập nhật: Số phòng '" + updatedRoom.getRoomNumber() + "' đã tồn tại!");
        }
        else {
            request.setAttribute("errorMessage", "Không thể cập nhật phòng. Vui lòng thử lại.");
        }

        request.setAttribute("room", updatedRoom);
        request.getRequestDispatcher(ROOM_FORM_JSP).forward(request, response);
    }
}

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int userId = getCurrentUserId(request);
        int roomId = Integer.parseInt(request.getParameter("id"));

        boolean success = roomService.deleteRoom(roomId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa phòng, có thể phòng đang có hợp đồng thuê.");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-phong");
    }

    // -------------------------------------------------------------
    // LOGIC TIỆN ÍCH
    // -------------------------------------------------------------

    private Room extractRoomData(HttpServletRequest request, boolean isNew) {

        String roomNumber = request.getParameter("roomNumber");
        String type = request.getParameter("type");

        // 1. Xử lý Price
        String priceStr = request.getParameter("price");
        long priceValue = 0;
        if (priceStr != null && !priceStr.trim().isEmpty()) {
            try {
                priceValue = Long.parseLong(priceStr.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Price: " + priceStr);
            }
        }

        // 2. Xử lý Floor
        String floorStr = request.getParameter("floor");
        int floorValue = 0;
        if (floorStr != null && !floorStr.trim().isEmpty()) {
            try {
                floorValue = Integer.parseInt(floorStr);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Floor: " + floorStr);
            }
        }

        // 3. Xử lý Status và Description
        String status = request.getParameter("status");
        String description = request.getParameter("description");

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setType(type);
        room.setPrice(BigDecimal.valueOf(priceValue));
        room.setStatus(status);
        room.setFloor(floorValue);
        room.setDescription(description);

        // Chỉ đặt RoomId nếu là thao tác Cập nhật (isNew = false)
        if (!isNew) {
            String roomIdStr = request.getParameter("roomId");
            if (roomIdStr != null && !roomIdStr.trim().isEmpty()) {
                try {
                    room.setRoomId(Integer.parseInt(roomIdStr));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ID phòng không hợp lệ.");
                }
            }
        }

        return room;
    }

    private int getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return user.getId();
        }
        return -1;
    }

}