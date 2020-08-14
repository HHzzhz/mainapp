package com.ashago.mainapp.controller;

import com.ashago.mainapp.resp.BlogResp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ashago.mainapp.service.BlogService;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("blog")
public class BlogController {
    // UPDATE : Autowiring
    @Autowired
    private BlogService blogService;

    @PostMapping(value="/getBlog", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BlogResp> getBlog(@RequestParam Map<String, String> param) {
        String title = param.get("title");
        String tag = param.get("tag");
        Boolean recommend = Boolean.valueOf(param.get("recommend"));
        BlogResp resp = blogService.getBlog(title,tag, recommend);
        System.out.println(resp);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/getBlogInfo")
    public BlogResp getBlogInfo(@RequestParam Map<String, String> param) {
        String title = param.get("title");
        BlogResp resp = blogService.getBlogInfo(title);
        System.out.println(resp);
        return resp;
    }

    @PostMapping("/addBlog")
    public BlogResp addBlog(@RequestParam Map<String, String> param) {
        String title = param.get("title");
        String tag = param.get("tag");
        Boolean recommend = Boolean.parseBoolean(param.get("recommend"));
        BlogResp resp = blogService.addBlog(title,tag,recommend);
        System.out.println(resp);
        return resp;
    }
}