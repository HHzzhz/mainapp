package com.ashago.mainapp.req;

import lombok.Data;

@Data
public class RegisterReq {
    private String email;
    private String token;
    private String userName;
    private Boolean subscribed;
}
