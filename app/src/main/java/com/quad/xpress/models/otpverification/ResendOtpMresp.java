package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class ResendOtpMresp
{
    private String status;

    private ResendOtpMData[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public ResendOtpMData[] getData ()
    {
        return data;
    }

    public void setData (ResendOtpMData[] data)
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