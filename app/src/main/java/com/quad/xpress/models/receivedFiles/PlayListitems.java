package com.quad.xpress.models.receivedFiles;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Venkatesh on 21-05-16.
 */
public class PlayListitems {

    private String filename;
    private String title;
    private String created_date;
    public String from_email;
    String TBPath, username,privacy;
    private String filemimeType;
    private String fileuploadPath;
    private String fileuploadFilename;
    private String fileID;
    private String tags;
    private String likesCount,isUserLiked;
    private String viewsCount,phonenumber,user_id;


    public PlayListitems() {

    }

    public PlayListitems(String filename, String title, String created_date, String from_email, String TBPath, String filemimeType,
                         String fileuploadPath, String fileuploadFilename, String fileID, String tags,String likesCount,String viewsCount
    ,String isUserLiked, String username,String privacy,String phonenumber,String user_id) {
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
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.isUserLiked= isUserLiked;
        this.username = username;
        this.privacy = privacy;
        this.phonenumber=phonenumber;
        this.user_id=user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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