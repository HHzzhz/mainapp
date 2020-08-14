package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findByTitle(String title);
    Blog findByTag(String tag);
    Blog findByRecommend(Boolean recommend);
}