package com.quad.xpress.models.profile_pic;

/**
 * Created by kural on 1/10/2017.
 */

public class profilepicResp {
    private String status;

    private profilePicData data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public profilePicData getData ()
    {
        return data;
    }

    public void setData (profilePicData data)
    {
        this.data = data;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", data = "+data+", code = "+code+"]";
    }
}
