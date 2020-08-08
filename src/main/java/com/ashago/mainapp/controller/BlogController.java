package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.repository.BlogRepository;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {

   @Autowired
   private BlogRepository blogRepository;

   @RequestMapping("/getAllBlog")
   @ResponseBody
   public List<Blog> findAll() {
       List<Blog> list = new ArrayList<Blog>();
       list = blogRepository.findAll();
       return list;
   }

   @RequestMapping("/getByTitle")
   @ResponseBody
   public Blog getByTitle(String title) {
       Blog blog = blogRepository.findByTitle(title);
       return blog;
   }

   @RequestMapping("/getByCategory")
   @ResponseBody
   public Blog getByCategory(String category) {
       Blog blog = blogRepository.findByCategory(category);
       return blog;
   }

    @RequestMapping("/getByTag")
    @ResponseBody
    public Blog getByTag(String tag) {
        Blog blog = blogRepository.findByTag(tag);
        return blog;
    }

    @RequestMapping("/getByRecommend")
    @ResponseBody
    public Blog getByRecommand(Boolean recommend) {
        Blog blog = blogRepository.findByRecommend(recommend);
        return blog;
    }
}