package com.servicehub.repository;

import com.servicehub.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderRepository
        extends JpaRepository<ServiceProvider, Long> {

    ServiceProvider findByEmail(String email);
}