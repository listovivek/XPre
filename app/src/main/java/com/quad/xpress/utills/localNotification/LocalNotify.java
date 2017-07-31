package com.quad.xpress.utills.localNotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.quad.xpress.R;
import com.quad.xpress.utills.StatiConstants;
import com.quad.xpress.utills.helpers.NetConnectionDetector;

import retrofit.mime.TypedFile;

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
    int ID;


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
                    .setContentTitle("Xpressed")
                    .setSmallIcon(R.drawable.ic_done)
                    // Removes the progress bar
                    .setProgress(0, 0, false)
                    .setAutoCancel(true);
            notificationManager.notify(ID, mBuilder.build());
        }
    }
    public void UploadNotificationFailedRetry(int ID, String Title, Activity _activity, TypedFile uri, String toUser, String toType, TypedFile typedFile) {


     /*   StatiConstants.mapVideoTyped.put(1,uri);
        StatiConstants.mapTitile.put(1,Title);
        StatiConstants.mapTo.put(1,toUser);
        StatiConstants.mapType.put(1,toType);
        StatiConstants.mapThumbnailTyped.put(1,typedFile);*/

          mContext = _activity.getApplicationContext();


        Intent RetryiNTENT = new Intent();
        RetryiNTENT.setAction(StatiConstants.Retry);
        Intent IgnoreIntent = new Intent();
        IgnoreIntent.setAction(StatiConstants.Ignore);

        PendingIntent PRetery = PendingIntent.getBroadcast(mContext, 01, RetryiNTENT, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent PIgnore = PendingIntent.getBroadcast(mContext, 12345, IgnoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);





        notification = mBuilder.setTicker(Title).setWhen(0)
                    .setAutoCancel(false)
                    .setContentTitle("Failed to deliver !")
                    .setContentText(Title)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_failed)
                    .setProgress(0, 0, false)
                    .addAction(R.drawable.ic_failed, "Retry",PRetery)
                    //.addAction(R.drawable.ic_user_icon, "Ignore",PIgnore)
                    .build();


            notificationManager.notify(ID, notification);



        }

    public void UploadNotificationFailed(int ID, String Title, Activity _activity, Uri uri) {



        mContext = _activity.getApplicationContext();



        notification = mBuilder.setTicker(Title).setWhen(0)
                .setAutoCancel(false)
                .setContentTitle("Failed to deliver !")
                .setContentText(Title)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_failed)
                .setProgress(0, 0, false)

                .build();


        notificationManager.notify(ID, notification);



    }




}
