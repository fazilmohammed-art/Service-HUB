package com.servicehub.controller;

import com.servicehub.model.Quotation;
import com.servicehub.model.ServiceProvider;
import com.servicehub.model.ServiceRequest;
import com.servicehub.service.EmailService;
import com.servicehub.service.QuotationService;
import com.servicehub.service.ServiceProviderService;
import com.servicehub.service.ServiceRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "*")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private EmailService emailService;

    // ================= ADD QUOTATION =================
    @PostMapping("/add")
    public ResponseEntity<?> addQuotation(@RequestBody Quotation quotation,
                                          HttpSession session) {

        Long providerId = (Long) session.getAttribute("providerId");

        if (providerId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Unauthorized"));
        }

        quotation.setProviderId(providerId);
        quotation.setStatus("Pending");

        quotationService.save(quotation);

        return ResponseEntity.ok(
                Map.of("message", "Quotation submitted successfully"));
    }

    // ================= GET QUOTATIONS =================
    @GetMapping("/request/{requestId}")
    public ResponseEntity<?> getQuotations(@PathVariable Long requestId) {

        return ResponseEntity.ok(
                quotationService.getByRequestId(requestId));
    }

    // ================= ACCEPT QUOTATION =================
    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptQuotation(@PathVariable Long id) {

        Quotation selected = quotationService.getById(id);

        if (selected == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Quotation not found"));
        }

        Long requestId = selected.getServiceRequestId();

        // 🔥 Get ServiceRequest using YOUR method
        ServiceRequest request =
                serviceRequestService.getRequestById(requestId);

        if (request == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Service request not found"));
        }

        // 🔥 Get all quotations for this request
        List<Quotation> allQuotations =
                quotationService.getEntitiesByRequestId(requestId);

        for (Quotation q : allQuotations) {

            if (q.getId().equals(id)) {
                q.setStatus("Accepted");
            } else {
                q.setStatus("Rejected");
            }

            quotationService.save(q);
        }

        // 🔥 Update service request status using YOUR method
        serviceRequestService.updateStatus(requestId, "Confirmed");

        // 🔥 Send Email to Accepted Provider
        ServiceProvider provider =
                serviceProviderService.getById(selected.getProviderId());

        if (provider != null) {

            String message =
                    "Hello " + provider.getName() + ",\n\n" +
                    "🎉 Your quotation has been ACCEPTED!\n\n" +
                    "Service Details:\n" +
                    "Category: " + request.getCategory() + "\n" +
                    "Description: " + request.getDescription() + "\n" +
                    "Address: " + request.getAddress() + "\n\n" +
                    "Please contact the customer to proceed.\n\n" +
                    "Thank you,\nServiceHub";

            emailService.sendSimpleMessage(
                    provider.getEmail(),
                    "ServiceHub - Quotation Accepted",
                    message
            );
        }

        return ResponseEntity.ok(
                Map.of("message", "Quotation accepted successfully"));
    }

    // ================= REJECT QUOTATION =================
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectQuotation(@PathVariable Long id) {

        Quotation quotation = quotationService.getById(id);

        if (quotation == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Quotation not found"));
        }

        quotation.setStatus("Rejected");
        quotationService.save(quotation);

        return ResponseEntity.ok(
                Map.of("message", "Quotation rejected"));
    }

    // ================= PROVIDER QUOTATIONS =================
    @GetMapping("/provider")
    public ResponseEntity<?> getProviderQuotations(HttpSession session){

        Long providerId = (Long) session.getAttribute("providerId");

        if(providerId == null){
            return ResponseEntity.status(401)
                    .body(Map.of("message","Unauthorized"));
        }

        return ResponseEntity.ok(
                quotationService.getByProvider(providerId)
        );
    }
}