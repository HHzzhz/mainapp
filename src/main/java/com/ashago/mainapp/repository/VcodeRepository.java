package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Vcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VcodeRepository extends JpaRepository<Vcode, Integer> {
}
