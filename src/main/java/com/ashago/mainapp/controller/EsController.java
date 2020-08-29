package com.ashago.mainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.esrepository.EsRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.service.BlogService;

import org.slf4j.LoggerFactory;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/es")
public class EsController {

    Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsRepository esRepository;


    @Autowired
    private BlogService blogService;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @GetMapping("/new-index")
    public boolean createIndex() {
        return elasticsearchTemplate.indexOps(EsBlog.class).create();
    }

    @RequestMapping(value = "/blog", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Iterable<EsBlog> getBookByName(@RequestParam(value = "name") String name) {
        EsBlog article = new EsBlog();
        article.setId(name);
        esRepository.save(article);
        return esRepository.findAll();
    }

    @PostMapping("/get-blog-list")
    public BlogResp getBlogInfo(Blog blog) {
        BlogResp resp = blogService.getBlogInfo(blog);
        System.out.println(resp);
        return resp;
    }

    @RequestMapping(value = "/blog", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Blog indexBook(@RequestBody Blog blog) {
        return blogRepository.save(blog);
    }
}
