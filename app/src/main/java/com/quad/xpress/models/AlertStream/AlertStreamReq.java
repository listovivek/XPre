package com.quad.xpress.models.AlertStream;

/**
 * Created by kural on 2/10/2017.
 */

public class AlertStreamReq {
    String user_email,index,limit,notificationCount;
    public AlertStreamReq(String user_email, String index,String limit, String notificationCount){
        this.user_email = user_email;
        this.index = index;
        this.limit = limit;
        this.notificationCount = notificationCount;

    }
}
