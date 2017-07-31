package com.quad.xpress.models.authToken;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quad.xpress.utills.helpers.LoadingDialog;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Venkatesh on 26-05-16.
 */
public class RefreshToken {
    Activity _activity;
    String email_id;
    String device_id;
    LoadingDialog LD;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public RefreshToken(Activity _activity) {
        this._activity = _activity;
        LD = new LoadingDialog(_activity);
        sharedpreferences = _activity.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    public void Start() {
        LD.ShowTheDialog("Please Wait", "Loading", false);
        RestClient.get(_activity).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {
                LD.DismissTheDialog();
                if (arg0.getCode().equals("200")) {
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LD.DismissTheDialog();
            }
        });

    }

}
