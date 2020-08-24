package com.ashago.mainapp.req;

import com.ashago.mainapp.domain.VcodeScene;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendEmailVcodeReq {
    @NotBlank
    private String email;
    @NotNull
    private VcodeScene scene;
}
