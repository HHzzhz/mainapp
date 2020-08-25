package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.LikeTargetType;
import com.ashago.mainapp.req.LikeReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.LikeService;
import com.ashago.mainapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    @PostMapping("/like/post")
    public CommonResp postLike(@RequestBody @Valid LikeReq postLikeReq) {
        userService.checkSession(postLikeReq.getUserId());
        return likeService.postLike(postLikeReq.getUserId(), postLikeReq.getLikeTargetType(), postLikeReq.getLikeTargetId());
    }

    @PostMapping("/like/cancel")
    public CommonResp cancelLike(@RequestBody @Valid LikeReq cancelLikeReq) {
        userService.checkSession(cancelLikeReq.getUserId());
        return likeService.cancelLike(cancelLikeReq.getUserId(), cancelLikeReq.getLikeTargetType(), cancelLikeReq.getLikeTargetId());
    }

    @GetMapping("/like/list")
    public CommonResp listLike(@RequestParam String userId,
                               @RequestParam(required = false) LikeTargetType likeTargetType) {
        userService.checkSession(userId);
        return likeService.listLike(userId, likeTargetType);
    }

    @GetMapping("/like/like-or-not")
    public CommonResp likeOrNot(@RequestParam String userId,
                                @RequestParam LikeTargetType likeTargetType,
                                @RequestParam String likeTargetId) {
        userService.checkSession(userId);
        Boolean likeOrNot = likeService.likeOrNot(userId, likeTargetType, likeTargetId);
        return CommonResp.success().appendData("likeOrNot", likeOrNot);
    }
}
