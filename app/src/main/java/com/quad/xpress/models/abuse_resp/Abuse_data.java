package com.quad.xpress.models.abuse_resp;

/**
 * Created by kural on 8/22/2016.
 */
public class Abuse_data {
    private String success;

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [success = "+success+"]";
    }
    }
