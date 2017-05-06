package com.quad.xpress.models.contacts;

/**
 * Created by kural on 2/15/2017.
 */

public class ContactsData {
    private String id;

    private String remainder;

    private String user_name;

    private String phone_number;

    private String status;

    private String created_on;

    private String profile_image;

    private String notification;

    private String user_id;

    private String language;

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

    public String getRemainder ()
    {
        return remainder;
    }

    public void setRemainder (String remainder)
    {
        this.remainder = remainder;
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

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getCreated_on ()
    {
        return created_on;
    }

    public void setCreated_on (String created_on)
    {
        this.created_on = created_on;
    }

    public String getProfile_image ()
    {
        return profile_image;
    }

    public void setProfile_image (String profile_image)
    {
        this.profile_image = profile_image;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
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
        return "ClassPojo [id = "+id+", remainder = "+remainder+", user_name = "+user_name+", phone_number = "+phone_number+", status = "+status+", created_on = "+created_on+", profile_image = "+profile_image+", notification = "+notification+", user_id = "+user_id+", language = "+language+", email_id = "+email_id+", country = "+country+"]";
    }

}
