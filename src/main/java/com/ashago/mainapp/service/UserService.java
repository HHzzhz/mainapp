package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.domain.UserProfile;
import com.ashago.mainapp.repository.UserProfileRepository;
import com.ashago.mainapp.repository.UserRepository;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;


    @Autowired
    private WxOpenComponentService wxOpenComponentService;
    @Value("${wx.appid}")
    private String wxAppId;


    @Autowired
    private MailService mailService;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    public CommonResp register(RegisterReq registerReq) {
        User user = User.builder().email(registerReq.getEmail()).build();
        Example<User> userExample = Example.of(user);

        if (userRepository.exists(userExample)) {
            return CommonResp.create("E001", "User exist");
        }

        //初始化登录认证信息
        String userId = String.valueOf(snowFlake.nextId());
        user.setEmailVerified(Boolean.FALSE);
        user.setToken(registerReq.getToken());
        user.setUserId(userId);
        userRepository.saveAndFlush(user);

        if (user.getId() == null) {
            return CommonResp.create("E002", "sign up failed");
        }

        //生成user_profile
        UserProfile userProfile = UserProfile.builder()
                .userId(userId)
                .userName(registerReq.getUserName())
                .subscribed(registerReq.getSubscribed()).build();
        userProfileRepository.saveAndFlush(userProfile);

        //TODO: 确认邮箱的地址
        mailService.sendSimpleTextMail(user.getEmail(), "Ashago需要您的确认", "请确认您的邮箱是否正确");

        return CommonResp.success()
                .appendData("userId", user.getUserId())
                .appendData("userName", userProfile.getUserName());
    }

    public CommonResp login(User user) {
        Example<User> userExample = Example.of(user,
                ExampleMatcher.matching()
                        .withIgnorePaths("subscribed")
                        .withIgnorePaths("userName")
                        .withIgnorePaths("activated")
        );
        Optional<User> userFinding = userRepository.findOne(userExample);
        if (userFinding.isPresent()) {
            String t = UUID.randomUUID().toString();
            return CommonResp.success()
                    .appendData("t", t)
                    .appendData("userId", userFinding.get().getUserId())
                    .appendData("sessionId", t.toUpperCase());
        } else {
            return CommonResp.create("E003", "email or pass failed");
        }
    }

    public CommonResp loginWithWechat(String code) {
        try {
            WxMpOAuth2AccessToken accessToken = wxOpenComponentService.oauth2getAccessToken(wxAppId, code);
            String openId = accessToken.getOpenId();

            User user = User.builder().wxOpenId(openId).build();
            Example<User> userExample = Example.of(user);
            Optional<User> userFinding = userRepository.findOne(userExample);
            if (userFinding.isPresent()) {
                String t = UUID.randomUUID().toString();
                return CommonResp.success()
                        .appendData("t", t)
                        .appendData("userId", userFinding.get().getUserId())
                        .appendData("sessionId", t.toUpperCase());
            } else {
                //登录成功，处理注册流程
                String userId = String.valueOf(snowFlake.nextId());
                user.setUserId(userId);
                userRepository.save(user);
                UserProfile userProfile = UserProfile.builder()
                        .userId(userId)
                        .build();
                userProfileRepository.save(userProfile);
                userRepository.flush();
                userProfileRepository.flush();
                String t = UUID.randomUUID().toString();
                return CommonResp.success()
                        .appendData("t", t)
                        .appendData("userId", userId)
                        .appendData("sessionId", t.toUpperCase());
            }

        } catch (Exception e) {
            log.error("wx process err.", e);
            return CommonResp.create("303", "未知错误");
        }

    }

    public CommonResp getUserProfile(String userId) {

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        Example<UserProfile> userProfileExample = Example.of(userProfile);
        Optional<UserProfile> userProfileFinding = userProfileRepository.findOne(userProfileExample);
        if (userProfileFinding.isPresent()) {

            return CommonResp.success()
                    .appendData("userId", userProfileFinding.get().getUserId())
                    .appendData("city", userProfileFinding.get().getCity())
                    .appendData("country", userProfileFinding.get().getCountry())
                    .appendData("nationality", userProfileFinding.get().getNationality())
                    .appendData("userName", userProfileFinding.get().getUserName())
                    .appendData("subscribed", userProfileFinding.get().getSubscribed())
                    .appendData("interesting", userProfileFinding.get().getInteresting())
                    .appendData("requiredCompleted", computeRequiredCompleted(userProfileFinding.get()));
        } else {
            return CommonResp.create("301", "用户不存在");
        }
    }

    private Boolean computeRequiredCompleted(UserProfile userProfile) {
        return userProfile.getCity() != null
                && userProfile.getCountry() != null
                && userProfile.getNationality() != null
                && userProfile.getUserName() != null
                && userProfile.getInteresting() != null;
    }
}
