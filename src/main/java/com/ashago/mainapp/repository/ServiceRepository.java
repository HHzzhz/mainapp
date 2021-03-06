package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
