package com.quad.xpress.models.vp_guide;

/**
 * Created by kural on 12/2/2016.
 */

public class guide_data {
    private String pro_name;

    private String id;

    private String pro_image;

    private String pro_descrption;

    private String type;

    public String getPro_name ()
    {
        return pro_name;
    }

    public void setPro_name (String pro_name)
    {
        this.pro_name = pro_name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPro_image ()
    {
        return pro_image;
    }

    public void setPro_image (String pro_image)
    {
        this.pro_image = pro_image;
    }

    public String getPro_descrption ()
    {
        return pro_descrption;
    }

    public void setPro_descrption (String pro_descrption)
    {
        this.pro_descrption = pro_descrption;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pro_name = "+pro_name+", id = "+id+", pro_image = "+pro_image+", pro_descrption = "+pro_descrption+", type = "+type+"]";
    }
}
