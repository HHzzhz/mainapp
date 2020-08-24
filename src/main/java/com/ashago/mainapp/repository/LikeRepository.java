package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
