package com.quad.xpress.models.receivedFiles.Plist_Emotion;

/**
 * Created by kural on 9/3/2016.
 */
public class Data {

    private Records[] Records;

    private String last;

    public Records[] getRecords ()
    {
        return Records;
    }

    public void setRecords (Records[] Records)
    {
        this.Records = Records;
    }

    public String getLast ()
    {
        return last;
    }

    public void setLast (String last)
    {
        this.last = last;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Records = "+Records+", last = "+last+"]";
    }
}
