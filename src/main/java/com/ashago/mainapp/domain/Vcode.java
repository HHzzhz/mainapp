package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vcode {
    @Id
    @GeneratedValue
    private Integer id;
    private String userId;
    private VcodeScene scene;
    private String seqNo;
    private String code;
    private Boolean verified;
    private VcodeType vcodeType;
}
