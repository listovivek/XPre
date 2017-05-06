package com.quad.xpress.models.spam;

/**
 * Created by kural on 27/4/17.
 */

public class spam_req {
    String user_email,spammed_email;

    public spam_req(String user_email, String spammed_email){
        this.user_email = user_email;
        this.spammed_email = spammed_email;
    }


}
