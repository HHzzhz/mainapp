package com.ashago.mainapp.esrepository;

import com.ashago.mainapp.domain.EsService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsServiceRepository extends ElasticsearchRepository<EsService, String> {
}
