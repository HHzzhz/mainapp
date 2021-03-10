package com.ashago.mainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.*;

import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.esrepository.EsBlogRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.service.EsService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Deprecated
@Controller
@RequestMapping("/es")
public class EsController {

    Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired
    private EsService esService;

    @Autowired
    private EsBlogRepository esBlogRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/new-index")
    public boolean createIndex() {
        return elasticsearchOperations.indexOps(EsBlog.class).create();
    }

    @GetMapping("/blog")
    @ResponseBody
    Iterable<EsBlog> getBookByName() {
        return esBlogRepository.findAll();
    }

    @PostMapping("/add-blog")
    @ResponseBody
    public BlogResp addBlog(EsBlog esblog) {
        return esService.addBlog(esblog);
    }

    @Deprecated
    @GetMapping("/search-content")
    @ResponseBody
    public BlogResp searchContent(EsBlog blog) {
        return esService.searchContent(blog.getContent());
    }

    @Deprecated
    @GetMapping("/search-tag")
    @ResponseBody
    public BlogResp searchTag(EsBlog blog) {
        return esService.searchTag(blog.getTag());
    }
}
