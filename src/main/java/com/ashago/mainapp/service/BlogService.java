package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.resp.BlogResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public BlogResp getBlog(String title, String tag, Boolean recommend) {
        Blog blog = Blog.builder()
                .title(title)
                .tag(tag)
                .recommend(recommend)
                .build();
        System.out.println(blog);
        Example<Blog> blogExample = Example.of(blog);
        List<Blog> blogFinding = blogRepository.findAll(blogExample);

        System.out.println(blogFinding.toString());
        if (!blogFinding.isEmpty()) {
            return BlogResp.success().appendDataList(blogFinding);
        } else {
            return BlogResp.create("404", "Blog does not exist!");
        }
    }
    public BlogResp addBlog(String title, String tag, Boolean recommend) {

        Blog blog = Blog.builder()
                .title(title)
                .build();
        Example<Blog> blogExample = Example.of(blog);

        if (blogRepository.exists(blogExample)) {
            return BlogResp.create("409", "Blog exist");
        }
        
        blog.setTitle(title);
        blog.setTag(tag);
        blog.setRecommend(recommend);
        blogRepository.saveAndFlush(blog);

        if (blog.getId() == null) {
            return BlogResp.create("500", "Failed");
        }

        blogRepository.saveAndFlush(blog);
        return BlogResp.success().appendData(Optional.of(blog));
    }
    public BlogResp getBlogInfo(String title) {
        Blog blog = Blog.builder()
            .title(title)
            .build();
        Example<Blog> blogExample = Example.of(blog);
        Optional<Blog> blogFinding = blogRepository.findOne(blogExample);

        System.out.println(blogFinding.toString());
        if (blogFinding.isPresent()) {
            return BlogResp.success().appendData(blogFinding);/*
                    .appendData("blogId", blogFinding.get().getId())
                    .appendData("title", blogFinding.get().getTitle())
                    .appendData("city", blogFinding.get().getCity())
                    .appendData("content", blogFinding.get().getContent())
                    .appendData("html", blogFinding.get().getHtml())
                    .appendData("recommend", blogFinding.get().getRecommend())
                    .appendData("tag", blogFinding.get().getTag())
                    .appendData("requiredCompleted", computeRequiredCompleted(blogFinding.get()));*/
                    //.appendData("data",blogFinding.toString());
        } else {
            return BlogResp.create("301", "Blog does not exist!");
        }
    }
}