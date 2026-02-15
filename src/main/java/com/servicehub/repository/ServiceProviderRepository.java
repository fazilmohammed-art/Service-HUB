package com.servicehub.repository;

import com.servicehub.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    Optional<ServiceProvider> findByEmail(String email);
}
