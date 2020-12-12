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
public class SingleBlogResp {
    private String title;
    private String cover;
    private String author;
    private String avatar;
    private String blogId;
    private String tags;
    private String content;
    private String time;
    private LocalDateTime postAt;
}
