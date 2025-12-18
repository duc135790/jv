package Services;

import dao.PaymentDAO;
import dao.ContractDAO;
import models.Payment;
import java.util.List;
import java.math.BigDecimal;

public class PaymentService {

    private PaymentDAO paymentDAO;
    private ContractDAO contractDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.contractDAO = new ContractDAO();
    }

    // --- Phương thức truy vấn ---

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    // ✅ THÊM IMPLEMENTATION CHO getPaymentById
    public Payment getPaymentById(int paymentId) {
        if (paymentId <= 0) {
            return null;
        }
        return paymentDAO.getPaymentById(paymentId);
    }

    // Phương thức lấy ID hợp đồng đang hoạt động cho UI
    public List<Integer> getAllActiveContractIds() {
        return paymentDAO.getAllContractIds();
    }

    // --- Phương thức Thêm (Bao gồm Logic nghiệp vụ) ---

    public boolean addPayment(Payment payment) {

        // Logic 1: Kiểm tra dữ liệu đầu vào cơ bản
        if (payment == null || payment.getContractId() <= 0 || payment.getAmount() == null
                || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Lỗi nghiệp vụ: ID hợp đồng phải hợp lệ và số tiền phải lớn hơn 0.");
            return false;
        }

        // Logic 2: Kiểm tra Khóa ngoại (Contract ID) có tồn tại và đang hoạt động không
        // Có thể thêm validation nếu cần thiết
        // if (!contractDAO.isContractActive(payment.getContractId())) {
        //      System.err.println("Lỗi nghiệp vụ: Hợp đồng " + payment.getContractId() + " không tồn tại hoặc đã kết thúc.");
        //      return false;
        // }

        // Nếu tất cả kiểm tra pass, gọi DAO
        return paymentDAO.addPayment(payment);
    }

    // --- Phương thức Cập nhật (Bao gồm Logic nghiệp vụ) ---

    public boolean updatePayment(Payment payment) {

        // Logic 1: Kiểm tra Payment có tồn tại không
        if (payment == null || payment.getPaymentId() <= 0) {
            System.err.println("Lỗi nghiệp vụ: Thanh toán không hợp lệ.");
            return false;
        }

        if (getPaymentById(payment.getPaymentId()) == null) {
            System.err.println("Lỗi nghiệp vụ: Giao dịch cần cập nhật không tồn tại.");
            return false;
        }

        // Logic 2: Kiểm tra số tiền hợp lệ
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("Lỗi nghiệp vụ: Số tiền phải lớn hơn 0.");
            return false;
        }

        // Gọi DAO
        return paymentDAO.updatePayment(payment);
    }

    // --- Phương thức Xóa ---

    public boolean deletePayment(int paymentId) {

        // Logic: Kiểm tra thanh toán có tồn tại không
        if (paymentId <= 0) {
            return false;
        }

        if (getPaymentById(paymentId) == null) {
            System.err.println("Lỗi nghiệp vụ: Thanh toán không tồn tại.");
            return false;
        }

        return paymentDAO.deletePayment(paymentId);
    }
}