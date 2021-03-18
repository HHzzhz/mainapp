package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.domain.EsService;
import com.ashago.mainapp.esrepository.EsBlogRepository;
import com.ashago.mainapp.esrepository.EsServiceRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class SearchService {

    @Autowired
    private EsServiceRepository esServiceRepository;
    @Autowired
    private EsBlogRepository esBlogRepository;

    public CommonResp searchAll(String keywords) {
        return CommonResp.success().appendData("articleList", searchArticle(keywords).getData("articleList"))
                .appendData("serviceList", searchService(keywords).getData("serviceList"));
    }

    public CommonResp searchArticle(String keywords) {
        Iterable<EsBlog> esBlogIterable = esBlogRepository.search(QueryBuilders.queryStringQuery(keywords));
        return CommonResp.success().appendData("articleList", Lists.newArrayList(esBlogIterable));
    }

    public CommonResp searchService(String keywords) {
        Iterable<EsService> esServiceIterable = esServiceRepository.search(QueryBuilders.queryStringQuery(keywords));
        return CommonResp.success().appendData("serviceList", Lists.newArrayList(esServiceIterable));
    }
}
