package com.ashago.mainapp.repository;

import com.ashago.mainapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
