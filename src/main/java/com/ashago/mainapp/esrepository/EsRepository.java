package com.ashago.mainapp.esrepository;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.domain.EsBlog;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EsRepository extends ElasticsearchRepository<EsBlog, String> {
    //EsBlog findByContent(String content);
    Page<EsBlog> findByContent(String content, Pageable pageable);
    Page<EsBlog> findByTag(String tag, Pageable pageable);
}