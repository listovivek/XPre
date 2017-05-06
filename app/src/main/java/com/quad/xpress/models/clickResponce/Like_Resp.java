package com.quad.xpress.models.clickResponce;

/**
 * Created by kural on 8/2/2016.
 */
public class Like_Resp {

        private String status;

        private Like_Data[] data;

        private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Like_Data[] getData ()
    {
        return data;
    }

    public void setData (Like_Data[] data)
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

