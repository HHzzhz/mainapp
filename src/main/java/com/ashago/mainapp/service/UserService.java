package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Session;
import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.domain.UserProfile;
import com.ashago.mainapp.exception.SessionNotValidException;
import com.ashago.mainapp.repository.SessionRepository;
import com.ashago.mainapp.repository.UserProfileRepository;
import com.ashago.mainapp.repository.UserRepository;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.req.UpdateUserProfileReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.resp.RespField;
import com.ashago.mainapp.util.RequestThreadLocal;
import com.ashago.mainapp.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
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
    private SessionRepository sessionRepository;

    private final WxOpenService wxOpenService = new WxOpenServiceImpl();

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
        user.setPassword(registerReq.getPassword());
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
        //mailService.sendSimpleTextMail(user.getEmail(), "Ashago需要您的确认", "请确认您的邮箱是否正确");

        return CommonResp.success()
                .appendData(RespField.USER_ID, user.getUserId())
                .appendData(RespField.USER_NAME, userProfile.getUserName());
    }

    public CommonResp login(String email, String password) {
        Example<User> userExample = Example.of(
                User.builder().email(email).password(password).build()
        );
        Optional<User> userFinding = userRepository.findOne(userExample);
        if (userFinding.isPresent()) {
            String userId = userFinding.get().getUserId();
            String t = UUID.randomUUID().toString();
            String sessionId = refreshSession(userId);
            return CommonResp.success()
                    .appendData(RespField.T, t)
                    .appendData(RespField.USER_ID, userId)
                    .appendData(RespField.SESSION_ID, sessionId);
        } else {
            return CommonResp.create("E003", "email or pass failed");
        }
    }

    private String refreshSession(String userId) {
        Session session = Session.builder().userId(userId).build();
        Example<Session> sessionExample = Example.of(session);
        Optional<Session> sessionFinding = sessionRepository.findOne(sessionExample);
        String newSessionId = UUID.randomUUID().toString();
        if (sessionFinding.isPresent()) {
            sessionFinding.get().setSessionId(newSessionId);
            sessionRepository.saveAndFlush(sessionFinding.get());
        } else {
            session.setSessionId(newSessionId);
            sessionRepository.saveAndFlush(session);
        }
        return newSessionId;
    }

    public CommonResp loginWithWechat(String code) {
        try {
            WxMpOAuth2AccessToken accessToken = wxOpenService.getWxOpenComponentService().oauth2getAccessToken(wxAppId, code);
            String openId = accessToken.getOpenId();

            User user = User.builder().wxOpenId(openId).build();
            Example<User> userExample = Example.of(user);
            Optional<User> userFinding = userRepository.findOne(userExample);
            if (userFinding.isPresent()) {
                String t = UUID.randomUUID().toString();
                return CommonResp.success()
                        .appendData(RespField.T, t)
                        .appendData(RespField.USER_ID, userFinding.get().getUserId())
                        .appendData(RespField.SESSION_ID, t.toUpperCase());
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
                        .appendData(RespField.T, t)
                        .appendData(RespField.USER_ID, userId)
                        .appendData(RespField.SESSION_ID, t.toUpperCase());
            }

        } catch (Exception e) {
            log.error("wx process err.", e);
            return CommonResp.create("303", "未知错误");
        }

    }

    public CommonResp getUserProfile(String userId) {
        UserProfile userProfile = UserProfile.builder().userId(userId).build();
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

    public CommonResp changePassword(String userId, String oldPassword, String newPassword) {

        return null;
    }

    public CommonResp loginWithFacebook(String fbUserId, String fbToken) {
        return null;
    }

    public CommonResp updateUserProfile(UpdateUserProfileReq updateUserProfileReq) {
        return null;
    }

    public void checkSession(String userId) {
        if (StringUtils.isBlank(RequestThreadLocal.getSessionId())) {
            throw new SessionNotValidException();
        }
        Session session = Session.builder().sessionId(RequestThreadLocal.getSessionId())
                .userId(userId).build();
        Example<Session> sessionExample = Example.of(session);
        Optional<Session> sessionFinding = sessionRepository.findOne(sessionExample);
        sessionFinding.orElseThrow(SessionNotValidException::new);
    }
}
