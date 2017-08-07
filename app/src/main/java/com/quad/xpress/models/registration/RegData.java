package com.quad.xpress.models.registration;

public class RegData
{
    private String gcm_id;

    private String phone_number;

    private String mobile_modelname;

    private String device_id;

    private String mobile_os;

    private String country;

    private String id;

    private String user_name;

    private String mobile_version;

    private String remainder;

    private String DocumentRoot;

    private String token;

    private String notification;

    private String language;

    private String expiry_otp;

    private String otp;

    private String email_id;

    private String is_edit;

    public String getGcm_id ()
    {
        return gcm_id;
    }

    public void setGcm_id (String gcm_id)
    {
        this.gcm_id = gcm_id;
    }

    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getMobile_modelname ()
    {
        return mobile_modelname;
    }

    public void setMobile_modelname (String mobile_modelname)
    {
        this.mobile_modelname = mobile_modelname;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
    }

    public String getMobile_os ()
    {
        return mobile_os;
    }

    public void setMobile_os (String mobile_os)
    {
        this.mobile_os = mobile_os;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

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

    public String getMobile_version ()
    {
        return mobile_version;
    }

    public void setMobile_version (String mobile_version)
    {
        this.mobile_version = mobile_version;
    }

    public String getRemainder ()
    {
        return remainder;
    }

    public void setRemainder (String remainder)
    {
        this.remainder = remainder;
    }

    public String getDocumentRoot ()
    {
        return DocumentRoot;
    }

    public void setDocumentRoot (String DocumentRoot)
    {
        this.DocumentRoot = DocumentRoot;
    }

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
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

    public String getIs_edit ()
    {
        return is_edit;
    }

    public void setIs_edit (String is_edit)
    {
        this.is_edit = is_edit;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gcm_id = "+gcm_id+", phone_number = "+phone_number+", mobile_modelname = "+mobile_modelname+", device_id = "+device_id+", mobile_os = "+mobile_os+", country = "+country+", id = "+id+", user_name = "+user_name+", mobile_version = "+mobile_version+", remainder = "+remainder+", DocumentRoot = "+DocumentRoot+", token = "+token+", notification = "+notification+", language = "+language+", expiry_otp = "+expiry_otp+", otp = "+otp+", email_id = "+email_id+", is_edit = "+is_edit+"]";
    }
}