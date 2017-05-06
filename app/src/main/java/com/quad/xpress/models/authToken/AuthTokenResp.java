package com.quad.xpress.models.authToken;

/**
 * Created by Venkatesh on 26-05-16.
 */
public class AuthTokenResp
{
    private String status;

    private AuthTokenData[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public AuthTokenData[] getData ()
    {
        return data;
    }

    public void setData (AuthTokenData[] data)
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