package com.quad.xpress.models.counter;

/**
 * Created by kural on 1/12/2017.
 */

public class CounterData {
    private String UserFollowers;

    private String PrivateCount;

    private String PreviousCount;

    private String TotalNumberofrecords;

    private String FollowingCount;

    private String PrivateFollowCount;

    private String ProfileImage;

    public String getUserFollowers ()
    {
        return UserFollowers;
    }

    public void setUserFollowers (String UserFollowers)
    {
        this.UserFollowers = UserFollowers;
    }

    public String getPrivateCount ()
    {
        return PrivateCount;
    }

    public void setPrivateCount (String PrivateCount)
    {
        this.PrivateCount = PrivateCount;
    }

    public String getPreviousCount ()
    {
        return PreviousCount;
    }

    public void setPreviousCount (String PreviousCount)
    {
        this.PreviousCount = PreviousCount;
    }

    public String getTotalNumberofrecords ()
    {
        return TotalNumberofrecords;
    }

    public void setTotalNumberofrecords (String TotalNumberofrecords)
    {
        this.TotalNumberofrecords = TotalNumberofrecords;
    }

    public String getFollowingCount ()
    {
        return FollowingCount;
    }

    public void setFollowingCount (String FollowingCount)
    {
        this.FollowingCount = FollowingCount;
    }

    public String getPrivateFollowCount ()
    {
        return PrivateFollowCount;
    }

    public void setPrivateFollowCount (String PrivateFollowCount)
    {
        this.PrivateFollowCount = PrivateFollowCount;
    }

    public String getProfileImage ()
    {
        return ProfileImage;
    }

    public void setProfileImage (String ProfileImage)
    {
        this.ProfileImage = ProfileImage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UserFollowers = "+UserFollowers+", PrivateCount = "+PrivateCount+", PreviousCount = "+PreviousCount+", TotalNumberofrecords = "+TotalNumberofrecords+", FollowingCount = "+FollowingCount+", PrivateFollowCount = "+PrivateFollowCount+", ProfileImage = "+ProfileImage+"]";
    }
}
