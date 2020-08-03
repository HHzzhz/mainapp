package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
