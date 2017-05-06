package com.quad.xpress.models.AlertStream;

/**
 * Created by kural on 2/18/2017.
 */

public class AlertStreamData {

    private AlertStreamRecords[] Records;

    private String last;

    private String CountOfRecords;

    public AlertStreamRecords[] getRecords ()
    {
        return Records;
    }

    public void setRecords (AlertStreamRecords[] Records)
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

    public String getCountOfRecords ()
    {
        return CountOfRecords;
    }

    public void setCountOfRecords (String CountOfRecords)
    {
        this.CountOfRecords = CountOfRecords;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Records = "+Records+", last = "+last+", CountOfRecords = "+CountOfRecords+"]";
    }
}
