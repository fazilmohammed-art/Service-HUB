package com.servicehub.model;

import javax.persistence.*;

@Entity
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serviceRequestId;
    private Long providerId;

    private Double amount;
    private String message;

    private String status = "Pending"; // Pending / Accepted / Rejected
    private boolean notificationShown = false;
    private boolean notificationSeen = false;

    public Long getId() { return id; }

    public Long getServiceRequestId() { return serviceRequestId; }
    public void setServiceRequestId(Long serviceRequestId) { this.serviceRequestId = serviceRequestId; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isNotificationShown() { return notificationShown; }
    public void setNotificationShown(boolean notificationShown) { this.notificationShown = notificationShown; }
    public boolean isNotificationSeen() {
    return notificationSeen;
}

public void setNotificationSeen(boolean notificationSeen) {
    this.notificationSeen = notificationSeen;
}
}
