package models;

import java.math.BigDecimal;
import java.sql.Date;

public class Payment {
    private int paymentId;
    private int contractId;
    private BigDecimal amount;
    private Date paymentDate;
    private String method;
    private String description;

    // Constructors (Giá»¯ nguyÃªn)
    public Payment() {}
    public Payment(int paymentId, int contractId, BigDecimal amount, Date paymentDate, String method, String description) {
        this.paymentId = paymentId;
        this.contractId = contractId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.description = description;
    }

    // Getters and Setters... (ÄÃ£ rÃºt gá»n)
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}