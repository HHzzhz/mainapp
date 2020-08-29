// package com.ashago.mainapp.service;

// import com.ashago.mainapp.domain.Blog;
// import com.ashago.mainapp.repository.BlogRepository;
// import com.ashago.mainapp.repository.EsRepository;
// import com.ashago.mainapp.resp.BlogResp;
// import com.ashago.mainapp.util.SnowFlake;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Example;
// import org.springframework.stereotype.Service;

// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Optional;

// import javax.annotation.Resource;

// import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
// import org.elasticsearch.client.Requests;
// import org.elasticsearch.common.xcontent.XContentBuilder;
// import org.elasticsearch.common.xcontent.XContentFactory;
// import org.elasticsearch.index.query.QueryBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
// import org.elasticsearch.search.sort.FieldSortBuilder;
// import org.elasticsearch.search.sort.SortBuilder;
// import org.elasticsearch.search.sort.SortOrder;

// @Service
// public class EsService {
//     @Resource
//     private EsRepository esRepository;

//     //@Override
//     public List<Blog> findAllArticle() {
//         Iterable<Blog> iterable = esRepository.findAll();
//         System.out.println(iterable);
//         List<Blog> list = new ArrayList();
//         if(iterable != null){
//             iterable.forEach(list::add);
//         }
//         return list;
//     }

//     @Autowired
//     private BlogRepository blogRepository;

//     private final SnowFlake snowFlake = new SnowFlake(10, 10);

//     public BlogResp getBlogList(Blog blog) {
//         System.out.println(blog);
//         Example<Blog> blogExample = Example.of(blog);
//         List<Blog> blogFinding = blogRepository.findAll(blogExample);

//         System.out.println(blogFinding.toString());
//         if (!blogFinding.isEmpty()) {
//             return BlogResp.success().appendDataList(blogFinding);
//         } else {
//             return BlogResp.create("404", "Blog does not exist!");
//         }
//     }

//     public BlogResp addBlog(Blog blog) {
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         blog.setDate(sdf.format(new Date()));
//         String blogId = String.valueOf(snowFlake.nextId());
//         blog.setBlogId(blogId);
//         Example<Blog> blogExample = Example.of(blog);

//         if (blogRepository.exists(blogExample)) {
//             return BlogResp.create("409", "Blog exist");
//         }
//         blogRepository.saveAndFlush(blog);
//         if (blog.getId() == null) {
//             return BlogResp.create("500", "Failed");
//         }

//         blogRepository.saveAndFlush(blog);
//         return BlogResp.success().appendData(Optional.of(blog));
//     }

//     public BlogResp getBlogInfo(Blog blog) {

//         System.out.println(blog.toString());
//         Example<Blog> blogExample = Example.of(blog);
//         Optional<Blog> blogFinding = blogRepository.findOne(blogExample);

//         if (blogFinding.isPresent()) {
//             List<Blog> relatedBlogFinding = blogRepository.findFirst5ByCategory(blogFinding.get().getCategory());
//             Iterator<Blog> i = relatedBlogFinding.iterator();
//             while (i.hasNext()) {
//                 Blog nextBlog = i.next();
//                 if (nextBlog.getBlogId().equals(blog.getBlogId())) {
//                     i.remove();
//                     break;
//                 }
//             }
//             if (relatedBlogFinding.size() >= 5)
//                 i.remove();

//             System.out.println(relatedBlogFinding.toString());
//             return BlogResp.success().appendData(blogFinding).appendDataList(relatedBlogFinding);
//         } else {
//             return BlogResp.create("301", "Blog does not exist!");
//         }
//     }
// }