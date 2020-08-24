package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserLike {
    @Id
    @GeneratedValue
    private Long id;
    private String likeId;
    private String userId;
    private String likeTargetId;
    private LikeTargetType likeTargetType;
    private LocalDateTime likeAt;
    private Boolean enable;
}
