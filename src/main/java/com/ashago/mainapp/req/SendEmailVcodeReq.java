package com.ashago.mainapp.req;

import com.ashago.mainapp.domain.VcodeScene;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendEmailVcodeReq {
    @NotBlank
    private String email;
    @NotBlank
    private VcodeScene scene;
}
