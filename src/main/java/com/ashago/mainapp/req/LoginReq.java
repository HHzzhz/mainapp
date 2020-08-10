package com.ashago.mainapp.req;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
}
