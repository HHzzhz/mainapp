package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReq {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
