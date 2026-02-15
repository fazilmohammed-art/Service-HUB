package com.servicehub.repository;

import com.servicehub.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByServiceRequestId(Long serviceRequestId);
    List<Quotation> findByProviderId(Long providerId);

}
