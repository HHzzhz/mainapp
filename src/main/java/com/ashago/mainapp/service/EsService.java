package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.EsBlog;
import com.ashago.mainapp.esrepository.EsBlogRepository;
import com.ashago.mainapp.resp.BlogResp;
import com.ashago.mainapp.util.SnowFlake;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Deprecated
public class EsService {
    @Autowired
    private EsBlogRepository esBlogRepository;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    private final Pageable pageable = PageRequest.of(0,10);

    public BlogResp searchContent(String content) {
        if (StringUtils.isBlank(content)) {
            return BlogResp.success().appendDataList(esBlogRepository.findAll(pageable).getContent());
        }
        Page<EsBlog> esblogs = esBlogRepository.findByContent(content, pageable);
        List<EsBlog> esblogsList = esblogs.getContent();
        for (EsBlog esBlog : esblogsList) {
            esBlog.setDate(null);
        }
        if (!esblogsList.isEmpty()) {
            return BlogResp.success().appendDataList(esblogsList);
        } else {
            return BlogResp.create("404", "EsBlog does not exist!");
        }
    }

    public BlogResp searchTag(String tag) {
        Page<EsBlog> esblogs = esBlogRepository.findByTag(tag, pageable);
        List<EsBlog> esblogsList = esblogs.getContent();
        if (!esblogsList.isEmpty()) {
            return BlogResp.success().appendDataList(esblogsList);
        } else {
            return BlogResp.create("404", "EsBlog does not exist!");
        }
    }

    public BlogResp addBlog(EsBlog esblog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        esblog.setDate(sdf.format(new Date()));
        String blogId = String.valueOf(snowFlake.nextId());
        esblog.setId(blogId);

        if (esBlogRepository.existsById(blogId)) {
            return BlogResp.create("409", "EsBlog exist");
        }
        esBlogRepository.save(esblog);
        return BlogResp.success().appendData(Optional.of(esblog));
    }
}
