package com.quad.xpress.models.spam;

/**
 * Created by kural on 27/4/17.
 */

public class spam_req {
    String user_email,spammed_email,user_id;

    public spam_req(String user_email, String spammed_email, String user_id){
        this.user_email = user_email;
        this.spammed_email = spammed_email;
        this.user_id= user_id;
    }


}
