package com.quad.xpress.models.otpverification;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class OTPMresp
{
    private String status;

    private OTPMdata[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public OTPMdata[] getData ()
    {
        return data;
    }

    public void setData (OTPMdata[] data)
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