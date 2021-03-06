package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RecommendAppendReq {
    @NotBlank
    private String title;
    @NotBlank
    private String cover;

    @NotNull
    private Integer type;

    private String serviceId;
    private String blogId;
    @NotNull
    private Integer priority;

}
