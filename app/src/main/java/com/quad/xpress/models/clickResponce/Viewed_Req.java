package com.quad.xpress.models.clickResponce;

/**
 * Created by kural on 8/2/2016.
 */
public class Viewed_Req {
    String viewed;
    String video_type;
    String id;
    //{"id":"57318bd6db923d57643edd59","viewed":"1","video_type":"video"}
    public Viewed_Req(String id, String viewed, String video_type){
        this.viewed = viewed;
        this.video_type = video_type;
        this.id = id;
    }

}
