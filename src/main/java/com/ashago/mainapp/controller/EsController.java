package com.ashago.mainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.*;

import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.esrepository.EsRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.service.EsService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/es")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD}
)
public class EsController {

    Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired
    private EsService esService;

    @Autowired
    private EsRepository esRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/new-index")
    public boolean createIndex() {
        return elasticsearchOperations.indexOps(EsBlog.class).create();
    }

    @GetMapping("/blog")
    @ResponseBody
    Iterable<EsBlog> getBookByName() {
        return esRepository.findAll();
    }

    @PostMapping("/add-blog")
    @ResponseBody
    public BlogResp addBlog(EsBlog esblog) {
        BlogResp resp = esService.addBlog(esblog);
        System.out.println(resp);
        return resp;
    }

    @GetMapping("/search-content")
    @ResponseBody
    public BlogResp serachContent(EsBlog blog) {
        BlogResp resp = esService.searchContent(blog.getContent().toString());
        System.out.println(resp);
        return resp;
    }

    @GetMapping("/search-tag")
    @ResponseBody
    public BlogResp serachTag(EsBlog blog) {
        BlogResp resp = esService.searchTag(blog.getTag().toString());
        System.out.println(resp);
        return resp;
    }
}
