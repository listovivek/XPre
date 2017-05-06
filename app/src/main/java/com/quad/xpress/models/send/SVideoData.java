package com.quad.xpress.models.send;

public class SVideoData
{
    private String title;

    private String from_email;

    private String file_id;

    private String path;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getFrom_email ()
    {
        return from_email;
    }

    public void setFrom_email (String from_email)
    {
        this.from_email = from_email;
    }

    public String getFile_id ()
    {
        return file_id;
    }

    public void setFile_id (String file_id)
    {
        this.file_id = file_id;
    }

    public String getPath ()
    {
        return path;
    }

    public void setPath (String path)
    {
        this.path = path;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", from_email = "+from_email+", file_id = "+file_id+", path = "+path+"]";
    }
}
