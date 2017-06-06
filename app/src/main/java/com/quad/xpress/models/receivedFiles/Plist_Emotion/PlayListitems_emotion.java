package com.quad.xpress.models.receivedFiles.Plist_Emotion;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PlayListitems_emotion {

    private String filename;
    private String title;
    private String created_date;
    String from_email,from_user;
    String TBPath,Mydp;
    String to_email;
    private String filemimeType;
    private String fileuploadPath;
    private String fileuploadFilename;
    private String fileID;
    private String tags;
    private String likesCount,isUserLiked;
    private String viewsCount;
    private String emotion;
    private String emotion_count;
    private String isUserFollowing;
    private String Privacy;

    public PlayListitems_emotion() {

    }

    public PlayListitems_emotion(String filename, String title, String created_date, String from_email, String TBPath, String filemimeType,
                                 String fileuploadPath, String fileuploadFilename, String fileID,String tags,
                                 String likesCount, String viewsCount, String isUserLiked,String emotion,String emotion_count,
                                    String isUserFollowing, String Privacy ,String to_email,String from_user,String Mydp) {
        this.filename = filename;
        this.title = title;
        this.to_email =to_email;
        this.created_date = created_date;
        this.from_email = from_email;
        this.TBPath = TBPath;
        this.filemimeType = filemimeType;
        this.fileuploadPath = fileuploadPath;
        this.fileuploadFilename = fileuploadFilename;
        this.fileID = fileID;
        this.tags = tags;
        this.from_user=from_user;
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.isUserLiked= isUserLiked;
        this.emotion = emotion;
        this.emotion_count =emotion_count;
        this.isUserFollowing= isUserFollowing;
        this.Privacy = Privacy;
        this.Mydp = Mydp;
    }

    public void setMydp(String mydp) {
        Mydp = mydp;
    }

    public String getMydp() {
        return Mydp;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public String getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(String privacy) {
        Privacy = privacy;
    }

    public String getIsUserFollowing() {
        return isUserFollowing;
    }

    public void setIsUserFollowing(String isUserFollowing) {
        this.isUserFollowing = isUserFollowing;
    }

    public String getEmotion_count() {
        return emotion_count;
    }

    public void setEmotion_count(String emotion_count) {
        this.emotion_count = emotion_count;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
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