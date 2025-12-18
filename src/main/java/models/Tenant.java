package models;

public class Tenant {
    private int tenantId;
    private String name;
    private String phone;
    private String email;
    private String idCard;
    private String address;

    // ✅ BỔ SUNG: Thuộc tính user_id để lưu ID người quản lý
    private int userId;

    public Tenant() {}

    // Constructor đầy đủ (6 tham số)
    // Cần cập nhật constructor này nếu bạn lấy dữ liệu từ DB (DB thường có 7 trường)
    public Tenant(int tenantId, String name, String phone, String email, String idCard, String address) {
        this.tenantId = tenantId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.idCard = idCard;
        this.address = address;
        // userId sẽ được gán sau khi đọc từ DB hoặc trong Controller/Service
    }

    // ✅ BỔ SUNG: Constructor đầy đủ 7 tham số (tốt nhất cho việc đọc từ DB)
    public Tenant(int tenantId, String name, String phone, String email, String idCard, String address, int userId) {
        this.tenantId = tenantId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.idCard = idCard;
        this.address = address;
        this.userId = userId;
    }

    // <<< CONSTRUCTOR KHẮC PHỤC LỖI (5 tham số, dùng cho Thêm mới) >>>
    public Tenant(String name, String phone, String email, String idCard, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.idCard = idCard;
        this.address = address;
        // userId sẽ được gán sau bằng setUserId() trong Controller/Service
    }

    // Getters and Setters...
    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // ✅ BỔ SUNG: Getter và Setter cho userId
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}