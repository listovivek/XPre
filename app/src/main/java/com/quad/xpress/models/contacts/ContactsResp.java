package com.quad.xpress.models.contacts;

/**
 * Created by kural on 2/15/2017.
 */

public class ContactsResp {
    private String status;

    private ContactsData[] data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public ContactsData[] getData ()
    {
        return data;
    }

    public void setData (ContactsData[] data)
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
