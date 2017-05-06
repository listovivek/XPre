package com.quad.xpress.models.counter;

/**
 * Created by kural on 1/12/2017.
 */

public class CounterResp {

    private String status;

    private CounterData data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public CounterData getData ()
    {
        return data;
    }

    public void setData (CounterData data)
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
