package Controller;

import Services.StatisticsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StatisticsController", urlPatterns = {"/thong-ke"})
public class StatisticsController extends HttpServlet {

    private static final String STATISTICS_JSP = "/WEB-INF/views/statistics/statistics.jsp";
    private StatisticsService statisticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.statisticsService = new StatisticsService();
        System.out.println("✅ StatisticsController đã khởi tạo");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra đăng nhập
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }

        // 2. Kiểm tra quyền Admin
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            request.setAttribute("errorMessage", "Bạn không có quyền truy cập. Chỉ Admin mới xem được Thống Kê.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        int userId = user.getId();
        System.out.println("📊 Admin " + user.getUsername() + " truy cập Thống Kê");

        try {
            // 3. Lấy tham số năm và tháng
            String yearStr = request.getParameter("year");
            int year = (yearStr != null && !yearStr.isEmpty())
                    ? Integer.parseInt(yearStr)
                    : java.time.Year.now().getValue();

            String monthStr = request.getParameter("month");
            Integer month = (monthStr != null && !monthStr.isEmpty())
                    ? Integer.parseInt(monthStr)
                    : null;

            // 4. Lấy thống kê tổng quan
            Map<String, Object> overview = statisticsService.getOverviewStatistics(userId);
            request.setAttribute("overview", overview);
            System.out.println("✅ Overview loaded: " + overview.size() + " items");

            // 5. Lấy doanh thu theo thời gian
            Map<Integer, BigDecimal> monthlyRevenue;
            if (month != null) {
                monthlyRevenue = statisticsService.getDailyRevenueByMonth(userId, year, month);
                request.setAttribute("viewType", "daily");
                request.setAttribute("selectedMonth", month);
                System.out.println("📈 Daily revenue for " + month + "/" + year);
            } else {
                monthlyRevenue = statisticsService.getMonthlyRevenue(userId, year);
                request.setAttribute("viewType", "monthly");
                System.out.println("📈 Monthly revenue for " + year);
            }

            request.setAttribute("monthlyRevenue", monthlyRevenue);
            request.setAttribute("selectedYear", year);

            // 6. Lấy thống kê theo phòng
            Map<String, BigDecimal> roomRevenue = statisticsService.getRevenueByRoom(userId);
            request.setAttribute("roomRevenue", roomRevenue);
            System.out.println("🏠 Room revenue: " + roomRevenue.size() + " rooms");

            // 7. Lấy thanh toán gần đây
            request.setAttribute("recentPayments", statisticsService.getRecentPayments(userId, 5));
            System.out.println("💳 Recent payments loaded");

            // ✅ 8. LẤY DANH SÁCH TÀI KHOẢN NGƯỜI DÙNG
            List<User> allUsers = statisticsService.getAllUsers();
            request.setAttribute("allUsers", allUsers);
            System.out.println("👥 Total users in system: " + allUsers.size());

            // ✅ 9. LẤY THỐNG KÊ THEO ROLE
            Map<String, Integer> userCountByRole = statisticsService.getUserCountByRole();
            request.setAttribute("userCountByRole", userCountByRole);
            System.out.println("📊 User count by role: " + userCountByRole);

            // ✅ 10. TỔNG SỐ USER
            int totalUsers = statisticsService.getTotalUsers();
            request.setAttribute("totalUsers", totalUsers);
            System.out.println("📊 Total users: " + totalUsers);

            // 11. Forward đến JSP
            System.out.println("✅ Forwarding to statistics.jsp");
            request.getRequestDispatcher(STATISTICS_JSP).forward(request, response);

        } catch (Exception ex) {
            System.err.println("❌ ERROR: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý thống kê: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}