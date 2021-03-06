package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Service {
    @Id
    @GeneratedValue
    private Integer id;
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
