package com.ashago.mainapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@ToString
public class Blog {
    @Id
    @GeneratedValue
    private Long id;
    //userId
    private String title;
    private String content;
    private String html;
    private String city;
    private String tag;
    private Boolean recommend;
    private String img;
    private String time;
    private Integer views;
    private String author;

    @Tolerate
    public Blog() {}
}
