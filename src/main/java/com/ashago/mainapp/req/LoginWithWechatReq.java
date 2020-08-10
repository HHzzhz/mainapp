package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginWithWechatReq {
    @NotBlank
    private String code;
}
