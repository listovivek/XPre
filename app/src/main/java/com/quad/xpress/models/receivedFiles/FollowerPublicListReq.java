package com.quad.xpress.models.receivedFiles;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class FollowerPublicListReq {
    String limit, index,user_id,emotion;
   /* "user_id":"10399",
            "index":"0",
            "limit":"30",
            "emotion":"like"*/

    public FollowerPublicListReq(String index, String limit, String user_id, String emotion) {
        this.index = index;
        this.limit = limit;
        this.user_id = user_id;
        this.emotion = emotion;
    }
}