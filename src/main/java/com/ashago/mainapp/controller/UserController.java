package com.ashago.mainapp.controller;

import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/user/register")
    public CommonResp register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(path = "/user/login")
    public CommonResp login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        CommonResp resp = userService.login(user);
        Cookie cookie = new Cookie("sessionId", resp.getData("sessionId").toString());
        httpServletResponse.addCookie(cookie);
        return resp;
    }
}
