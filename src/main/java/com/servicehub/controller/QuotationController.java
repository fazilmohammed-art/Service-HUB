package com.servicehub.controller;

import com.servicehub.model.Quotation;
import com.servicehub.service.QuotationService;
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

    // ================= GET QUOTATIONS (DTO) =================
    @GetMapping("/request/{requestId}")
    public ResponseEntity<?> getQuotations(@PathVariable Long requestId) {

        return ResponseEntity.ok(
                quotationService.getByRequestId(requestId));  // DTO
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

        // 🔥 Get all quotation entities
        List<Quotation> allQuotations =
                quotationService.getEntitiesByRequestId(requestId);

        for (Quotation q : allQuotations) {

            if (q.getId().equals(id)) {
                q.setStatus("Accepted");
                quotationService.save(q);
            } else {
                quotationService.delete(q.getId());   // 🔥 DELETE others
            }
        }

        // 🔥 Update request status
        serviceRequestService.updateStatus(requestId, "Confirmed");

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
