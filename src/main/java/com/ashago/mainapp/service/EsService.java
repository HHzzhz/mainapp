package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.esrepository.EsRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.util.SnowFlake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

@Service
public class EsService {
    //@Resource
    @Autowired
    private EsRepository esRepository;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    private Pageable pageable = PageRequest.of(0,10);

    public BlogResp searchContent(String content) {
        Page<EsBlog> esblogs = esRepository.findByContent(content, pageable);
        List<EsBlog> esblogsList = esblogs.getContent();
        if (!esblogsList.isEmpty()) {
            return BlogResp.success().appendDataList(esblogsList);
        } else {
            return BlogResp.create("404", "EsBlog does not exist!");
        }
    }

    public BlogResp searchTag(String tag) {
        Page<EsBlog> esblogs = esRepository.findByTag(tag, pageable);
        List<EsBlog> esblogsList = esblogs.getContent();
        if (!esblogsList.isEmpty()) {
            return BlogResp.success().appendDataList(esblogsList);
        } else {
            return BlogResp.create("404", "EsBlog does not exist!");
        }
    }

    public BlogResp addBlog(EsBlog esblog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        esblog.setDate(sdf.format(new Date()));
        String blogId = String.valueOf(snowFlake.nextId());
        esblog.setId(blogId);

        if (esRepository.existsById(blogId)) {
            return BlogResp.create("409", "EsBlog exist");
        }
        esRepository.save(esblog);
        return BlogResp.success().appendData(Optional.of(esblog));
    }
}
