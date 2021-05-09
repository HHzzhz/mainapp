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
public class Recommend {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer priority;
    private String recommendId;
    private String cover;
    private String recommendTitle;
    //推荐的类型，1 - blog；2 - service
    private Integer type;
    private String blogId;
    private String serviceId;
}
