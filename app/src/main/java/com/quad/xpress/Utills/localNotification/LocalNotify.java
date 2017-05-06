package com.quad.xpress.Utills.localNotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.quad.xpress.MyUploads;
import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;

/**
 * Created by Venkatesh on 08-06-16.
 */
public class LocalNotify {
    Context mContext;
    NotificationManager notificationManager;
    Notification notification;
    NotificationCompat.Builder mBuilder;
    Activity _activity;
    NetConnectionDetector NDC;



    public LocalNotify(Context mContext, NotificationCompat.Builder mBuilder, Activity _activity) {
        this.mContext = mContext;
        this.mBuilder = mBuilder;
        this._activity = _activity;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void ShowFileUploadNotification(String UploadTitle, int ID) {


          notification = mBuilder.setTicker(UploadTitle).setWhen(0)
                .setAutoCancel(false)
                .setContentTitle("Xpressing")
                .setContentText(UploadTitle)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_file_upload)
                .setProgress(100, 0, true)
                .build();
        notificationManager.notify(ID, notification);



    }

    public void FinishedUploadNotification(int ID, String Title) {

        mContext = _activity.getApplicationContext();
        if (notificationManager != null) {
            mBuilder.setContentText(Title)
                    .setSmallIcon(R.drawable.ic_done);
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            notificationManager.notify(ID, mBuilder.build());
        }
    }
    public void UploadNotificationFailed(int ID, String Title, Activity _activity) {

        mContext = _activity.getApplicationContext();

            Intent intent = new Intent(mContext, MyUploads.class);
            PendingIntent pIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, 0);

            notification = mBuilder.setTicker(Title).setWhen(0)
                    .setAutoCancel(false)
                    .setContentTitle("Failed to deliver, Retry ?")
                    .setContentText(Title)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_block)
                    .setProgress(100, 0, true)
                    .addAction(R.drawable.intro_heart, "Retry",pIntent)
                    .build();
            notificationManager.notify(ID, notification);

                if(notificationManager == null){


                }



        }



}
