package com.quad.xpress.models.privateAcceptReject;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class PrivARreq {
    String id, video_type,feedback;

    public PrivARreq(String id, String video_type,String feedback) {
        this.id = id;
        this.video_type = video_type;
        this.feedback = feedback;
    }
}