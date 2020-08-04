package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
}
