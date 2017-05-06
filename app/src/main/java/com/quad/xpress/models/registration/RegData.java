package com.quad.xpress.models.registration;

public class RegData
{
    private String id;

    private String user_name;

    private String phone_number;

    private String device_id;

    private String language;

    private String expiry_otp;

    private String otp;

    private String email_id;

    private String country;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUser_name ()
    {
        return user_name;
    }

    public void setUser_name (String user_name)
    {
        this.user_name = user_name;
    }

    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
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

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", user_name = "+user_name+", phone_number = "+phone_number+", device_id = "+device_id+", language = "+language+", expiry_otp = "+expiry_otp+", otp = "+otp+", email_id = "+email_id+", country = "+country+"]";
    }
}