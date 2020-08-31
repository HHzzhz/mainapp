package com.ashago.mainapp.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostCommentReq {
    @NotBlank
    private String userId;
    @NotBlank
    private String blogId;
    @NotBlank
    private String content;
    private String ref;
}
