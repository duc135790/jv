package Services;

import dao.ContractDAO;
import models.Contract;
import java.sql.Date;
import java.util.List;

public class ContractService {

    private ContractDAO contractDAO;
    private RoomService roomService;
    private TenantService tenantService;

    public ContractService() {
        this.contractDAO = new ContractDAO();
        this.roomService = new RoomService();
        this.tenantService = new TenantService();
    }

    // --- Phương thức truy vấn ---

    public List<Contract> getAllContracts(int userId) {
        return contractDAO.getAllContracts(userId);
    }

    public Contract getContractById(int contractId, int userId) {
        if (contractId <= 0 || userId <= 0) return null;
        return contractDAO.getContractById(contractId, userId);
    }

    // --- Nghiệp vụ: Thêm Hợp đồng ---

    public boolean addContract(Contract contract, int userId) {

        // Logic 1: Kiểm tra tính hợp lệ
        if (contract == null || contract.getStartDate() == null || contract.getEndDate() == null) {
            System.err.println("Lỗi: Ngày bắt đầu/kết thúc không được để trống.");
            return false;
        }

        contract.setUserId(userId);

        // Logic 2: Kiểm tra khách thuê và phòng tồn tại
        if (tenantService.getTenantById(contract.getTenantId(), userId) == null ||
                roomService.getRoomById(contract.getRoomId(), userId) == null) {
            System.err.println("Lỗi: Khách thuê hoặc Phòng không tồn tại.");
            return false;
        }

        // Logic 3: Kiểm tra phòng đang trống
        if (contractDAO.hasActiveContractForRoom(contract.getRoomId(), userId)) {
            System.err.println("Lỗi: Phòng đang có hợp đồng hoạt động.");
            return false;
        }

        // Thêm hợp đồng
        boolean contractAdded = contractDAO.addContract(contract);

        if (contractAdded) {
            // Cập nhật trạng thái phòng sang 'Đang thuê'
            boolean roomStatusUpdated = roomService.updateRoomStatus(contract.getRoomId(), "Đang thuê", userId);

            if (roomStatusUpdated) {
                return true;
            } else {
                System.err.println("Lỗi: Thêm hợp đồng thành công nhưng cập nhật phòng thất bại.");
                return false;
            }
        }
        return false;
    }

    // --- Nghiệp vụ: Cập nhật Hợp đồng ---

    public boolean updateContract(Contract contract, int userId) {

        if (contract == null || contract.getContractId() <= 0) {
            System.err.println("Lỗi: Hợp đồng không hợp lệ.");
            return false;
        }

        contract.setUserId(userId);

        // Kiểm tra hợp đồng có tồn tại không
        Contract existingContract = getContractById(contract.getContractId(), userId);
        if (existingContract == null) {
            System.err.println("Lỗi: Hợp đồng không tồn tại hoặc không thuộc về bạn.");
            return false;
        }

        // Nếu thay đổi trạng thái sang "Đã kết thúc"
        if ("Đã kết thúc".equals(contract.getStatus()) &&
                !"Đã kết thúc".equals(existingContract.getStatus())) {

            // Cập nhật phòng về trạng thái "Trống"
            roomService.updateRoomStatus(existingContract.getRoomId(), "Trống", userId);
        }

        return contractDAO.updateContract(contract);
    }

    // --- Nghiệp vụ: Xóa Hợp đồng ---

    public boolean deleteContract(int contractId, int userId) {

        Contract contract = getContractById(contractId, userId);

        if (contract == null) {
            System.err.println("Lỗi: Hợp đồng không tồn tại.");
            return false;
        }

        // Xóa hợp đồng
        boolean deleted = contractDAO.deleteContract(contractId, userId);

        if (deleted) {
            // Cập nhật phòng về "Trống"
            roomService.updateRoomStatus(contract.getRoomId(), "Trống", userId);
            return true;
        }

        return false;
    }

    // --- Nghiệp vụ: Kết thúc Hợp đồng ---

    public boolean endContract(int contractId, Date endDate, int userId) {

        Contract contract = getContractById(contractId, userId);

        if (contract == null || !contract.getStatus().equals("Đang thuê")) {
            System.err.println("Lỗi: Hợp đồng không tồn tại hoặc đã kết thúc.");
            return false;
        }

        boolean contractEnded = contractDAO.endContract(contractId, endDate, userId);

        if (contractEnded) {
            boolean roomStatusUpdated = roomService.updateRoomStatus(contract.getRoomId(), "Trống", userId);

            if (roomStatusUpdated) {
                return true;
            } else {
                System.err.println("Lỗi: Kết thúc hợp đồng thành công nhưng cập nhật phòng thất bại.");
                return false;
            }
        }
        return false;
    }
}