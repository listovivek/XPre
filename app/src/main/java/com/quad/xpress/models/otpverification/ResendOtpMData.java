package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class ResendOtpMData
{
    private String email;

    private String device_id;

    private String firstname;

    private String expiry_otp;

    private String otp;

    private String email_id;

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
    }

    public String getFirstname ()
    {
        return firstname;
    }

    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    public String getExpiry_otp ()
    {
        return expiry_otp;
    }

    public void setExpiry_otp (String expiry_otp)
    {
        this.expiry_otp = expiry_otp;
    }

    public String getOtp ()
    {
        return otp;
    }

    public void setOtp (String otp)
    {
        this.otp = otp;
    }

    public String getEmail_id ()
    {
        return email_id;
    }

    public void setEmail_id (String email_id)
    {
        this.email_id = email_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [email = "+email+", device_id = "+device_id+", firstname = "+firstname+", expiry_otp = "+expiry_otp+", otp = "+otp+", email_id = "+email_id+"]";
    }
}