package com.quad.xpress.models.TrendingSearch;

/**
 * Created by kural on 3/3/2017.
 */

public class Tsresp {

        private String status;

        private TSdata data;

        private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public TSdata getData ()
    {
        return data;
    }

    public void setData (TSdata data)
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
