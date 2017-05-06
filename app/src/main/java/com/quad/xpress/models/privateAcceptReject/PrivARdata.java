package com.quad.xpress.models.privateAcceptReject;

/**
 * Created by Venkatesh on 17-06-16.
 */
public class PrivARdata
{
    private String status;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+"]";
    }
}