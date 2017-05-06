package com.quad.xpress.models.tagList;

/**
 * Created by kural on 7/20/2016.
 */
public class TagListData
{
    private TagListRecords[] Records;

    public TagListRecords[] getRecords ()
    {
        return Records;
    }

    public void setRecords (TagListRecords[] Records)
    {
        this.Records = Records;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Records = "+Records+"]";
    }
}
