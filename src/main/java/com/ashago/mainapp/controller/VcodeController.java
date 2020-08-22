package com.ashago.mainapp.controller;

import com.ashago.mainapp.req.SendEmailVcodeReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.VcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class VcodeController {
    @Autowired
    private VcodeService vcodeService;

    @PostMapping("/vcode/send-email")
    public CommonResp sendEmailVcode(@RequestBody @Valid SendEmailVcodeReq sendEmailVcodeReq) {
        return vcodeService.sendEmailVcode(sendEmailVcodeReq.getEmail(), sendEmailVcodeReq.getScene());
    }
}
