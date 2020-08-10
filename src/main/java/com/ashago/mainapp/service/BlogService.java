package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BlogService {
    @Autowired
    static private BlogRepository blogRepository;
/*
    @Autowired
    private MailService mailService;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);


    public CommonResp login(Blog blog) {
        Example<Blog> blogExample = Example.of(blog,
                ExampleMatcher.matching()
                        .withIgnorePaths("subscribed")
                        .withIgnorePaths("blogName")
                        .withIgnorePaths("activated")
        );
        Optional<Blog> blogFinding = blogRepository.findOne(blogExample);
        if (blogFinding.isPresent()) {
            String t = UUID.randomUUID().toString();
            return CommonResp.success()
                    .appendData("t", t)
                    .appendData("blogId", blogFinding.get().getId())
                    .appendData("sessionId", t.toUpperCase());
        } else {
            return CommonResp.create("E003", "email or pass failed");
        }
    }

    public CommonResp loginWithWechat(String code) {
        try {
            String openId = "This is ID !!!!!";

            Blog blog = Blog.builder().wxOpenId(openId).build();
            Example<Blog> blogExample = Example.of(blog);
            Optional<Blog> blogFinding = blogRepository.findOne(blogExample);
            if (blogFinding.isPresent()) {
                String t = UUID.randomUUID().toString();
                return CommonResp.success()
                        .appendData("t", t)
                        .appendData("blogId", blogFinding.get().getId())
                        .appendData("sessionId", t.toUpperCase());
            } else {
                //登录成功，处理注册流程
                String blogId = String.valueOf(snowFlake.nextId());
                blog.setBlogId(blogId);
                blogRepository.save(blog);
                Blog blog = Blog.builder()
                        .blogId(blogId)
                        .build();
                blogRepository.save(blog);
                blogRepository.flush();
                blogRepository.flush();
                String t = UUID.randomUUID().toString();
                return CommonResp.success()
                        .appendData("t", t)
                        .appendData("blogId", blogId)
                        .appendData("sessionId", t.toUpperCase());
            }

        } catch (Exception e) {
            log.error("wx process err.", e);
            return CommonResp.create("303", "未知错误");
        }

    }

    public CommonResp getBlog(String blogId) {

        Blog blog = Blog.builder().title(blogId).build();
        Example<Blog> blogExample = Example.of(blog);
        Optional<Blog> blogFinding = blogRepository.findOne(blogExample);
        if (blogFinding.isPresent()) {

            return CommonResp.success()
                    .appendData("blogId", blogFinding.get().getBlogId())
                    .appendData("city", blogFinding.get().getCity())
                    .appendData("country", blogFinding.get().getCountry())
                    .appendData("nationality", blogFinding.get().getNationality())
                    .appendData("blogName", blogFinding.get().getBlogName())
                    .appendData("subscribed", blogFinding.get().getSubscribed())
                    .appendData("interesting", blogFinding.get().getInteresting())
                    .appendData("requiredCompleted", computeRequiredCompleted(blogFinding.get()));
        } else {
            return CommonResp.create("301", "用户不存在");
        }
    }

    private Boolean computeRequiredCompleted(Blog blog) {
        return blog.getCity() != null
                && blog.getCountry() != null
                && blog.getNationality() != null
                && blog.getBlogName() != null
                && blog.getInteresting() != null;
    }*/
    static public CommonResp getBlog(String title, String tag) {
        Blog blog = Blog.builder()
                .title(title)
                .tag(tag)
                .build();
        Example<Blog> blogExample = Example.of(blog);
        List<Blog> blogFinding = blogRepository.findAll(blogExample);
        if (!blogFinding.isEmpty()) {

            return CommonResp.success()
                    .appendData("data",blogFinding);
        } else {
            return CommonResp.create("301", "用户不存在");
        }
    }
}