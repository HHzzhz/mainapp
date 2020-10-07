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
    private String blogId;
    private LocalDateTime postAt;
}
