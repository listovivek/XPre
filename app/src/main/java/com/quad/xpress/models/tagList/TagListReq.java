package com.quad.xpress.models.tagList;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class TagListReq {
    String user_email, file_id;

    public TagListReq(String user_email, String file_id) {
        this.user_email = user_email;
        this.file_id = file_id;

    }
}