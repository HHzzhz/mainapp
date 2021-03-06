package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ServiceProvider {
    @Id
    @GeneratedValue
    private Integer id;
    //雪花id
    private String serviceProviderId;
    //服务提供商名字
    private String name;
    //service provider类型
    private ServiceProviderType type;
    //id信息，个人为身份证，企业为营业执照号码
    private String idNo;
    //id信息的照片
    private String idImgList;
    //联系号码
    private String mobile;
}
