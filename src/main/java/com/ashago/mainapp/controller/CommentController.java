package com.ashago.mainapp.controller;

import com.ashago.mainapp.req.PostCommentReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.CommentService;
import com.ashago.mainapp.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/post")
    public CommonResp postComment(@RequestBody @Valid PostCommentReq postCommentReq) {
        userService.checkSession(postCommentReq.getUserId());
        return commentService.postComment(postCommentReq.getUserId(), postCommentReq.getBlogId(), postCommentReq.getContent(), postCommentReq.getRef());
    }

    @GetMapping("/comment/list")
    public CommonResp listComment(@RequestParam(required = false) String userId,
                                  @RequestParam(required = false) String blogId) {
        if (StringUtils.isNotBlank(userId)) {
            userService.checkSession(userId);
        }
        return commentService.listComment(userId, blogId);
    }
}
