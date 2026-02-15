package com.servicehub.controller;

import com.servicehub.model.Customer;
import com.servicehub.model.ServiceProvider;
import com.servicehub.service.CustomerService;
import com.servicehub.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ServiceProviderService serviceProviderService;
    
    // CUSTOMER REGISTRATION
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        if (customer.getName() == null || customer.getEmail() == null || 
            customer.getPassword() == null || customer.getPhone() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
        }
        
        Customer registeredCustomer = customerService.registerCustomer(customer);
        if (registeredCustomer == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Customer registered successfully",
            "customerId", registeredCustomer.getId()
        ));
    }
    
    // CUSTOMER LOGIN
    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Customer customer = customerService.loginCustomer(email, password);
        if (customer == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }
        
        session.setAttribute("customerId", customer.getId());
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("userName", customer.getName());
        
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "customerId", customer.getId(),
            "name", customer.getName()
        ));
    }
    
    // SERVICE PROVIDER REGISTRATION
    @PostMapping("/provider/register")
    public ResponseEntity<?> registerProvider(@RequestBody ServiceProvider provider) {
        if (provider.getName() == null || provider.getEmail() == null || 
            provider.getPassword() == null || provider.getPhone() == null ||
            provider.getCategory() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required"));
        }
        
        ServiceProvider registeredProvider = serviceProviderService.registerProvider(provider);
        if (registeredProvider == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Provider registered successfully",
            "providerId", registeredProvider.getId()
        ));
    }
    
    // SERVICE PROVIDER LOGIN
    @PostMapping("/provider/login")
    public ResponseEntity<?> loginProvider(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        ServiceProvider provider = serviceProviderService.loginProvider(email, password);
        if (provider == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }
        
        session.setAttribute("providerId", provider.getId());
        session.setAttribute("userType", "PROVIDER");
        session.setAttribute("userName", provider.getName());
        
     return ResponseEntity.ok(Map.of(
    "message", "Login successful",
    "providerId", provider.getId(),
    "name", provider.getName(),
    "category", provider.getCategory()
));

    }
    
    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}