package Controller;

import Services.PaymentService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Payment;
import models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "PaymentController", urlPatterns = {"/quan-ly-thanh-toan"})
public class PaymentController extends HttpServlet {

    private PaymentService paymentService;
    private static final String PAYMENT_LIST_JSP = "/WEB-INF/views/payment/payment-list.jsp";
    private static final String PAYMENT_FORM_JSP = "/WEB-INF/views/payment/payment-form.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        this.paymentService = new PaymentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int userId = getCurrentUserId(request);
        String contextPath = request.getContextPath();

        // Kiểm tra đăng nhập
        if (userId < 0) {
            response.sendRedirect(contextPath + "/auth?action=showLogin");
            return;
        }

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "showAddForm":
                    showAddForm(request, response);
                    break;

                case "showEditForm":
                    showEditForm(request, response);
                    break;

                case "delete":
                    // ✅ XỬ LÝ DELETE QUA GET (Khi click link xóa)
                    deletePaymentViaGet(request, response);
                    break;

                case "list":
                default:
                    listPayments(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý yêu cầu GET: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String contextPath = request.getContextPath();

        if (action == null) {
            response.sendRedirect(contextPath + "/quan-ly-thanh-toan");
            return;
        }

        try {
            switch (action) {
                case "add":
                    insertPayment(request, response);
                    break;
                case "update":
                    updatePayment(request, response);
                    break;
                case "delete":
                    // POST delete (ít dùng)
                    deletePayment(request, response);
                    break;
                default:
                    response.sendRedirect(contextPath + "/quan-ly-thanh-toan");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    // ===== GET HELPERS =====

    private void listPayments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Payment> listPayments = paymentService.getAllPayments();
        request.setAttribute("payments", listPayments);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PAYMENT_LIST_JSP);
        dispatcher.forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Integer> contractIds = paymentService.getAllActiveContractIds();
        request.setAttribute("contractIds", contractIds);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PAYMENT_FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Payment existingPayment = paymentService.getPaymentById(id);

        if (existingPayment == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy thanh toán.");
            return;
        }

        List<Integer> contractIds = paymentService.getAllActiveContractIds();
        request.setAttribute("contractIds", contractIds);
        request.setAttribute("payment", existingPayment);

        RequestDispatcher dispatcher = request.getRequestDispatcher(PAYMENT_FORM_JSP);
        dispatcher.forward(request, response);
    }

    // ✅ XỬ LÝ DELETE QUA GET (Khi click link xóa)
    private void deletePaymentViaGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int paymentId = Integer.parseInt(request.getParameter("id"));
        boolean success = paymentService.deletePayment(paymentId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa thanh toán.");
        } else {
            request.getSession().setAttribute("message", "Xóa thanh toán thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-thanh-toan");
    }

    // ===== POST HELPERS =====

    private void insertPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Payment newPayment = extractPaymentData(request, true);
        boolean success = paymentService.addPayment(newPayment);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-thanh-toan");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Không thể thêm thanh toán.");
            request.setAttribute("payment", newPayment);
            request.getRequestDispatcher(PAYMENT_FORM_JSP).forward(request, response);
        }
    }

    private void updatePayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Payment updatedPayment = extractPaymentData(request, false);
        boolean success = paymentService.updatePayment(updatedPayment);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-thanh-toan");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Không thể cập nhật thanh toán.");
            request.setAttribute("payment", updatedPayment);
            request.getRequestDispatcher(PAYMENT_FORM_JSP).forward(request, response);
        }
    }

    private void deletePayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int paymentId = Integer.parseInt(request.getParameter("id"));
        boolean success = paymentService.deletePayment(paymentId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa thanh toán.");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-thanh-toan");
    }

    // ===== UTILITY METHODS =====

    private Payment extractPaymentData(HttpServletRequest request, boolean isNew) {
        String contractIdStr = request.getParameter("contractId");
        String amountStr = request.getParameter("amount");
        String paymentDateStr = request.getParameter("paymentDate");
        String method = request.getParameter("method");
        String description = request.getParameter("description");

        Payment payment = new Payment();

        // Parse Contract ID
        if (contractIdStr != null && !contractIdStr.trim().isEmpty()) {
            try {
                payment.setContractId(Integer.parseInt(contractIdStr));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Contract ID: " + contractIdStr);
            }
        }

        // Parse Amount
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                long amountValue = Long.parseLong(amountStr.replaceAll("[^0-9]", ""));
                payment.setAmount(BigDecimal.valueOf(amountValue));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Amount: " + amountStr);
            }
        }

        // Parse Payment Date
        if (paymentDateStr != null && !paymentDateStr.trim().isEmpty()) {
            try {
                payment.setPaymentDate(Date.valueOf(paymentDateStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi parse Payment Date: " + paymentDateStr);
            }
        }

        payment.setMethod(method);
        payment.setDescription(description);

        if (!isNew) {
            String paymentIdStr = request.getParameter("paymentId");
            if (paymentIdStr != null && !paymentIdStr.trim().isEmpty()) {
                try {
                    payment.setPaymentId(Integer.parseInt(paymentIdStr));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ID thanh toán không hợp lệ.");
                }
            }
        }

        return payment;
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