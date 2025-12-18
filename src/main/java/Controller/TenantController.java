package Controller;

import Services.TenantService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Tenant;
import models.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TenantController", urlPatterns = {"/quan-ly-khach-thue"})
public class TenantController extends HttpServlet {

    private TenantService tenantService;
    private static final String TENANT_LIST_JSP = "/WEB-INF/views/tenant/tenant-list.jsp";
    private static final String TENANT_FORM_JSP = "/WEB-INF/views/tenant/tenant-form.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        this.tenantService = new TenantService();
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
                    showEditForm(request, response, userId);
                    break;

                case "delete":
                    deleteTenantViaGet(request, response, userId);
                    break;

                case "list":
                default:
                    listTenants(request, response, userId);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý yêu cầu GET: " + ex.getMessage());
            listTenants(request, response, userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String contextPath = request.getContextPath();

        if (action == null) {
            response.sendRedirect(contextPath + "/quan-ly-khach-thue");
            return;
        }

        try {
            switch (action) {
                case "add":
                    insertTenant(request, response);
                    break;
                case "update":
                    updateTenant(request, response);
                    break;
                case "delete":
                    deleteTenant(request, response);
                    break;
                default:
                    response.sendRedirect(contextPath + "/quan-ly-khach-thue");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + ex.getMessage());
            int userId = getCurrentUserId(request);
            listTenants(request, response, userId);
        }
    }

    // ===== GET HELPERS =====

    private void listTenants(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        List<Tenant> listTenants = tenantService.getAllTenants(userId);
        request.setAttribute("tenants", listTenants);

        RequestDispatcher dispatcher = request.getRequestDispatcher(TENANT_LIST_JSP);
        dispatcher.forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher(TENANT_FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Tenant existingTenant = tenantService.getTenantById(id, userId);

        if (existingTenant == null) {
            request.setAttribute("errorMessage", "Không tìm thấy khách thuê hoặc bạn không có quyền truy cập.");
            listTenants(request, response, userId);
            return;
        }

        request.setAttribute("tenant", existingTenant);
        RequestDispatcher dispatcher = request.getRequestDispatcher(TENANT_FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void deleteTenantViaGet(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int tenantId = Integer.parseInt(request.getParameter("id"));

        boolean success = tenantService.deleteTenant(tenantId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa khách thuê, có thể đang có hợp đồng.");
        } else {
            request.getSession().setAttribute("message", "Xóa khách thuê thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-khach-thue");
    }

    // ===== POST HELPERS =====

    private void insertTenant(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int userId = getCurrentUserId(request);
        Tenant newTenant = extractTenantData(request, true);

        System.out.println("DEBUG - Thêm khách: " + newTenant.getName() + ", CMND: " + newTenant.getIdCard() + ", Phone: " + newTenant.getPhone());

        boolean success = tenantService.addTenant(newTenant, userId);

        if (success) {
            System.out.println("✅ Thêm khách thuê thành công!");
            response.sendRedirect(request.getContextPath() + "/quan-ly-khach-thue");
        } else {
            System.out.println("❌ Thêm khách thuê thất bại!");
            request.setAttribute("errorMessage", "Lỗi: CMND/CCCD hoặc số điện thoại đã tồn tại.");
            request.setAttribute("tenant", newTenant);
            request.getRequestDispatcher(TENANT_FORM_JSP).forward(request, response);
        }
    }

    private void updateTenant(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int userId = getCurrentUserId(request);
        Tenant updatedTenant = extractTenantData(request, false);

        boolean success = tenantService.updateTenant(updatedTenant, userId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/quan-ly-khach-thue");
        } else {
            request.setAttribute("errorMessage", "Lỗi: Không thể cập nhật khách thuê.");
            request.setAttribute("tenant", updatedTenant);
            request.getRequestDispatcher(TENANT_FORM_JSP).forward(request, response);
        }
    }

    private void deleteTenant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int userId = getCurrentUserId(request);
        int tenantId = Integer.parseInt(request.getParameter("id"));

        boolean success = tenantService.deleteTenant(tenantId, userId);

        if (!success) {
            request.getSession().setAttribute("message", "Lỗi: Không thể xóa khách thuê, có thể đang có hợp đồng.");
        }

        response.sendRedirect(request.getContextPath() + "/quan-ly-khach-thue");
    }

    // ===== UTILITY METHODS =====

    private Tenant extractTenantData(HttpServletRequest request, boolean isNew) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String idCard = request.getParameter("idCard");
        String address = request.getParameter("address");

        Tenant tenant = new Tenant();
        tenant.setName(name != null ? name.trim() : "");
        tenant.setPhone(phone != null ? phone.trim() : "");
        tenant.setEmail(email != null ? email.trim() : "");
        tenant.setIdCard(idCard != null ? idCard.trim() : "");
        tenant.setAddress(address != null ? address.trim() : "");

        if (!isNew) {
            String tenantIdStr = request.getParameter("tenantId");
            if (tenantIdStr != null && !tenantIdStr.trim().isEmpty()) {
                try {
                    tenant.setTenantId(Integer.parseInt(tenantIdStr));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("ID khách thuê không hợp lệ.");
                }
            }
        }

        return tenant;
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