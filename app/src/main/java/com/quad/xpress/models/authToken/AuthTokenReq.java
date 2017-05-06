package com.quad.xpress.models.authToken;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class AuthTokenReq {
    String email_id, device_id;

    public AuthTokenReq(String email_id, String device_id) {

        this.email_id = email_id;
        this.device_id = device_id;
    }
}