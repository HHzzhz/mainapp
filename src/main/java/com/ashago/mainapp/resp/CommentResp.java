package com.ashago.mainapp.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResp {
    //user
    private String userId;
    private String avatar;
    private String author;
    //blog
    private String blogId;
    private String blogTitle;
    //comment
    private LocalDateTime postAt;
    private String content;
    private String commentId;

}
