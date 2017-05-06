package com.quad.xpress.models.AlertStream;

/**
 * Created by kural on 2/18/2017.
 */

public class AlertStreamResp {
    private String status;

    private AlertStreamData data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public AlertStreamData getData ()
    {
        return data;
    }

    public void setData (AlertStreamData data)
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
