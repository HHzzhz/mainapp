package com.ashago.mainapp.controller;

import com.ashago.mainapp.req.RecommendAppendReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @PostMapping("/recommend/append")
    public CommonResp recommendAppend(@RequestBody @Valid RecommendAppendReq recommendAppendReq) {
        return recommendService.append(recommendAppendReq.getTitle(), recommendAppendReq.getCover(), recommendAppendReq.getBlogId(), recommendAppendReq.getPriority());
    }

    @GetMapping("/recommend/list")
    public CommonResp recommendList() {
        return recommendService.list();
    }

    @PostMapping("/recommend/remove")
    public CommonResp recommendRemove(@RequestParam String recommendId) {
        return recommendService.remove(recommendId);
    }
}
