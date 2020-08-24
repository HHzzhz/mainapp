package com.ashago.mainapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue
    private Integer id;
    private String userId;
    private String userName;
    private Gender gender;
    private LocalDate birthday;
    private String email;
    private String nationality;
    private String country;
    private String city;
    private String interesting;
    private Boolean subscribed;
    private String avatar;
}
