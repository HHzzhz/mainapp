package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.Blog;
import com.ashago.mainapp.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {

   @Autowired
   private BlogRepository blogRepository;

   //TODO: 明确请求的method
   @RequestMapping("/getAllBlog")

   //TODO: 不需要这个注解，具体参见@RestController
   @ResponseBody

   //TODO:返回都用CommonResp包一下吧，这样接口的返回会比较统一，具体用法可以参见UserService
   public List<Blog> findAll() {
       List<Blog> list = new ArrayList<Blog>();
       //TODO: 不要在controller层写逻辑，写一个BlogService
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