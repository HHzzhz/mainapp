package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {
}
