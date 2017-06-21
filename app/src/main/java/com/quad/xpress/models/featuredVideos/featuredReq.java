package com.quad.xpress.models.featuredVideos;

/**
 * Created by kural on 1/5/2017.
 */

public class featuredReq {
    String video_type,user_email;
    public  featuredReq( String video_type,String user_email){
        this.video_type = video_type;
        this.user_email = user_email;
    }

}
