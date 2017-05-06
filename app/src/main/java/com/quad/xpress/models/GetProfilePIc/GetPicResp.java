package com.quad.xpress.models.GetProfilePIc;

/**
 * Created by kural on 1/24/2017.
 */

public class GetPicResp {
    private String status;

    private PicData data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public PicData getData ()
    {
        return data;
    }

    public void setData (PicData data)
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
