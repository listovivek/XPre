package com.quad.xpress.models.Follow;

/**
 * Created by kural on 1/5/2017.
 */

public class FollowReq {
    String orignator,followers,user_id;
    public FollowReq(String orignator, String followers, String user_id){
        this.orignator = orignator;
        this.followers =followers;
        this.user_id=user_id;
    }

}
