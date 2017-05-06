package com.quad.xpress.models.NotificationStream;

/**
 * Created by kural on 2/10/2017.
 */

public class NotificationStreamReq {
    String followers_email,Index;
    public NotificationStreamReq(String followers_email,String Index){
        this.followers_email = followers_email;
        this.Index = Index;

    }
}
