package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordReq {
    @NotBlank
    private String email;
    @NotBlank
    private String seqNo;
    @NotBlank
    private String vcode;
    @NotBlank
    private String newPassword;
}
