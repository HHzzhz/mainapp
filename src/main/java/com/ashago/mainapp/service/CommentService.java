package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.domain.Comment;
import com.ashago.mainapp.domain.UserProfile;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.repository.CommentRepository;
import com.ashago.mainapp.repository.UserProfileRepository;
import com.ashago.mainapp.resp.CommentResp;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
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
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

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
        List<CommentResp> commentRespList = commentList.parallelStream().map(comment -> {
            Optional<Blog> blogFinding = blogRepository.findOne(Example.of(Blog.builder().blogId(comment.getBlogId()).build()));
            Optional<UserProfile> userProfileFinding = userProfileRepository.findOne(Example.of(UserProfile.builder().userId(comment.getUserId()).build()));
            CommentResp.CommentRespBuilder commentRespBuilder = CommentResp.builder();
            if (blogFinding.isPresent()) {
                commentRespBuilder.blogId(comment.getBlogId()).blogTitle(blogFinding.get().getTitle());
            } else {
                commentRespBuilder.blogId(comment.getBlogId()).blogTitle("unknown");
            }

            if (userProfileFinding.isPresent()) {
                commentRespBuilder.author(userProfileFinding.get().getUserName()).avatar(userProfileFinding.get().getAvatar());
            } else {
                commentRespBuilder.author("unknown");
            }
            return commentRespBuilder.build();
        }).collect(Collectors.toList());
        log.info("comment resp:{}", commentRespList);
        return CommonResp.success().appendData("comments", commentList);
    }
}
