package com.ashago.mainapp.controller;

import com.ashago.mainapp.resp.RespField;
import com.ashago.mainapp.req.ChangePasswordReq;
import com.ashago.mainapp.req.LoginReq;
import com.ashago.mainapp.req.LoginWithWechatReq;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/user/register")
    public CommonResp register(@RequestBody @Validated RegisterReq registerReq) {
        return userService.register(registerReq);
    }

    @PostMapping("/user/login")
    public CommonResp login(@RequestBody @Validated LoginReq loginReq, HttpServletResponse httpServletResponse) {
        CommonResp resp = userService.login(loginReq.getEmail(), loginReq.getPassword());
        Cookie cookie = new Cookie(RespField.SESSION_ID, resp.getData(RespField.SESSION_ID));
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @PostMapping("/user/login-with-wechat")
    public CommonResp loginWithWechat(@RequestBody @Validated LoginWithWechatReq loginWithWechatReq, HttpServletResponse httpServletResponse) {
        String code = loginWithWechatReq.getCode();
        CommonResp resp = userService.loginWithWechat(code);
        Cookie cookie = new Cookie(RespField.SESSION_ID, resp.getData(RespField.SESSION_ID));
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @GetMapping("/user/profile")
    public CommonResp getUserProfile(@RequestParam String userId) {

        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/change-password")
    public CommonResp changePassword(@RequestBody @Validated ChangePasswordReq changePasswordReq) {
        return userService.changePassword(changePasswordReq.getUserId(), changePasswordReq.getOldPassword(), changePasswordReq.getNewPassword());
    }
}
