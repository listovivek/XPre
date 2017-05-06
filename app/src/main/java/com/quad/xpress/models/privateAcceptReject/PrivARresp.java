package com.quad.xpress.models.privateAcceptReject;

/**
 * Created by Venkatesh on 17-06-16.
 */
public class PrivARresp
{
    private String status;

    private PrivARdata[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public PrivARdata[] getData ()
    {
        return data;
    }

    public void setData (PrivARdata[] data)
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
