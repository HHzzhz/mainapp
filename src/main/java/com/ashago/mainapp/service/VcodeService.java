package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.User;
import com.ashago.mainapp.domain.Vcode;
import com.ashago.mainapp.domain.VcodeScene;
import com.ashago.mainapp.domain.VcodeType;
import com.ashago.mainapp.repository.UserRepository;
import com.ashago.mainapp.repository.VcodeRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.SnowFlake;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VcodeService {

    @Autowired
    private MailService mailService;
    @Autowired
    private VcodeRepository vcodeRepository;
    @Autowired
    private UserRepository userRepository;

    private final SnowFlake snowFlake = new SnowFlake(10, 10);

    public CommonResp sendEmailVcode(String email, VcodeScene scene) {
        //先生产vcode
        Optional<User> userFinding = userRepository.findOne(Example.of(User.builder().email(email).build()));
        if (!userFinding.isPresent()) {
            return CommonResp.create("E015", "This email not registered.");
        }
        String userId = userFinding.get().getUserId();

        String code = RandomStringUtils.randomAlphanumeric(6);
        String seqNo = StringUtils.join(snowFlake.nextId());
        Vcode vcode = Vcode.builder().scene(scene)
                .seqNo(seqNo)
                .userId(userId)
                .code(code)
                .vcodeType(VcodeType.EMAIL)
                .verified(Boolean.FALSE).build();
        vcodeRepository.saveAndFlush(vcode);

        //发送vcode
        mailService.sendSimpleTextMail(email, "Your Email Verify Code", StringUtils.join("Your email verify code is:", code,". Please be careful not to leak it."));

        return CommonResp.success()
                .appendData("seqNo", seqNo)
                .appendData("userId", userId);
    }

    public Boolean verifyVcode(String userId, String seqNo, String code, VcodeScene scene) {
        Optional<Vcode> vcodeFinding = vcodeRepository.findOne(
                Example.of(Vcode.builder()
                        .userId(userId)
                        .code(code)
                        .seqNo(seqNo)
                        .scene(scene).build()));
        vcodeFinding.ifPresent(vcode -> {
            vcode.setVerified(Boolean.TRUE);
            vcodeRepository.saveAndFlush(vcode);
        });
        return vcodeFinding.isPresent();
    }
}
