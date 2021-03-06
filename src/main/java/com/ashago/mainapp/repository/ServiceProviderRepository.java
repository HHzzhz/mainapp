package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {
}
