package com.ashago.mainapp.esrepository;

import com.ashago.mainapp.domain.EsService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsServiceRepository extends ElasticsearchRepository<EsService, String> {
    List<EsService> findByServiceId(String serviceId);
}
