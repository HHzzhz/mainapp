package com.ashago.mainapp.service;

import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.repository.CommentRepository;
import com.ashago.mainapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Long countUser() {
        return userRepository.count();
    }

    public Long countBlog() {
        return blogRepository.count();
    }

    public Long countComment() {
        return commentRepository.count();
    }
}
