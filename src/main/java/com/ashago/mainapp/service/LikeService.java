package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.domain.LikeTargetType;
import com.ashago.mainapp.domain.UserLike;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.repository.UserLikeRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.resp.LikeResp;
import com.ashago.mainapp.util.SnowFlake;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikeService {
    @Autowired
    private UserLikeRepository userLikeRepository;
    @Autowired
    private BlogRepository blogRepository;
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
        List<UserLike> userLikeList = userLikeRepository.findAll(Example.of(UserLike.builder().enable(Boolean.TRUE).likeTargetType(likeTargetType).userId(userId).build()), Sort.by(Sort.Direction.ASC, "likeAt"));
        log.info("like list:" + userLikeList);
        List<LikeResp> likeRespList = userLikeList.parallelStream().map(userLike -> {
            LikeResp.LikeRespBuilder likeRespBuilder = LikeResp.builder();
            switch (userLike.getLikeTargetType()) {
                case BLOG:
                default:
                    Optional<Blog> blogOptional = blogRepository.findOne(Example.of(Blog.builder().blogId(userLike.getLikeTargetId()).build()));
                    blogOptional.ifPresent(blog -> likeRespBuilder.likeId(userLike.getLikeId())
                            .title(blog.getTitle())
                            .cover(blog.getImg())
                            .likeAt(userLike.getLikeAt())
                            .slots(Lists.newArrayList(blog.getAuthor(), StringUtils.join(blog.getTime(),"min"), blog.getTag())));
            }
            return likeRespBuilder.build();
        }).filter(likeResp -> likeResp.getLikeId() != null).collect(Collectors.toList());

        return CommonResp.success().appendData("likeList", likeRespList);
    }

    public Boolean likeOrNot(String userId, LikeTargetType likeTargetType, String likeTargetId) {
        Example<UserLike> userLikeExample = Example.of(UserLike.builder().userId(userId)
                .enable(Boolean.TRUE).likeTargetType(likeTargetType).likeTargetId(likeTargetId).build());
        return userLikeRepository.exists(userLikeExample);
    }
}
