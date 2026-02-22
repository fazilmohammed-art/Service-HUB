package com.servicehub.service;

import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.model.ServiceRequest;
import com.servicehub.model.Customer;
import com.servicehub.repository.ServiceRequestRepository;
import com.servicehub.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ================= CREATE REQUEST =================
    public ServiceRequest createRequest(ServiceRequest request) {

        // Default status
        request.setStatus("Pending");

        return serviceRequestRepository.save(request);
    }

    // ================= CUSTOMER REQUESTS =================
    public List<ServiceRequest> getRequestsByCustomer(Long customerId) {
        return serviceRequestRepository.findByCustomerId(customerId);
    }

    // ================= PROVIDER REQUESTS =================
    public List<ServiceRequest> getRequestsByCategory(String category) {
        return serviceRequestRepository.findByCategoryIgnoreCase(category);
    }

    // ================= PROVIDER DTO VERSION =================
    public List<ServiceRequestDTO> getRequestsDTOByCategory(String category) {

        List<ServiceRequest> requests =
                serviceRequestRepository.findByCategoryIgnoreCase(category);

        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    public ServiceRequest getRequestById(Long id) {
        return serviceRequestRepository.findById(id).orElse(null);
    }

    // ================= UPDATE STATUS =================
    public void updateStatus(Long requestId, String status) {

        ServiceRequest request =
                serviceRequestRepository.findById(requestId).orElse(null);

        if (request != null) {
            request.setStatus(status);
            serviceRequestRepository.save(request);
        }
    }

    // ================= DTO CONVERSION =================
    private ServiceRequestDTO convertToDTO(ServiceRequest request) {

        Customer customer =
                customerRepository.findById(request.getCustomerId()).orElse(null);

        ServiceRequestDTO dto = new ServiceRequestDTO();

        dto.setId(request.getId());
        dto.setCustomerId(request.getCustomerId());
        dto.setCategory(request.getCategory());
        dto.setDescription(request.getDescription());
        dto.setImagePath(request.getImagePath());
        dto.setAddress(request.getAddress());
        dto.setStatus(request.getStatus());
        dto.setLocation(request.getLocation());

        if (customer != null) {
            dto.setCustomerName(customer.getName());
            dto.setCustomerPhone(customer.getPhone());
        }

        return dto;
    }
}
