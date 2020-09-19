package com.ashago.mainapp.req;

import com.ashago.mainapp.domain.LikeTargetType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LikeReq {
    private String userId;
    @NotBlank
    private String likeTargetId;
    @NotNull
    private LikeTargetType likeTargetType;
}
