package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.LikeTargetType;
import com.ashago.mainapp.resp.CommonResp;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    public CommonResp postLike(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        return null;
    }

    public CommonResp cancelLike(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        return null;
    }

    public CommonResp listLike(String userId, String likeTargetId, LikeTargetType likeTargetType) {
        return null;
    }
}
