package com.quad.xpress.Utills.helpers;

/**
 * Created by Venkatesh on 28-01-16.
 */

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingDialog {

    private Activity _activity;
    ProgressDialog ringProgressDialog;
    public LoadingDialog(Activity _activity){
        this._activity = _activity;
    }

    public void ShowTheDialog(String title,String message,Boolean Cancelable){

      //  ringProgressDialog = ProgressDialog.show(_activity, title, message, true);

        ringProgressDialog = new ProgressDialog(_activity, ProgressDialog.THEME_HOLO_DARK);
// set indeterminate style
        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
// set title and message
        ringProgressDialog.setTitle(title);
        ringProgressDialog.setMessage(message);
// and show it
        ringProgressDialog.show();
        ringProgressDialog.setCancelable(Cancelable);

    }
    public void DismissTheDialog(){



        if(ringProgressDialog.isShowing()){
            ringProgressDialog.dismiss();
        }
    }


   /* public LoadingDialog(Activity _activity, String title, String message, Boolean cancelable) {
        // this._activity = _activity;
        ringProgressDialog = new ProgressDialog(_activity);
        ringProgressDialog.setTitle(title);
        ringProgressDialog.setMessage(message);
        ringProgressDialog.setCancelable(cancelable);

    }

    public void ShowtheDialog() {
        ringProgressDialog.show();
    }

    public void DismissTheDialog() {
        if (ringProgressDialog.isShowing()) {
            ringProgressDialog.dismiss();
        }
    }*/

}