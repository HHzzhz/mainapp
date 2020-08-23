package com.ashago.mainapp.repository;

import java.util.List;

import com.ashago.mainapp.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findByTitle(String title);
    Blog findByTag(String tag);
    Blog findByRecommend(Boolean recommend);
    List<Blog> findFirst5ByCategory(String category);
}