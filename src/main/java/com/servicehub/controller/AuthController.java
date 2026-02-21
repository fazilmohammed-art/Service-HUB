package com.servicehub.controller;

import com.servicehub.util.OtpUtil;
import com.servicehub.model.Customer;
import com.servicehub.model.ServiceProvider;
import com.servicehub.service.CustomerService;
import com.servicehub.service.EmailService;
import com.servicehub.service.ServiceProviderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private EmailService emailService;

    // ================= CUSTOMER REGISTER =================
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {

        if (customer.getName() == null ||
            customer.getEmail() == null ||
            customer.getPassword() == null ||
            customer.getPhone() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "All fields are required"));
        }

        Customer saved = customerService.registerCustomer(customer);

        if (saved == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already exists"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Customer registered successfully"));
    }

    // ================= CUSTOMER LOGIN =================
    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> body,
                                           HttpSession session) {

        String email = body.get("email");
        String password = body.get("password");

        Customer customer = customerService.loginCustomer(email, password);

        if (customer == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid credentials"));
        }

        session.setAttribute("customerId", customer.getId());
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("userName", customer.getName());

        return ResponseEntity.ok(
                Map.of("message", "Login successful",
                       "name", customer.getName()));
    }

    // ================= PROVIDER REGISTER =================
    @PostMapping("/provider/register")
    public ResponseEntity<?> registerProvider(@RequestBody ServiceProvider provider) {

        if (provider.getName() == null ||
            provider.getEmail() == null ||
            provider.getPassword() == null ||
            provider.getPhone() == null ||
            provider.getCategory() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "All fields are required"));
        }

        ServiceProvider saved = serviceProviderService.registerProvider(provider);

        if (saved == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already exists"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Provider registered successfully"));
    }

    // ================= PROVIDER LOGIN (STEP 1 - SEND OTP) =================
    @PostMapping("/provider/login")
    public ResponseEntity<?> loginProvider(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        ServiceProvider provider =
                serviceProviderService.loginProvider(email, password);

        if (provider == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid credentials"));
        }

        // 🔥 Generate OTP
        String otp = OtpUtil.generateOtp();

        provider.setOtp(otp);
        provider.setOtpTime(LocalDateTime.now());

        serviceProviderService.save(provider);

        // 🔥 Send Email
        emailService.sendOtp(provider.getEmail(), otp);

        return ResponseEntity.ok(
                Map.of("message", "OTP sent to your email",
                       "email", provider.getEmail()));
    }

    // ================= PROVIDER VERIFY OTP (STEP 2 - LOGIN SUCCESS) =================
    @PostMapping("/provider/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body,
                                       HttpSession session) {

        String email = body.get("email");
        String otp = body.get("otp");

        ServiceProvider provider =
                serviceProviderService.findByEmail(email);

        if (provider == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Provider not found"));
        }

        if (provider.getOtp() == null ||
            !provider.getOtp().equals(otp)) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid OTP"));
        }

        // 🔥 Check Expiry (5 minutes)
        if (provider.getOtpTime()
                .isBefore(LocalDateTime.now().minusMinutes(5))) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "OTP expired"));
        }

        // 🔥 Clear OTP
        provider.setOtp(null);
        serviceProviderService.save(provider);

        // 🔥 Now set session (Login success)
        session.setAttribute("providerId", provider.getId());
        session.setAttribute("userType", "PROVIDER");
        session.setAttribute("userName", provider.getName());

        return ResponseEntity.ok(
                Map.of("message", "Login successful",
                       "name", provider.getName(),
                       "category", provider.getCategory()));
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();
        return ResponseEntity.ok(
                Map.of("message", "Logout successful"));
    }
}