package com.quad.xpress.models.GetProfilePIc;

/**
 * Created by kural on 1/24/2017.
 */

public class PicData {
    private String profile_image;

    public String getProfile_image ()
    {
        return profile_image;
    }

    public void setProfile_image (String profile_image)
    {
        this.profile_image = profile_image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [profile_image = "+profile_image+"]";
    }
}
