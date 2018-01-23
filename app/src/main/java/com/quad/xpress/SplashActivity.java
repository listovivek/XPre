package com.quad.xpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.quad.xpress.utills.helpers.PermissionStrings;
import com.quad.xpress.utills.helpers.SharedPrefUtils;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;  //2 Seconds
    SharedPreferences sharedpreferences;
    Intent i;
    Context _context;
    String Permission4;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity _activity;
    // Toast DeclineToast;
    // Boolean SpOtpVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = SplashActivity.this;
        _activity = SplashActivity.this;
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean RegisterSuccess = sharedpreferences.getBoolean(SharedPrefUtils.SpRegisterSuccess, false);
        Boolean OtpVerifySuccess = sharedpreferences.getBoolean(SharedPrefUtils.SpOtpVerify, false);
        // OtpVerifiedSuccess = true;  //for testing . remove it
        Permission4 = PermissionStrings.WRITE_EXTERNAL_STORAGE;
        if (OtpVerifySuccess) {
            i = new Intent(SplashActivity.this, DashBoard.class);
        } else {

            i = new Intent(SplashActivity.this, RegistrationActivity.class);
        }




        //CheckAndRequestPermission();
        Intent2Activity();




    }


    /*@Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkPermission(PermissionStrings.GET_ACCOUNTS)) {

                return;
            }
        }
    }*/




    public void CheckAndRequestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            if (checkPermission(Permission4)) {
                Intent2Activity();
                return;
            }
            requestPermission(Permission4);
        } else {
            Intent2Activity();
        }
    }

    private void Intent2Activity() {



        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean checkPermission(String permission) {

        int result = ContextCompat.checkSelfPermission(_context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else {

            return false;
        }
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.GET_ACCOUNTS}, 93);
                    Intent2Activity();
                } else {
                    Toast.makeText(_context, "Kindly, give storage permission to store and access the video and audio", Toast.LENGTH_SHORT).show();
                    //CheckAndRequestPermission();
                }
                break;
        }
    }

  /*  public void ShowDeclineToast() {
        if (DeclineToast == null) {
            DeclineToast.show();
        }
    }*/

}