package com.servicehub.service;

import com.servicehub.model.ServiceProvider;
import com.servicehub.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceProviderService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    // ================= REGISTER =================
    public ServiceProvider registerProvider(ServiceProvider provider) {

        if (serviceProviderRepository.findByEmail(provider.getEmail()) != null) {
            return null;
        }

        return serviceProviderRepository.save(provider);
    }

    // ================= LOGIN =================
    public ServiceProvider loginProvider(String email, String password) {

        ServiceProvider provider =
                serviceProviderRepository.findByEmail(email);

        if (provider != null &&
            provider.getPassword().equals(password)) {

            return provider;
        }

        return null;
    }

    // ================= SAVE (FOR OTP UPDATE) =================
    public ServiceProvider save(ServiceProvider provider) {
        return serviceProviderRepository.save(provider);
    }

    // ================= FIND BY EMAIL =================
    public ServiceProvider findByEmail(String email) {
        return serviceProviderRepository.findByEmail(email);
    }
}