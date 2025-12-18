package Controller;

import Services.RoomService;
import Services.TenantService;
import Services.ContractService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import models.Room;
import models.Tenant;
import models.Contract;
import models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "ContractController", urlPatterns = {"/quan-ly-hop-dong"})
public class ContractController extends HttpServlet {

    private static final String CONTRACT_LIST_JSP = "/WEB-INF/views/contract/contract-list.jsp";
    private static final String CONTRACT_FORM_JSP = "/WEB-INF/views/contract/contract-form.jsp";

    private ContractService contractService;
    private RoomService roomService;
    private TenantService tenantService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.contractService = new ContractService();
        this.roomService = new RoomService();
        this.tenantService = new TenantService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }

        String action = request.getParameter("action");
        int userId = user.getId();

        try {
            if (action == null || action.equals("list")) {
                listContracts(request, response, userId);
            } else if (action.equals("showAddForm")) {
                showAddForm(request, response, userId);
            } else if (action.equals("showEditForm")) {
                showEditForm(request, response, userId);
            } else if (action.equals("delete")) {
                // ✅ SỬA: Xử lý DELETE qua GET (khi click link xóa)
                deleteContractViaGet(request, response, userId);
            } else {
                listContracts(request, response, userId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý yêu cầu: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }

        String action = request.getParameter("action");
        int userId = user.getId();

        try {
            switch (action) {
                case "add":
                    insertContract(request, response, userId);
                    break;
                case "update":
                    updateContract(request, response, userId);
                    break;
                case "delete":
                    // POST delete (nếu dùng form)
                    deleteContract(request, response, userId);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/quan-ly-hop-dong");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    // ===== GET HELPERS =====

    private void listContracts(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        List<Contract> contracts = contractService.getAllContracts(userId);
        request.setAttribute("contracts", contracts);
        request.getRequestDispatcher(CONTRACT_LIST_JSP).forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        List<Room> availableRooms = roomService.getVacantRooms(userId);
        request.setAttribute("availableRooms", availableRooms);

        List<Tenant> availableTenants = tenantService.getAllTenants(userId);
        request.setAttribute("availableTenants", availableTenants);

        request.getRequestDispatcher(CONTRACT_FORM_JSP).forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        int contractId = Integer.parseInt(request.getParameter("id"));
        Contract existingContract = contractService.getContractById(contractId, userId);

        if (existingContract == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy hợp đồng.");
            return;
        }

        List<Room> availableRooms = roomService.getVacantRooms(userId);
        List<Tenant> availableTenants = tenantService.getAllTenants(userId);

        request.setAttribute("contract", existingContract);
        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("availableTenants", availableTenants);

        request.getRequestDispatcher(CONTRACT_FORM_JSP).forward(request, response);
    }

    // ✅ XỬ LÝ DELETE QUA GET (Khi click link)
    private void deleteContractViaGet(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("id"));

        boolean success = contractService.deleteContract(contractId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa hợp đồng.");
        } else {
            request.getSession().setAttribute("message", "Xóa hợp đồng thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-hop-dong");
    }

    // ===== POST HELPERS =====

    private void insertContract(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException, ServletException {

        Contract newContract = extractContractData(request, true);
        boolean success = contractService.addContract(newContract, userId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-hop-dong");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Không thể tạo hợp đồng. Kiểm tra phòng và khách thuê.");

            List<Room> availableRooms = roomService.getVacantRooms(userId);
            List<Tenant> availableTenants = tenantService.getAllTenants(userId);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("availableTenants", availableTenants);
            request.setAttribute("contract", newContract);

            request.getRequestDispatcher(CONTRACT_FORM_JSP).forward(request, response);
        }
    }

    private void updateContract(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException, ServletException {

        Contract updatedContract = extractContractData(request, false);
        updatedContract.setUserId(userId);

        boolean success = contractService.updateContract(updatedContract, userId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-hop-dong");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Không thể cập nhật hợp đồng.");
            request.setAttribute("contract", updatedContract);
            request.getRequestDispatcher(CONTRACT_FORM_JSP).forward(request, response);
        }
    }

    private void deleteContract(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("id"));
        boolean success = contractService.deleteContract(contractId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa hợp đồng.");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-hop-dong");
    }

    // ===== UTILITY METHODS =====

    private Contract extractContractData(HttpServletRequest request, boolean isNew) {

        String tenantIdStr = request.getParameter("tenantId");
        String roomIdStr = request.getParameter("roomId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String depositAmountStr = request.getParameter("depositAmount");
        String status = request.getParameter("status");

        Contract contract = new Contract();

        // Parse Tenant ID
        if (tenantIdStr != null && !tenantIdStr.trim().isEmpty()) {
            try {
                contract.setTenantId(Integer.parseInt(tenantIdStr));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Tenant ID: " + tenantIdStr);
            }
        }

        // Parse Room ID
        if (roomIdStr != null && !roomIdStr.trim().isEmpty()) {
            try {
                contract.setRoomId(Integer.parseInt(roomIdStr));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Room ID: " + roomIdStr);
            }
        }

        // Parse Start Date
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            try {
                contract.setStartDate(Date.valueOf(startDateStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi parse Start Date: " + startDateStr);
            }
        }

        // Parse End Date
        if (endDateStr != null && !endDateStr.trim().isEmpty()) {
            try {
                contract.setEndDate(Date.valueOf(endDateStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi parse End Date: " + endDateStr);
            }
        }

        // Parse Deposit Amount
        if (depositAmountStr != null && !depositAmountStr.trim().isEmpty()) {
            try {
                long depositValue = Long.parseLong(depositAmountStr.replaceAll("[^0-9]", ""));
                contract.setDepositAmount(BigDecimal.valueOf(depositValue));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Deposit Amount: " + depositAmountStr);
            }
        }

        // Set Status
        if (status != null && !status.trim().isEmpty()) {
            contract.setStatus(status);
        } else {
            contract.setStatus("Đang thuê");
        }

        // Set Contract ID nếu là update
        if (!isNew) {
            String contractIdStr = request.getParameter("contractId");
            if (contractIdStr != null && !contractIdStr.trim().isEmpty()) {
                try {
                    contract.setContractId(Integer.parseInt(contractIdStr));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ID hợp đồng không hợp lệ.");
                }
            }
        }

        contract.setStaffId(1);

        return contract;
    }
}