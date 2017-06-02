package com.quad.xpress.models.clickResponce;

/**
 * Created by kural on 8/2/2016.
 */
public class Like_Req {
    String user_email;
    String emotion;
    String status;
    String id;
    public Like_Req(String user_email, String emotion, String status,String id){
        this.user_email = user_email;
        this.emotion = emotion;
        this.status = status;
        this.id = id;

    }

}
