package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "service")
public class EsService {
    @Id
    @GeneratedValue
    private String id;
    @Field(type = FieldType.Keyword)
    private String serviceId;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private Boolean isOffLineSupport;
    @Field(type = FieldType.Keyword)
    private String city;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String desc;
    @Field(index = false, type = FieldType.Keyword)
    private String serviceProviderId;
    @Field(type = FieldType.Keyword)
    private Boolean isOnlineSupport;
    @Field(index = false, type = FieldType.Keyword)
    private String image;
    @Field(type = FieldType.Text)
    private String detailPlaintext;
    @Field(index = false, type = FieldType.Keyword)
    private String price;
    @Field(index = false, type = FieldType.Keyword)
    private Integer priority;
}
