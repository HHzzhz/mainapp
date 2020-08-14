package com.ashago.mainapp.req;

import lombok.Data;

@Data
public class LoginWithFacebookReq {
    private String fbUserId;
    private String fbToken;
}
