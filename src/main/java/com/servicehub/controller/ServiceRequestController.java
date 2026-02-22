package com.servicehub.controller;

import com.servicehub.model.ServiceRequest;
import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.service.ServiceRequestService;
import com.servicehub.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/service-requests")
@CrossOrigin(origins = "*")
public class ServiceRequestController {
    
    @Autowired
    private ServiceRequestService serviceRequestService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    // CREATE SERVICE REQUEST
@PostMapping("/create")
public ResponseEntity<?> createRequest(
        @RequestParam("category") String category,
        @RequestParam("description") String description,
        @RequestParam("address") String address,
        @RequestParam(required = false) String location,
        @RequestParam("image") MultipartFile image,
        HttpSession session) {

    Long customerId = (Long) session.getAttribute("customerId");

    if (customerId == null) {
        return ResponseEntity.status(401)
                .body(Map.of("message", "Unauthorized"));
    }

    try {

        String fileName = fileUploadService.uploadFile(image);

        ServiceRequest request = new ServiceRequest();
        request.setCustomerId(customerId);
        request.setCategory(category);
        request.setDescription(description);
        request.setAddress(address);
        request.setImagePath(fileName);

        // ✅ SAFE LOCATION HANDLING
        if (location != null && !location.trim().isEmpty()
                && !location.equalsIgnoreCase("undefined")) {
            request.setLocation(location);
        } else {
            request.setLocation(null);
        }

        ServiceRequest saved =
                serviceRequestService.createRequest(request);

        return ResponseEntity.ok(Map.of(
                "message", "Service request created successfully",
                "requestId", saved.getId()
        ));

    } catch (IOException e) {

        return ResponseEntity.status(500)
                .body(Map.of("message", "File upload failed"));
    }
}
    
    // GET REQUESTS BY CATEGORY (FOR PROVIDERS)
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getRequestsByCategory(@PathVariable String category) {
        List<ServiceRequestDTO> requests = serviceRequestService.getRequestsDTOByCategory(category);
        return ResponseEntity.ok(requests);
    }
    
    // GET REQUESTS BY CUSTOMER
    @GetMapping("/customer")
    public ResponseEntity<?> getCustomerRequests(HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        
        List<ServiceRequest> requests = serviceRequestService.getRequestsByCustomer(customerId);
        return ResponseEntity.ok(requests);
    }
    
    // GET REQUEST DETAILS
    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestDetails(@PathVariable Long id) {
        ServiceRequest request = serviceRequestService.getRequestById(id);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request);
    }
}