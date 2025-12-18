package Services;

import dao.StatisticsDAO;
import models.Payment;

import java.math.BigDecimal;
import java.util.*;

public class StatisticsService {

    private StatisticsDAO statisticsDAO;

    public StatisticsService() {
        this.statisticsDAO = new StatisticsDAO();
    }

    /**
     * Lấy thống kê tổng quan
     */
    public Map<String, Object> getOverviewStatistics(int userId) {
        Map<String, Object> overview = new HashMap<>();

        // Tổng số phòng
        overview.put("totalRooms", statisticsDAO.getTotalRooms(userId));

        // Số phòng đang thuê
        overview.put("occupiedRooms", statisticsDAO.getOccupiedRooms(userId));

        // Số phòng trống
        overview.put("vacantRooms", statisticsDAO.getVacantRooms(userId));

        // Tổng doanh thu
        overview.put("totalRevenue", statisticsDAO.getTotalRevenue(userId));

        // Doanh thu tháng này
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        overview.put("currentMonthRevenue", statisticsDAO.getRevenueByMonth(userId, currentYear, currentMonth));

        // Số hợp đồng đang hoạt động
        overview.put("activeContracts", statisticsDAO.getActiveContracts(userId));

        return overview;
    }

    /**
     * Lấy doanh thu theo tháng trong năm
     */
    public Map<Integer, BigDecimal> getMonthlyRevenue(int userId, int year) {
        return statisticsDAO.getMonthlyRevenue(userId, year);
    }

    /**
     * Lấy doanh thu theo ngày trong tháng
     */
    public Map<Integer, BigDecimal> getDailyRevenueByMonth(int userId, int year, int month) {
        return statisticsDAO.getDailyRevenueByMonth(userId, year, month);
    }

    /**
     * Lấy doanh thu theo phòng
     */
    public Map<String, BigDecimal> getRevenueByRoom(int userId) {
        return statisticsDAO.getRevenueByRoom(userId);
    }

    /**
     * Lấy danh sách thanh toán gần đây
     */
    public List<Payment> getRecentPayments(int userId, int limit) {
        return statisticsDAO.getRecentPayments(userId, limit);
    }
}