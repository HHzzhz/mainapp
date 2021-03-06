package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Session;
import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.domain.UserProfile;
import com.ashago.mainapp.domain.VcodeScene;
import com.ashago.mainapp.exception.SessionNotValidException;
import com.ashago.mainapp.repository.SessionRepository;
import com.ashago.mainapp.repository.UserProfileRepository;
import com.ashago.mainapp.repository.UserRepository;
import com.ashago.mainapp.req.RegisterReq;
import com.ashago.mainapp.req.ResetPasswordReq;
import com.ashago.mainapp.req.UpdateUserProfileReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.resp.RespField;
import com.ashago.mainapp.util.CommonUtil;
import com.ashago.mainapp.util.RequestThreadLocal;
import com.ashago.mainapp.util.SnowFlake;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    @Value("${ashago.host}")
    private String ashagoHost;
    @Value("${ashago.user.emailverify.required:true}")
    private Boolean emailVerifyRequired;

    @Autowired
    private MailService mailService;
    @Autowired
    private VcodeService vcodeService;
    @Autowired
    private AvatarService avatarService;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CommonResp register(RegisterReq registerReq) {
        User user = User.builder().email(registerReq.getEmail()).build();
        Example<User> userExample = Example.of(user);

        if (userRepository.exists(userExample)) {
            return CommonResp.create("E001", "Email is already in use");
        }

        //初始化登录认证信息
        //初始化认证token
        String emailVerifyToken = UUID.randomUUID().toString();
        String userId = String.valueOf(snowFlake.nextId());
        user.setEmailVerified(Boolean.FALSE);
        user.setEmailVerifyToken(emailVerifyToken);
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
                .email(registerReq.getEmail())
                .subscribed(registerReq.getSubscribed()).build();
        userProfileRepository.saveAndFlush(userProfile);

        mailService.sendSimpleTextMail(user.getEmail(), "Please Confirm Your Email Address",
                StringUtils.join("Dear " + userProfile.getUserName() + ",\n" +
                                "\n" +
                                "Thank you for signing up for Asha Go!\n" +
                                "\n" +
                                "Please click on the following link to activate your account and join the Asha Go community:\n",
                        ashagoHost, "/getStarted", "?", "token=", emailVerifyToken, "&", "userId=", userId, "\n",
                        "\n" +
                                "If you have any questions, please feel free to contact us at info@ashago.com\n" +
                                "\n" +
                                "Regards,\n" +
                                "Asha Go Team"));

        return CommonResp.success("Thank you for signing up for Asha Go. A verification link has been sent to your email account. Please click to complete the sign up process.")
                .appendData(RespField.USER_ID, user.getUserId())
                .appendData(RespField.USER_NAME, userProfile.getUserName());
    }

    public CommonResp login(String email, String password) {
        Example<User> userExample = Example.of(
                User.builder().email(email).password(password).build()
        );
        Optional<User> userFinding = userRepository.findOne(userExample);
        if (userFinding.isPresent()) {
            if (Boolean.TRUE.equals(emailVerifyRequired) && Boolean.FALSE.equals(userFinding.get().getEmailVerified())) {
                return CommonResp.create("E014", "Email not verified.");
            }
            String userId = userFinding.get().getUserId();
            String t = UUID.randomUUID().toString();
            String sessionId = refreshSession(userId);
            return CommonResp.success()
                    .appendData(RespField.T, t)
                    .appendData(RespField.USER_ID, userId)
                    .appendData(RespField.SESSION_ID, sessionId);
        } else {
            return CommonResp.create("E003", "Incorrect email or password");
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
        try {


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
                        .appendData("email", userProfileFinding.get().getEmail())
                        .appendData("subscribed", userProfileFinding.get().getSubscribed())
                        .appendData("interesting", objectMapper.readValue(StringUtils.defaultIfBlank(userProfileFinding.get().getInteresting(), "[]"), List.class))
                        .appendData("birthday", userProfileFinding.get().getBirthday())
                        .appendData("gender", userProfileFinding.get().getGender())
                        .appendData("avatar", "http://" + userProfileFinding.get().getAvatar())
                        .appendData("requiredCompleted", computeRequiredCompleted(userProfileFinding.get()));
            } else {
                return CommonResp.create("E301", "用户不存在");
            }
        } catch (Exception e) {
            log.error("Get user profile error.", e);
            return CommonResp.create("E013", "Get user profile error.");
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
        //valid old password
        User user = User.builder().userId(userId).build();
        Optional<User> userFinding = userRepository.findOne(Example.of(user));
        if (!userFinding.isPresent()) {
            return CommonResp.create("E004", "user not found");
        }
        User userFound = userFinding.get();
        if (StringUtils.isBlank(userFound.getEmail()) || Boolean.TRUE.equals(userFound.getEmailVerified())) {
            return CommonResp.create("E005", "email not set or verified.");
        }

        if (StringUtils.isBlank(userFound.getPassword())) {
            return CommonResp.create("E006", "password not set");
        }

        if (!userFound.getPassword().equals(oldPassword)) {
            return CommonResp.create("E007", "old password is wrong.");
        }

        //set new password
        userFound.setPassword(newPassword);
        userRepository.saveAndFlush(userFound);
        return CommonResp.success();
    }

    public CommonResp loginWithFacebook(String fbUserId, String fbToken) {
        return null;
    }

    public CommonResp updateUserProfile(UpdateUserProfileReq updateUserProfileReq) {
        try {
            UserProfile userProfile = UserProfile.builder().userId(updateUserProfileReq.getUserId()).build();
            Optional<UserProfile> userProfileFinding = userProfileRepository.findOne(Example.of(userProfile));
            if (userProfileFinding.isPresent()) {
                userProfile = userProfileFinding.get();
                CommonUtil.executeIfNotNull(userProfile::setUserName, updateUserProfileReq.getUserName());
                CommonUtil.executeIfNotNull(userProfile::setBirthday, updateUserProfileReq.getBirthday());
                CommonUtil.executeIfNotNull(userProfile::setCity, updateUserProfileReq.getCity());
                CommonUtil.executeIfNotNull(userProfile::setCountry, updateUserProfileReq.getCountry());
                CommonUtil.executeIfNotNull(userProfile::setGender, updateUserProfileReq.getGender());
                CommonUtil.executeIfNotNull(userProfile::setInteresting, objectMapper.writeValueAsString(updateUserProfileReq.getInteresting()));
                CommonUtil.executeIfNotNull(userProfile::setSubscribed, updateUserProfileReq.getSubscribed());
                CommonUtil.executeIfNotNull(userProfile::setNationality, updateUserProfileReq.getNationality());
                userProfileRepository.saveAndFlush(userProfile);
                return CommonResp.success("Update successful!");
            } else {
                return CommonResp.create("E010", "User profile not found");
            }
        } catch (Exception e) {
            log.error("update user profile error.", e);
            return CommonResp.create("E012", "Update user profile error.");
        }
    }

    public void checkSession(String userId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(RequestThreadLocal.getSessionId())) {
            throw new SessionNotValidException();
        }
        Session session = Session.builder().sessionId(RequestThreadLocal.getSessionId())
                .userId(userId).build();
        Example<Session> sessionExample = Example.of(session);
        Optional<Session> sessionFinding = sessionRepository.findOne(sessionExample);
        sessionFinding.orElseThrow(SessionNotValidException::new);
    }

    public CommonResp uploadAvatar(String userId, MultipartFile avatar) {
        Optional<UserProfile> userProfileFinding = userProfileRepository.findOne(
                Example.of(UserProfile.builder().userId(userId).build()));
        userProfileFinding.ifPresent(userProfile -> {
            String avatarUrl = null;
            try {
                avatarUrl = avatarService.uploadAvatar(userId, avatar.getInputStream());
                userProfile.setAvatar(avatarUrl);
                userProfileRepository.saveAndFlush(userProfile);
            } catch (IOException e) {
                log.error("upload to oss failed.", e);
            }
        });
        return CommonResp.success();
    }

    public CommonResp verifyEmail(String userId, String token) {

        if (StringUtils.isAnyBlank(userId, token)) {
            return CommonResp.success();
        }

        Optional<User> userFinding = userRepository.findOne(Example.of(User.builder().userId(userId).emailVerifyToken(token).build()));
        userFinding.ifPresent(user -> {
            user.setEmailVerified(Boolean.TRUE);
            userRepository.saveAndFlush(user);
        });
        return CommonResp.success("Sign up successful!");
    }

    public CommonResp resetPassword(ResetPasswordReq resetPasswordReq) {
        Optional<User> userFinding = userRepository.findOne(Example.of(User.builder().email(resetPasswordReq.getEmail()).build()));
        if (!userFinding.isPresent()) {
            return CommonResp.create("E018", "user not exist");
        }
        User user = userFinding.get();
        //先验证vcode再重置密码
        Boolean verified = vcodeService.verifyVcode(user.getUserId(), resetPasswordReq.getSeqNo(), resetPasswordReq.getVcode(), VcodeScene.RESET_PASSWORD);
        if (Boolean.TRUE.equals(verified)) {
            //验证成功，重置密码
            user.setPassword(resetPasswordReq.getNewPassword());
            userRepository.saveAndFlush(user);
            return CommonResp.success();
        } else {
            return CommonResp.create("E017", "Vcode verify failed. Sorry plz try again.");
        }
    }
}
