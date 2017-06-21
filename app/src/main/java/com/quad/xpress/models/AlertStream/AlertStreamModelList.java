package com.quad.xpress.models.AlertStream;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AlertStreamModelList {

    private String filename;
    private String title;
    private String created_date;
    String from_email,sendername;
    String TBPath,Profile_pic;
    private String filemimeType;
    private String fileuploadPath;
    private String fileuploadFilename;
    private String fileID;
    private String tags;
    private String likesCount,isUserLiked;
    private String viewsCount;
    private String privacy;

    public AlertStreamModelList() {

    }

    public AlertStreamModelList(String filename, String title, String created_date, String from_email, String TBPath, String filemimeType,
                                String fileuploadPath, String fileuploadFilename, String fileID, String tags,
                                String likesCount, String viewsCount, String isUserLiked, String privacy, String sendername,String Profile_pic) {
        this.filename = filename;
        this.title = title;
        this.created_date = created_date;
        this.from_email = from_email;
        this.TBPath = TBPath;
        this.filemimeType = filemimeType;
        this.fileuploadPath = fileuploadPath;
        this.fileuploadFilename = fileuploadFilename;
        this.fileID = fileID;
        this.tags = tags;
        this.sendername =sendername;
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.isUserLiked= isUserLiked;
        this.privacy = privacy;
        this.Profile_pic=Profile_pic;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedDate() {
        String ChangedDate = created_date;
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
            Date netDate = (new Date(Long.parseLong(created_date)));
            ChangedDate = sdf.format(netDate);
        } catch (Exception ex) {
            Log.v("ChangedDate", "Exception " + ex);
        }

        return ChangedDate;
    }

    public void setCreatedDate(String created_date) {
        String ChangedDate = created_date;
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
            Date netDate = (new Date(Long.parseLong(created_date)));
            ChangedDate = sdf.format(netDate);
        } catch (Exception ex) {
            Log.v("ChangedDate", "Exception " + ex);
        }

        this.created_date = ChangedDate;
    }


    public String getFromEmail() {
        return from_email;
    }

    public void setFromEmail(String from_email) {
        this.from_email = from_email;
    }

    public String getTBPath() {
        return TBPath;
    }

    public void setTBPath(String TBPath) {
        this.TBPath = TBPath;
    }

    public String getFileMimeType() {
        return filemimeType;
    }

    public void setFileMimeType(String filemimeType) {
        this.filemimeType = filemimeType;
    }

    public String getFileuploadPath() {
        return fileuploadPath;
    }

    public void setFileuploadPath(String fileuploadPath) {
        this.fileuploadPath = fileuploadPath;
    }

    public String getFileuploadFilename() {
        return fileuploadFilename;
    }

    public void setFileuploadFilename(String fileuploadFilename) {
        this.fileuploadFilename = fileuploadFilename;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getIsUserLiked() {
        return isUserLiked;
    }

    public void setIsUserLiked(String isUserLiked) {
        this.isUserLiked = isUserLiked;
    }
}