package com.ashago.mainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.esrepository.EsRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.service.BlogService;
import com.ashago.mainapp.service.EsService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/es")
public class EsController {

    Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsRepository esRepository;

    @Autowired
    private EsService esService;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @PostMapping("/add-blog")
    @ResponseBody
    public BlogResp addBlog(EsBlog esblog) {
        BlogResp resp = esService.addBlog(esblog);
        System.out.println(resp);
        return resp;
    }

    @GetMapping("/search-content")
    @ResponseBody
    public BlogResp serachContent(@RequestParam(value = "content") String content) {
        BlogResp resp = esService.searchContent(content);
        System.out.println(resp);
        return resp;
    }

    @GetMapping("/search-tag")
    @ResponseBody
    public BlogResp serachTag(@RequestParam(value = "tag") String tag) {
        BlogResp resp = esService.searchContent(tag);
        System.out.println(resp);
        return resp;
    }
}
