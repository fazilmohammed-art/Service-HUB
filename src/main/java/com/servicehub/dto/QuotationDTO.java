package com.servicehub.dto;

public class QuotationDTO {

    private Long id;
    private Long requestId;
    private Long providerId;
    private Double amount;
    private String message;
    private String status;   // 🔥 ADD THIS

    private String providerName;
    private String providerPhone;
    private String providerCategory;

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }   // 🔥 ADD
    public void setStatus(String status) { this.status = status; }   // 🔥 ADD

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getProviderPhone() { return providerPhone; }
    public void setProviderPhone(String providerPhone) { this.providerPhone = providerPhone; }

    public String getProviderCategory() { return providerCategory; }
    public void setProviderCategory(String providerCategory) { this.providerCategory = providerCategory; }
}
