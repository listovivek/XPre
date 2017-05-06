package com.quad.xpress.models.abuse_resp;

/**
 * Created by kural on 8/22/2016.
 */
public class Abuse_response {

        private String status;

        private Abuse_data[] data;

        private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Abuse_data[] getData ()
    {
        return data;
    }

    public void setData (Abuse_data[] data)
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
