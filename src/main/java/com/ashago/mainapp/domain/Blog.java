package com.ashago.mainapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.ToString;

import javax.persistence.Column;
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
    //title
    private String title;

    //blogId
    private String blogId;

    @Column(columnDefinition="TEXT")
    private String content;

    @Column(columnDefinition="TEXT")
    private String html;
    private String city;
    private String tag;
    
    @Builder.Default
    private Boolean recommend = false;

    @Column(columnDefinition="TEXT")
    private String img;
    @Column(columnDefinition="TEXT")
    private String avatar;
    private String time;
    private Integer views = 0;
    private Integer likes = 0;
    private Integer comment = 0;
    private String author;
    private String category;
    private String date;

    @Tolerate
    public Blog() {}
}
