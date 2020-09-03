package com.ashago.mainapp.esrepository;

import com.ashago.mainapp.domain.EsBlog;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EsRepository extends ElasticsearchRepository<EsBlog, String> {
    //EsBlog findByContent(String content);
    Page<EsBlog> findByContent(String content, Pageable pageable);
    Page<EsBlog> findByTag(String tag, Pageable pageable);
}