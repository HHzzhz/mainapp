package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AppendServiceReq {
    @NotBlank
    private String category;
    @NotNull
    private Boolean isOffLineSupport;
    @NotBlank
    private String city;
    @NotBlank
    private String title;
    @NotBlank
    private String desc;
    @NotBlank
    private String serviceProviderId;
    @NotNull
    private Boolean isOnlineSupport;
    @NotBlank
    private String image;
    @NotBlank
    private String detailHtml;
    @NotBlank
    private String detailPlaintext;
    @NotBlank
    private String price;
    private Integer priority;
}
