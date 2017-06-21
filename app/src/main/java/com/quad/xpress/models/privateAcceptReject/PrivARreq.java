package com.quad.xpress.models.privateAcceptReject;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class PrivARreq {
    String id, video_type,feedback,response_email;

    public PrivARreq(String id, String video_type,String feedback,String response_email) {
        this.id = id;
        this.video_type = video_type;
        this.feedback = feedback;
        this.response_email =response_email;
    }
}