package com.ashago.mainapp.controller;

import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search/all")
    public CommonResp searchAll(String keywords) {
        return null;
    }

    @GetMapping("/search/article")
    public CommonResp searchArticle(String keywords) {
        return null;
    }

    @GetMapping("/search/service")
    public CommonResp searchService(String keywords) {
        return null;
    }

}
