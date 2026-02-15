package com.servicehub.repository;

import com.servicehub.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByCategoryIgnoreCase(String category);

    List<ServiceRequest> findByCustomerId(Long customerId);
}
