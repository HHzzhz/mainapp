package com.ashago.mainapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class UserProfile {

    @Id
    @GeneratedValue
    private Integer id;
    private String userId;
    private String userName;
    private String nationality;
    private String country;
    private String city;
    private String interesting;
    private Boolean subscribed;
}
