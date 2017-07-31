package com.quad.xpress.utills.pushnotification;

/**
 * Created by Venkatesh on 19-05-16.
 */

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tsengvn.typekit.Typekit;



public class myapplication extends Application {

    public static final String TAG = "ixpress";

    private static myapplication mInstance;

    private NotifyPrefManager pref;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
      //  ACRA.init(this);
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "moskmedium500.ttf"))
                .addBold(Typekit.createFromAsset(this, "mosksemibold600.ttf"));

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
       // LeakCanary.install(this);

    }

    public static synchronized myapplication getInstance() {
        return mInstance;
    }


    public NotifyPrefManager getPrefManager() {
        if (pref == null) {
            pref = new NotifyPrefManager(this);
        }

        return pref;
    }
}