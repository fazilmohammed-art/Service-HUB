package com.servicehub.model;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String password;
    private String category;
    private String otp;
private LocalDateTime otpTime;

    // Getters & Setters

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getOtp() {
    return otp;
}

public void setOtp(String otp) {
    this.otp = otp;
}

public LocalDateTime getOtpTime() {
    return otpTime;
}

public void setOtpTime(LocalDateTime otpTime) {
    this.otpTime = otpTime;
}
}
