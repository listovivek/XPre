package com.quad.xpress.Utills.localNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kural on 11/5/17.
 */

public class NotifcationButtonActionRecer extends BroadcastReceiver{

    Context contextp;
    
    @Override
    public void onReceive(Context context, Intent intent) {

        contextp = context;

        String action = intent.getAction();
       /* if(StatiConstants.urimap != null ) {
            Toast.makeText(context, "CALLED", Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }*/

/*
        public class NotificationReceiver extends BroadcastReceiver {

            private static final String YES_ACTION = "com.example.packagename.YES";
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                if(YES_ACTION.equals(action)) {
                    Toast.makeText(context, "CALLED", Toast.LENGTH_SHORT).show();
                }
            }*/


    }





}
