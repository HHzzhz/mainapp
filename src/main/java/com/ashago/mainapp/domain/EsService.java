package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "service")
@Deprecated
public class EsService {
    @Id
    @GeneratedValue
    private String id;
    private String serviceId;
    private String category;
    private Boolean isOffLineSupport;
    private String location;
    private String title;
    private String desc;
    private String serviceProviderId;
    private Boolean isOnlineSupport;
    private String image;
    private String detailHtml;
    private String price;
    private Integer priority;
}
