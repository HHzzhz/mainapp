package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Comment;
import com.ashago.mainapp.repository.CommentRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    private SnowFlake snowFlake = new SnowFlake(10, 10);

    public CommonResp postComment(String userId, String blogId, String content, String ref) {
        String commentId = StringUtils.join("C", snowFlake.nextId());
        Comment comment = Comment.builder().commentId(commentId)
                .blogId(blogId)
                .content(content)
                .postAt(LocalDateTime.now())
                .ref(ref)
                .userId(userId)
                .build();
        commentRepository.saveAndFlush(comment);
        return CommonResp.success().appendData("commentId", commentId);
    }

    public CommonResp listComment(String userId, String blogId) {
        List<Comment> commentList = commentRepository.findAll(Example.of(Comment.builder().blogId(blogId).userId(userId).build()), Sort.by(Sort.Direction.ASC, "postAt"));
        return CommonResp.success().appendData("comments", commentList);
    }
}
