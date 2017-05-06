package com.quad.xpress.Utills.pushnotification;

/**
 * Created by Venkatesh on 19-05-16.
 */

import android.app.Application;

import com.quad.xpress.R;
import com.tsengvn.typekit.Typekit;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formUri = "", // will not be used
        mailTo = "kural.ms@quadrupleindia.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private NotifyPrefManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
      //  ACRA.init(this);
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "moskmedium500.ttf"))
                .addBold(Typekit.createFromAsset(this, "mosksemibold600.ttf"));
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public NotifyPrefManager getPrefManager() {
        if (pref == null) {
            pref = new NotifyPrefManager(this);
        }

        return pref;
    }
}