package com.ashago.mainapp.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResp {
    private String likeId;
    private String title;
    private String cover;
    private LocalDateTime likeAt;
    private List<String> slots;
}
