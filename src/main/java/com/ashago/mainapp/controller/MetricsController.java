package com.ashago.mainapp.controller;

import com.ashago.mainapp.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {

    @Autowired
    private MetricsService metricsService;


    @GetMapping("/metrics/user:count")
    public Long countUser() {
        return metricsService.countUser();
    }

    @GetMapping("/metrics/blog:count")
    public Long countBlog() {
        return metricsService.countBlog();
    }

    @GetMapping("/metrics/comment:count")
    public Long countComment() {
        return metricsService.countComment();
    }
}
