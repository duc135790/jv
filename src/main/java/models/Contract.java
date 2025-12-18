package models;

import java.math.BigDecimal;
import java.sql.Date;

public class Contract {
    private int contractId;
    private int tenantId;
    private int roomId;
    private int staffId;
    private Date startDate;
    private Date endDate;
    private String status;
    private String roomNumber;
    private String tenantName;
    private BigDecimal depositAmount;
    private int userId;
    // Constructors (Giá»¯ nguyÃªn)
    public Contract() {}
    public Contract(int contractId, int tenantId, int roomId, int staffId, Date startDate, Date endDate, String status, BigDecimal depositAmount) {
        this.contractId = contractId;
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.staffId = staffId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.depositAmount = depositAmount;
    }
    
    // Getters and Setters... (ÄÃ£ rÃºt gá»n)
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getDepositAmount() { return depositAmount; }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}