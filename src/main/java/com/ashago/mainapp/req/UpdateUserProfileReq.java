package com.ashago.mainapp.req;

import com.ashago.mainapp.domain.Gender;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateUserProfileReq {
   @NotBlank
   private String userId;
   private String userName;
   private Gender gender;
   private LocalDate birthday;
   private String nationality;
   private String country;
   private String city;
   private List<String> interesting;
   private Boolean subscribed;
}
