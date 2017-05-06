package com.quad.xpress.models.Follow;

/**
 * Created by kural on 1/5/2017.
 */

public class FollowReq {
    String orignator,followers;
    public FollowReq(String orignator, String followers){
        this.orignator = orignator;
        this.followers =followers;
    }

}
