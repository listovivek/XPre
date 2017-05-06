package com.quad.xpress.models.privateBlock;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class PrivBlockReq {
    String user_email, blocked_email;

    public PrivBlockReq(String user_email, String blocked_email) {
        this.user_email = user_email;
        this.blocked_email = blocked_email;
    }
}