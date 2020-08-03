package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.repository.UserRepository;
import com.ashago.mainapp.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public CommonResp register(User user) {
        Example<User> userExample = Example.of(user,
                ExampleMatcher.matching()
                        .withIgnorePaths("userId")
                        .withIgnorePaths("token")
                        .withIgnorePaths("subscribed")
                        .withIgnorePaths("userName")
                        .withIgnorePaths("activated"));

        if (userRepository.exists(userExample)) {
            return CommonResp.create("E001", "User exist");
        }
        user.setActivated(Boolean.FALSE);
        if (user.getSubscribed() == null) {
            user.setSubscribed(Boolean.FALSE);
        }
        userRepository.saveAndFlush(user);
        if (user.getId() == null) {
            return CommonResp.create("E002", "sign up failed");
        }

        //TODO:发送邮件


        return CommonResp.success()
                .appendData("userId", user.getUserId())
                .appendData("userName", user.getUserName());
    }

    public CommonResp login(User user){
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
}
