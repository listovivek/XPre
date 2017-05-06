package com.quad.xpress.models.tagList;

/**
 * Created by kural on 7/20/2016.
 */
public class TagListResp
{
    private String status;

    private TagListData data;

    private String code;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public TagListData getData ()
    {
        return data;
    }

    public void setData (TagListData data)
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
