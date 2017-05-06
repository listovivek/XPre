package com.quad.xpress.models.TrendingSearch;

/**
 * Created by kural on 3/3/2017.
 */

public class TsRecords {
    private String tags;

    private String created_date;

    private String fileuploadFilename;

    private String thumbnailPath;

    private String likeCount;

    private String isUserLiked;

    private String fileuploadPath;

    private String privacy;

    private String viewed;

    private String isuerfollowing;

    private TsEmotions[] emotion;

    private String country;

    private String feedback;

    private String title;

    private String is_deleted;

    private String thumbnailmimeType;

    private String _id;

    private String filemimeType;

    private String from_email;

    private String language;

    private String view_count;

    private String to_email;

    private String thumbnailFilename;

    public String getTags ()
    {
        return tags;
    }

    public void setTags (String tags)
    {
        this.tags = tags;
    }

    public String getCreated_date ()
    {
        return created_date;
    }

    public void setCreated_date (String created_date)
    {
        this.created_date = created_date;
    }

    public String getFileuploadFilename ()
    {
        return fileuploadFilename;
    }

    public void setFileuploadFilename (String fileuploadFilename)
    {
        this.fileuploadFilename = fileuploadFilename;
    }

    public String getThumbnailPath ()
    {
        return thumbnailPath;
    }

    public void setThumbnailPath (String thumbnailPath)
    {
        this.thumbnailPath = thumbnailPath;
    }

    public String getLikeCount ()
    {
        return likeCount;
    }

    public void setLikeCount (String likeCount)
    {
        this.likeCount = likeCount;
    }

    public String getIsUserLiked ()
    {
        return isUserLiked;
    }

    public void setIsUserLiked (String isUserLiked)
    {
        this.isUserLiked = isUserLiked;
    }

    public String getFileuploadPath ()
    {
        return fileuploadPath;
    }

    public void setFileuploadPath (String fileuploadPath)
    {
        this.fileuploadPath = fileuploadPath;
    }

    public String getPrivacy ()
    {
        return privacy;
    }

    public void setPrivacy (String privacy)
    {
        this.privacy = privacy;
    }

    public String getViewed ()
    {
        return viewed;
    }

    public void setViewed (String viewed)
    {
        this.viewed = viewed;
    }

    public String getIsuerfollowing ()
    {
        return isuerfollowing;
    }

    public void setIsuerfollowing (String isuerfollowing)
    {
        this.isuerfollowing = isuerfollowing;
    }

    public TsEmotions[] getEmotion ()
    {
        return emotion;
    }

    public void setEmotion (TsEmotions[] emotion)
    {
        this.emotion = emotion;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getFeedback ()
    {
        return feedback;
    }

    public void setFeedback (String feedback)
    {
        this.feedback = feedback;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getIs_deleted ()
    {
        return is_deleted;
    }

    public void setIs_deleted (String is_deleted)
    {
        this.is_deleted = is_deleted;
    }

    public String getThumbnailmimeType ()
    {
        return thumbnailmimeType;
    }

    public void setThumbnailmimeType (String thumbnailmimeType)
    {
        this.thumbnailmimeType = thumbnailmimeType;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getFilemimeType ()
    {
        return filemimeType;
    }

    public void setFilemimeType (String filemimeType)
    {
        this.filemimeType = filemimeType;
    }

    public String getFrom_email ()
    {
        return from_email;
    }

    public void setFrom_email (String from_email)
    {
        this.from_email = from_email;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    public String getView_count ()
    {
        return view_count;
    }

    public void setView_count (String view_count)
    {
        this.view_count = view_count;
    }

    public String getTo_email ()
    {
        return to_email;
    }

    public void setTo_email (String to_email)
    {
        this.to_email = to_email;
    }

    public String getThumbnailFilename ()
    {
        return thumbnailFilename;
    }

    public void setThumbnailFilename (String thumbnailFilename)
    {
        this.thumbnailFilename = thumbnailFilename;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tags = "+tags+", created_date = "+created_date+", fileuploadFilename = "+fileuploadFilename+", thumbnailPath = "+thumbnailPath+", likeCount = "+likeCount+", isUserLiked = "+isUserLiked+", fileuploadPath = "+fileuploadPath+", privacy = "+privacy+", viewed = "+viewed+", isuerfollowing = "+isuerfollowing+", emotion = "+emotion+", country = "+country+", feedback = "+feedback+", title = "+title+", is_deleted = "+is_deleted+", thumbnailmimeType = "+thumbnailmimeType+", _id = "+_id+", filemimeType = "+filemimeType+", from_email = "+from_email+", language = "+language+", view_count = "+view_count+", to_email = "+to_email+", thumbnailFilename = "+thumbnailFilename+"]";
    }
}
