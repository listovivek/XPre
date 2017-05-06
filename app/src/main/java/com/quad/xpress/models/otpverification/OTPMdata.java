package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */

public class OTPMdata
{
    private String token;

    private String device_id;

    private String email_id;

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
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
        return "ClassPojo [token = "+token+", device_id = "+device_id+", email_id = "+email_id+"]";
    }
}