package com.quad.xpress.models.receivedFiles;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class PublicPlayListReq {
    String limit, index,user_email,emotion;

    public PublicPlayListReq(String index, String limit,String user_email,String emotion) {
        this.index = index;
        this.limit = limit;
        this.user_email = user_email;
        this.emotion = emotion;
    }
}