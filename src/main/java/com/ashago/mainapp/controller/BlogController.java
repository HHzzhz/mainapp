package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.resp.BlogResp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ashago.mainapp.service.BlogService;

@RestController
@CrossOrigin
@RequestMapping("blog")
@Slf4j
public class BlogController {
    // UPDATE : Autowiring
    @Autowired
    private BlogService blogService;

    @PostMapping(value="/get-blog-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BlogResp> getBlogList(Blog blog) {
        BlogResp resp = blogService.getBlogList(blog);
        System.out.println(resp);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/get-blog-info")
    public BlogResp getBlogInfo(Blog blog) {
        BlogResp resp = blogService.getBlogInfo(blog);
        System.out.println(resp);
        return resp;
    }

    @PostMapping("/add-blog")
    public BlogResp addBlog(Blog blog) {
        BlogResp resp = blogService.addBlog(blog);
        System.out.println(resp);
        return resp;
    }
}