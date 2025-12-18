package Services;

import dao.TenantDAO;
import models.Tenant;
import java.util.List;
import dao.ContractDAO; // Cần thiết để kiểm tra hợp đồng trước khi xóa

public class TenantService {

    private TenantDAO tenantDAO;
    private ContractDAO contractDAO; // ✅ Bổ sung ContractDAO để kiểm tra ràng buộc

    public TenantService() {
        this.tenantDAO = new TenantDAO();
        this.contractDAO = new ContractDAO(); // Khởi tạo
    }

    // --- Phương thức truy vấn ---

    // ✅ Thêm userId để lấy danh sách khách thuê của người dùng hiện tại
    public List<Tenant> getAllTenants(int userId) {
        return tenantDAO.getAllTenants(userId);
    }

    // ✅ Phương thức này đã đúng
    public Tenant getTenantById(int tenantId, int userId) {
        if (tenantId <= 0 || userId <= 0) return null;
        return tenantDAO.getTenantById(tenantId, userId);
    }

    // --- Phương thức Thêm (Bao gồm Logic nghiệp vụ) ---

    // ✅ Thêm userId vào chữ ký
    public boolean addTenant(Tenant tenant, int userId) {

        // Logic 1: Kiểm tra dữ liệu đầu vào cơ bản
        if (tenant == null || tenant.getName() == null || tenant.getIdCard() == null
                || tenant.getName().trim().isEmpty() || tenant.getIdCard().trim().isEmpty()) {
            System.err.println("Lỗi nghiệp vụ: Tên và CMND/CCCD không được để trống.");
            return false;
        }

        // ✅ BƯỚC QUAN TRỌNG: Gán userId cho đối tượng Tenant
        tenant.setUserId(userId);

        // Logic 2: Kiểm tra trùng lặp CMND/CCCD (Lọc theo userId)
        // ✅ SỬA LỖI: Truyền userId vào
        if (tenantDAO.isIdCardExists(tenant.getIdCard(), userId)) {
            System.err.println("Lỗi nghiệp vụ: CMND/CCCD " + tenant.getIdCard() + " đã tồn tại trong hệ thống của bạn.");
            return false;
        }

        // Logic 3: Kiểm tra trùng lặp Số điện thoại (Lọc theo userId)
        // ✅ SỬA LỖI: Truyền userId vào
        if (tenant.getPhone() != null && !tenant.getPhone().trim().isEmpty() && tenantDAO.isPhoneExists(tenant.getPhone(), userId)) {
            System.err.println("Lỗi nghiệp vụ: Số điện thoại " + tenant.getPhone() + " đã được sử dụng.");
            return false;
        }

        // Nếu tất cả kiểm tra pass, gọi DAO
        return tenantDAO.addTenant(tenant); // DAO sẽ dùng userId đã được gán trong object Tenant
    }

    // --- Phương thức Cập nhật (Bao gồm Logic nghiệp vụ) ---

    // ✅ Thêm userId vào chữ ký
    public boolean updateTenant(Tenant tenant, int userId) {

        // ✅ BƯỚC QUAN TRỌNG: Gán userId cho đối tượng Tenant
        tenant.setUserId(userId);

        // Logic 1: Đảm bảo khách thuê tồn tại VÀ THUỘC VỀ USER NÀY
        // ✅ SỬA LỖI: Truyền userId vào
        if (tenantDAO.getTenantById(tenant.getTenantId(), userId) == null) {
            System.err.println("Lỗi nghiệp vụ: Khách thuê cần cập nhật không tồn tại hoặc không thuộc về bạn.");
            return false;
        }

        // Logic 2: Kiểm tra trùng lặp CMND/CCCD (trừ chính khách thuê đang cập nhật) (Lọc theo userId)
        // ✅ SỬA LỖI: Truyền userId vào
        if (tenantDAO.isIdCardExistsExcludingId(tenant.getIdCard(), tenant.getTenantId(), userId)) {
            System.err.println("Lỗi nghiệp vụ: CMND/CCCD mới đã bị trùng với khách thuê khác của bạn.");
            return false;
        }

        // Gọi DAO
        return tenantDAO.updateTenant(tenant); // DAO sẽ dùng userId đã được gán
    }

    // --- Phương thức Xóa ---

    // ✅ Thêm userId vào chữ ký
    public boolean deleteTenant(int tenantId, int userId) {

        // Logic 1: Kiểm tra xem khách thuê có hợp đồng thuê phòng nào đang hoạt động không
        // Cần đảm bảo ContractDAO có phương thức kiểm tra hợp đồng theo tenantId và userId
        // Giả sử ContractDAO có hasActiveContractForTenant(tenantId, userId)

        // ✅ THÊM LOGIC KIỂM TRA RÀNG BUỘC
        if (contractDAO.hasActiveContractForTenant(tenantId, userId)) {
            System.err.println("Lỗi nghiệp vụ: Khách thuê đang có hợp đồng hoạt động. Vui lòng kết thúc hợp đồng trước khi xóa.");
            return false;
        }

        // Gọi DAO để xóa (DAO đã được sửa để lọc theo userId)
        return tenantDAO.deleteTenant(tenantId, userId);
    }
}