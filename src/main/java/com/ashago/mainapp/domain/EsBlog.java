package com.ashago.mainapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@ToString

@Document(indexName= "searchblog")
public class EsBlog {
    @Id
    @GeneratedValue
    @Field(type = FieldType.Long)
    private String id;
    //title
    private String title;

    private String blogId;
    @Column(columnDefinition="TEXT")
    @Field(type = FieldType.Text)
    private String content;

    @Column(columnDefinition="TEXT")
    private String html;
    private String city;
    @Field(type = FieldType.Text)
    private String tag;
    
    @Builder.Default
    private Boolean recommend = false;

    @Column(columnDefinition="TEXT")
    private String img;
    @Column(columnDefinition="TEXT")
    private String avatar;
    private String time;
    private Integer views;
    private Integer likes;
    private Integer comment;
    private String author;
    private String category;
    private String date;

    @Tolerate
    public EsBlog() {}
}
