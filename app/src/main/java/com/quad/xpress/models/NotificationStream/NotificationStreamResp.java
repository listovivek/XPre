package com.quad.xpress.models.NotificationStream;

/**
 * Created by kural on 2/10/2017.
 */

public class NotificationStreamResp {
    private String status;

    private NotificationStreamData[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public NotificationStreamData[] getData ()
    {
        return data;
    }

    public void setData (NotificationStreamData[] data)
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
