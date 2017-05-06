package com.quad.xpress.models.receivedFiles.register;

/**
 * Created by kural on 9/29/2016.
 */

public class LanguageData {
    private String id;

    private String iso;

    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIso ()
    {
        return iso;
    }

    public void setIso (String iso)
    {
        this.iso = iso;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", iso = "+iso+", name = "+name+"]";
    }
}
