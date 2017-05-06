package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class ResendOtpMreq {
    String email_id, device_id;

    public ResendOtpMreq(String email_id, String device_id) {
        this.email_id = email_id;
        this.device_id = device_id;
    }
}
