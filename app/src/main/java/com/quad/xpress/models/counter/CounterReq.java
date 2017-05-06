package com.quad.xpress.models.counter;

/**
 * Created by kural on 1/12/2017.
 */

public class CounterReq {
    String user_email,PreviousCount;
    public  CounterReq( String user_email,String PreviousCount){
        this.user_email = user_email;
        this.PreviousCount = PreviousCount;
    }
}
