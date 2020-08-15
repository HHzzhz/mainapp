package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
