package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Recommend;
import com.ashago.mainapp.repository.RecommendRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecommendService {
    @Autowired
    private RecommendRepository recommendRepository;
    private final SnowFlake snowFlake = new SnowFlake(10, 10);


    public CommonResp append(String title, String cover, String blogId, Integer priority, String serviceId, Integer type) {
        String recommendId = StringUtils.join(snowFlake.nextId());
        Recommend recommend = Recommend.builder().recommendTitle(title).recommendId(recommendId)
                .blogId(blogId).cover(cover).priority(priority)
                .serviceId(serviceId)
                .type(type).build();
        recommendRepository.saveAndFlush(recommend);
        return CommonResp.success();
    }

    public CommonResp list() {
        return CommonResp.success().appendData("recommendList", recommendRepository.findAll());
    }

    public CommonResp remove(String recommendId) {
        Optional<Recommend> recommendOptional = recommendRepository.findOne(Example.of(Recommend.builder().recommendId(recommendId).build()));
        recommendOptional.ifPresent(recommend -> recommendRepository.deleteById(recommend.getId()));
        return CommonResp.success();
    }
}
