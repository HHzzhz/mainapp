package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.resp.SingleBlogResp;
import com.ashago.mainapp.util.SnowFlake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    public BlogResp getBlogList(Blog blog) {
        Example<Blog> blogExample = Example.of(blog);
        List<Blog> blogFinding = blogRepository.findAll(blogExample);
        Collections.reverse(blogFinding);

        if (!blogFinding.isEmpty()) {
            Iterator<Blog> i = blogFinding.iterator();
            String allTagStr = "";
            while (i.hasNext()) {
                Blog nextBlog = i.next();
                if (nextBlog.getTag() != null)
                    allTagStr += nextBlog.getTag() + ",";
            }
            Set<String> items = new HashSet<String>(Arrays.asList(allTagStr.split(",")));
            String data = String.join(",", items);
            Optional<String> tagsOptional = Optional.of(data);
            System.out.println(items.toString());
            return BlogResp.success().appendDataList(blogFinding).appendData(tagsOptional);
        } else {
            return BlogResp.create("404", "Blog does not exist!");
        }
    }

    public BlogResp addBlog(Blog blog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        blog.setDate(sdf.format(new Date()));
        String blogId = String.valueOf(snowFlake.nextId());
        blog.setBlogId(blogId);
        Example<Blog> blogExample = Example.of(blog);

        if (blogRepository.exists(blogExample)) {
            return BlogResp.create("409", "Blog exist");
        }
        blogRepository.saveAndFlush(blog);
        if (blog.getId() == null) {
            return BlogResp.create("500", "Failed");
        }

        blogRepository.saveAndFlush(blog);
        return BlogResp.success().appendData(Optional.of(blog));
    }

    public BlogResp getBlogInfo(Blog blog) {

        System.out.println(blog.toString());
        Example<Blog> blogExample = Example.of(blog);
        Optional<Blog> blogFinding = blogRepository.findOne(blogExample);

        if (blogFinding.isPresent()) {
            List<Blog> relatedBlogFinding = blogRepository.findFirst5ByCategory(blogFinding.get().getCategory());
            Iterator<Blog> i = relatedBlogFinding.iterator();
            while (i.hasNext()) {
                Blog nextBlog = i.next();
                if (nextBlog.getBlogId().equals(blog.getBlogId())) {
                    i.remove();
                    break;
                }
            }
            if (relatedBlogFinding.size() >= 5)
                i.remove();
            Collections.reverse(relatedBlogFinding);
            return BlogResp.success().appendData(blogFinding).appendDataList(relatedBlogFinding);
        } else {
            return BlogResp.create("301", "Blog does not exist!");
        }
    }

    public CommonResp getRecentBlog() {
        List<Blog> blogList = blogRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "id")).getContent();
        List<SingleBlogResp> singleBlogRespList = blogList.parallelStream().map(blog -> SingleBlogResp.builder().author(blog.getAuthor())
                .blogId(blog.getBlogId())
                .cover(blog.getImg())
                .title(blog.getTitle())
                .postAt(LocalDateTime.parse(blog.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))).build()).collect(Collectors.toList());
        return CommonResp.success().appendData("recentBlogs", singleBlogRespList);
    }
}