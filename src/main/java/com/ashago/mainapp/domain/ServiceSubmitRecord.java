package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ServiceSubmitRecord {

    @Id
    @GeneratedValue
    private Integer id;
    private String serviceId;
    private String whenAndWhere;
    private String name;
    private String msg;
    private String email;
    private String mobile;
    private LocalDateTime submitAt;
    private Boolean notified;
}
