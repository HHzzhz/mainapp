package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public CommonResp register(@RequestBody RegisterReq registerReq) {
        return userService.register(registerReq);
    }

    @PostMapping("/user/login")
    public CommonResp login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        CommonResp resp = userService.login(user);
        Cookie cookie = new Cookie("sessionId", resp.getData("sessionId").toString());
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @PostMapping("/user/login-with-wechat")
    public CommonResp loginWithWechat(@RequestBody Map<String, String> param, HttpServletResponse httpServletResponse) {
        String code = param.get("code");
        CommonResp resp = userService.loginWithWechat(code);
        Cookie cookie = new Cookie("sessionId", resp.getData("sessionId").toString());
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @GetMapping("/user/profile")
    public CommonResp getUserProfile(@RequestParam String userId) {

        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/change-password")
    public CommonResp
}
