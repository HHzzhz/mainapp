package com.ashago.mainapp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    //传入email
    private String userId;
    private String email;
    private String userName;
    private String token;
    private Boolean subscribed;
    private Boolean activated;

}
