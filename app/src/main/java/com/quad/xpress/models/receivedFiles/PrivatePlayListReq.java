package com.quad.xpress.models.receivedFiles;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class PrivatePlayListReq {
    String user_email, limit, index;

    public PrivatePlayListReq(String user_email, String index, String limit) {
        this.user_email = user_email;
        this.limit = limit;
        this.index = index;
    }
}