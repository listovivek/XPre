package com.quad.xpress.models.vp_guide;

/**
 * Created by kural on 12/2/2016.
 */

public class guide_status {

        private String status;

        private guide_data[] data;

        private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public guide_data[] getData ()
    {
        return data;
    }

    public void setData (guide_data[] data)
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
