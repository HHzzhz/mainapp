package com.ashago.mainapp.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.ashago.mainapp.config.AvatarStoreConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AvatarService {
    @Autowired
    private AvatarStoreConfig avatarStoreConfig;

    private volatile OSS ossClient = null;

    public String uploadAvatar(String userId, InputStream avatarIs) {
        OSS client = getOssClient();
        String avatarOssKey = StringUtils.join("avatar", "/", userId, ".jpg");
        client.putObject(avatarStoreConfig.getBucketName(), avatarOssKey, avatarIs);

        return StringUtils.join(avatarStoreConfig.getBucketName(), ".", avatarStoreConfig.getEndpoint(), "/", avatarOssKey);
    }

    private OSS getOssClient() {
        if (ossClient == null) {
            synchronized (this) {
                if (ossClient == null) {
                    ossClient = new OSSClient(avatarStoreConfig.getEndpoint(), avatarStoreConfig.getAccessKeyId(), avatarStoreConfig.getAccessKeySecret());
                }
            }
        }
        return ossClient;
    }
}
