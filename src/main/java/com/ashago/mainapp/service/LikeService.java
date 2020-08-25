package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.LikeTargetType;
import com.ashago.mainapp.domain.UserLike;
import com.ashago.mainapp.repository.UserLikeRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private UserLikeRepository userLikeRepository;
    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    public CommonResp postLike(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        UserLike userLike = UserLike.builder().likeTargetId(likeTargetId).likeTargetType(likeTargetType).userId(userId).build();
        Optional<UserLike> userLikeOptional = userLikeRepository.findOne(Example.of(userLike));
        if (userLikeOptional.isPresent()) {
            if (Boolean.TRUE.equals(userLikeOptional.get().getEnable())) {
                //do nothing
            } else {
                userLikeOptional.get().setEnable(Boolean.TRUE);
                userLikeRepository.saveAndFlush(userLikeOptional.get());
            }
        } else {
            userLike.setEnable(Boolean.TRUE);
            userLike.setLikeAt(LocalDateTime.now());
            userLike.setLikeId(StringUtils.join(snowFlake.nextId()));
            userLikeRepository.saveAndFlush(userLike);
        }
        return CommonResp.success();
    }

    public CommonResp cancelLike(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        Optional<UserLike> userLikeOptional = userLikeRepository.findOne(Example.of(
                UserLike.builder().likeTargetId(likeTargetId).likeTargetType(likeTargetType).userId(userId).build()));
        if (userLikeOptional.isPresent()) {
            userLikeOptional.get().setEnable(Boolean.FALSE);
            userLikeRepository.saveAndFlush(userLikeOptional.get());
        } else {
            //do nothing
        }
        return CommonResp.success();
    }

    public CommonResp listLike(String userId, LikeTargetType likeTargetType) {
        return null;
    }

    public Boolean likeOrNot(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        Example<UserLike> userLikeExample = Example.of(UserLike.builder().userId(userId)
                .enable(Boolean.TRUE).likeTargetType(likeTargetType).likeTargetId(likeTargetId).build());
        return userLikeRepository.exists(userLikeExample);
    }
}
