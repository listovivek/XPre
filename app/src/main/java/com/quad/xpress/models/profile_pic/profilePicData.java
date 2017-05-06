package com.quad.xpress.models.profile_pic;

/**
 * Created by kural on 1/23/2017.
 */

public class profilePicData {
    private String filepath;

    private String authtoken;

    private String email;

    public String getFilepath ()
    {
        return filepath;
    }

    public void setFilepath (String filepath)
    {
        this.filepath = filepath;
    }

    public String getAuthtoken ()
    {
        return authtoken;
    }

    public void setAuthtoken (String authtoken)
    {
        this.authtoken = authtoken;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [filepath = "+filepath+", authtoken = "+authtoken+", email = "+email+"]";
    }
}
