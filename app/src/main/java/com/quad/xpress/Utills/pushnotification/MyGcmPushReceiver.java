package com.quad.xpress.Utills.pushnotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.quad.xpress.Act_notifications;
import com.quad.xpress.DashBoard;
import com.quad.xpress.PrivatePlayListActivity;

/**
 * Created by Venkatesh on 26-05-16.
 */
public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = "ixprez";

    private NotifyUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {


        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String image = bundle.getString("image");
        String timestamp = bundle.getString("created_at");
        Log.v(TAG, "bundle: " + bundle.toString() + " b " + bundle);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Title: " + title);
        Log.v(TAG, "message: " + message);
        Log.v(TAG, "image: " + image);
        Log.v(TAG, "timestamp: " + timestamp);




        Intent newMessageIntent = new Intent(getApplicationContext(), PrivatePlayListActivity.class);

        Intent acceptedRejectIntent = new Intent(getApplicationContext(), DashBoard.class);

        Intent followerIntent = new Intent(getApplicationContext(), Act_notifications.class);


        if(message.contains("Rise up to")|| message.contains ("Accepted")){

            showNotificationMessage(getApplicationContext(), "Xpression " +title, message, timestamp, acceptedRejectIntent);

        }else if (message.contains("latest Xpression")){

            showNotificationMessage(getApplicationContext(), "Xpression " +title, message, timestamp, followerIntent);
        }
        else{

            showNotificationMessage(getApplicationContext(), "Xpression " +title, message, timestamp, newMessageIntent);
        }


    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {

        notificationUtils = new NotifyUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);


    }


}