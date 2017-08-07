package com.quad.xpress.utills;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kural on 8/4/17.
 */

public class RefreshToken {
    Activity activity;
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String token,txn;


   /* public RefreshToken(String token, Activity activity){



        sharedpreferences = activity.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

        RestClient.get(context).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {

                // LD.DismissTheDialog();
                if (arg0.getCode().equals("200")) {
                   // token = arg0.getData()[0].getToken();
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();

                    Toast.makeText(context, "TokenRefreshed", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            return;

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();

            }
        });


    }*/

}
