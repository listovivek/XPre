package com.quad.xpress.models.clickResponce;

/**
 * Created by kural on 8/2/2016.
 */
public class Like_Data {

    private String message;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+"]";
    }
}
