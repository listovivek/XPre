package com.quad.xpress.models.featuredVideos;

/**
 * Created by kural on 1/5/2017.
 */

public class featuredResp {
    private String status;

    private featuredData[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public featuredData[] getData ()
    {
        return data;
    }

    public void setData (featuredData[] data)
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
