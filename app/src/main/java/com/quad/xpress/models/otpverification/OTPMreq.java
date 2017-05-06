package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class OTPMreq {
    String email_id, otp, device_id;

    public OTPMreq(String email_id, String otp, String device_id) {
        this.email_id = email_id;
        this.otp = otp;
        this.device_id = device_id;

    }
}
