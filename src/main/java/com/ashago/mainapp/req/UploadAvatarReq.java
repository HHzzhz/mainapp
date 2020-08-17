package com.ashago.mainapp.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class UploadAvatarReq {
    @NotBlank
    private String userId;
    @NotBlank
    private MultipartFile avatar;
}
