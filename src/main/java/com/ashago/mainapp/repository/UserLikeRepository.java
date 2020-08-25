package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
}
