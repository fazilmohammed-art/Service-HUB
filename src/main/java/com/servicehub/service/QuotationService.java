package com.servicehub.service;

import com.servicehub.model.Quotation;
import com.servicehub.model.ServiceProvider;
import com.servicehub.dto.QuotationDTO;
import com.servicehub.repository.QuotationRepository;
import com.servicehub.repository.ServiceProviderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private ServiceProviderRepository providerRepository;

    // ================= SAVE QUOTATION =================
    public Quotation save(Quotation quotation) {
        return quotationRepository.save(quotation);
    }

    // ================= DELETE QUOTATION =================
    public void delete(Long id) {
        quotationRepository.deleteById(id);
    }

    // ================= GET ENTITY VERSION (For Accept Logic) =================
    public List<Quotation> getEntitiesByRequestId(Long requestId) {
        return quotationRepository.findByServiceRequestId(requestId);
    }

    // ================= GET DTO VERSION (For Customer Dashboard) =================
    public List<QuotationDTO> getByRequestId(Long requestId) {

        List<Quotation> quotations =
                quotationRepository.findByServiceRequestId(requestId);

        return quotations.stream().map(q -> {

            ServiceProvider provider =
                    providerRepository.findById(q.getProviderId()).orElse(null);

            QuotationDTO dto = new QuotationDTO();

            dto.setId(q.getId());
            dto.setRequestId(q.getServiceRequestId());
            dto.setProviderId(q.getProviderId());
            dto.setAmount(q.getAmount());
            dto.setMessage(q.getMessage());
            dto.setStatus(q.getStatus());   // VERY IMPORTANT

            if (provider != null) {
                dto.setProviderName(provider.getName());
                dto.setProviderPhone(provider.getPhone());
                dto.setProviderCategory(provider.getCategory());
            }

            return dto;

        }).collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    public Quotation getById(Long id) {
        return quotationRepository.findById(id).orElse(null);
    }

    // ================= PROVIDER DASHBOARD (DTO VERSION) =================
    public List<QuotationDTO> getByProvider(Long providerId) {

        List<Quotation> quotations =
                quotationRepository.findByProviderId(providerId);

        return quotations.stream().map(q -> {

            QuotationDTO dto = new QuotationDTO();

            dto.setId(q.getId());
            dto.setRequestId(q.getServiceRequestId());
            dto.setProviderId(q.getProviderId());
            dto.setAmount(q.getAmount());
            dto.setMessage(q.getMessage());
            dto.setStatus(q.getStatus());

            return dto;

        }).collect(Collectors.toList());
    }
    public List<Quotation> getAcceptedByProvider(Long providerId){
    return quotationRepository
            .findByProviderIdAndStatus(providerId, "Accepted");
}
public List<Quotation> getNewAcceptedNotifications(Long providerId){
    return quotationRepository
            .findByProviderIdAndStatusAndNotificationSeenFalse(
                    providerId, "Accepted");
}
}
