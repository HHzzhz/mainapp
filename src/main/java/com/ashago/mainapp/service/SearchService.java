package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Service;
import com.ashago.mainapp.repository.ServiceRepository;
import com.ashago.mainapp.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;

import java.util.List;

@org.springframework.stereotype.Service
public class SearchService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private EsService esService;

    public CommonResp searchAll(String keywords) {
        return CommonResp.success().appendData("articleList", searchArticle(keywords).getData("articleList"))
                .appendData("serviceList", searchService(keywords).getData("serviceList"));
    }

    public CommonResp searchArticle(String keywords) {
        return CommonResp.success().appendData("articleList", esService.searchContent(keywords).getDataList());
    }

    public CommonResp searchService(String keywords) {
        Example<Service> serviceExample = Example.of(
                Service.builder()
                        .location(keywords)
                        .category(keywords)
                        .title(keywords)
                        .desc(keywords)
                        .detailPlaintext(keywords)
                        .build(),
                ExampleMatcher.matchingAny()
                        .withMatcher("location", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                        .withMatcher("category", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                        .withMatcher("title", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                        .withMatcher("desc", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                        .withMatcher("detailPlaintext", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())

        );
        List<Service> serviceList = serviceRepository.findAll(serviceExample, Sort.by(Sort.Direction.DESC, "priority"));
        return CommonResp.success().appendData("serviceList", serviceList);
    }
}
