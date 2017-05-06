package com.quad.xpress.OOC;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.R;

/**
 * Created by kural on 28/3/17.
 */
public class ToastCustom  {

    private Activity _activity;
    Toast customtoast;
    Context context;
    public ToastCustom(Activity _activity){
        this._activity = _activity;
    }


    public void ShowToast(String title,SpannableString message, int gravity){


        context=_activity.getApplicationContext();
        LayoutInflater inflater=_activity.getLayoutInflater();
        View customToastroot =inflater.inflate(R.layout.toast_va, null);
        customtoast=new Toast(context);
        customtoast.setView(customToastroot);
        TextView textTitle = (TextView) customToastroot.findViewById(R.id.tv_toast_title);
        textTitle.setText(title);
        TextView text = (TextView) customToastroot.findViewById(R.id.tv_toast_msg);
        text.setText(message);

        switch (gravity){
            case 1:
                //top
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP,0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);

            break;
            case 2:
            // center
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);

                break;
            default:
                //bottom
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);

                break;
        }


        new CountDownTimer(4000, 1000)
        {

            public void onTick(long millisUntilFinished) {customtoast.show();}
            public void onFinish() {customtoast.show();}

        }.start();



    }




}
