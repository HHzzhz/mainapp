package com.ashago.mainapp.controller;

import com.ashago.mainapp.req.*;
import com.ashago.mainapp.resp.RespField;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public CommonResp register(@RequestBody @Valid RegisterReq registerReq) {
        return userService.register(registerReq);
    }

    @PostMapping("/user/login")
    public CommonResp login(@RequestBody @Valid LoginReq loginReq, HttpServletResponse httpServletResponse) {
        CommonResp resp = userService.login(loginReq.getEmail(), loginReq.getPassword());
        Cookie cookie = new Cookie(RespField.SESSION_ID, resp.getData(RespField.SESSION_ID));
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @PostMapping("/user/login-with-wechat")
    public CommonResp loginWithWechat(@RequestBody @Valid LoginWithWechatReq loginWithWechatReq, HttpServletResponse httpServletResponse) {
        String code = loginWithWechatReq.getCode();
        CommonResp resp = userService.loginWithWechat(code);
        Cookie cookie = new Cookie(RespField.SESSION_ID, resp.getData(RespField.SESSION_ID));
        httpServletResponse.addCookie(cookie);
        return resp;
    }

    @PostMapping("/user/login-with-facebook")
    public CommonResp loginWithFacebook(@RequestBody @Valid LoginWithFacebookReq loginWithFacebookReq) {
        return userService.loginWithFacebook(loginWithFacebookReq.getFbUserId(), loginWithFacebookReq.getFbToken());
    }

    @GetMapping("/user/profile")
    public CommonResp getUserProfile(@RequestParam String userId) {
        userService.checkSession(userId);
        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/change-password")
    public CommonResp changePassword(@RequestBody @Valid ChangePasswordReq changePasswordReq) {
        userService.checkSession(changePasswordReq.getUserId());
        return userService.changePassword(changePasswordReq.getUserId(), changePasswordReq.getOldPassword(), changePasswordReq.getNewPassword());
    }

    @PostMapping("/user/update-profile")
    public CommonResp updateUserProfile(@RequestBody @Valid UpdateUserProfileReq updateUserProfileReq) {
        userService.checkSession(updateUserProfileReq.getUserId());
        return userService.updateUserProfile(updateUserProfileReq);
    }

    @PostMapping("/user/upload-avatar")
    public CommonResp uploadAvatar(@RequestBody @Valid UploadAvatarReq uploadAvatarReq) {
        userService.checkSession(uploadAvatarReq.getUserId());
        return userService.uploadAvatar(uploadAvatarReq.getUserId(), uploadAvatarReq.getAvatar());
    }

    @GetMapping("/user/email-verify")
    public CommonResp verifyEmail(@RequestParam String userId, @RequestParam String token) {
        return userService.verifyEmail(userId, token);
    }
}
