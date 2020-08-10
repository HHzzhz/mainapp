package com.ashago.mainapp.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordReq {
    @NotBlank
    private String userId;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
