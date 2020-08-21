package com.ashago.mainapp.req;

import com.ashago.mainapp.domain.Gender;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserProfileReq {
   @NotBlank
   private String userId;
   private String userName;
   private Gender gender;
   private Integer age;
   private String nationality;
   private String country;
   private String city;
   private String interesting;
   private Boolean subscribed;
}
