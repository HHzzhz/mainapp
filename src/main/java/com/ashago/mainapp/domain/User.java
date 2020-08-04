package com.ashago.mainapp.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    //userId
    private String userId;

    //email登录凭证
    private String email;
    private String token;
    private Boolean emailVerified;

    //微信登录凭证
    private String wxOpenId;


}
