package com.servicehub.service;

import com.servicehub.model.ServiceProvider;
import com.servicehub.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceProviderService {

    @Autowired
    private ServiceProviderRepository repository;

    public ServiceProvider registerProvider(ServiceProvider provider) {
        return repository.save(provider);
    }

    public ServiceProvider loginProvider(String email, String password) {
        Optional<ServiceProvider> provider = repository.findByEmail(email);
        if (provider.isPresent() && provider.get().getPassword().equals(password)) {
            return provider.get();
        }
        return null;
    }
}
