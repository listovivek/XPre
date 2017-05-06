package com.quad.xpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.models.otpverification.OTPMreq;
import com.quad.xpress.models.otpverification.OTPMresp;
import com.quad.xpress.models.otpverification.ResendOtpMreq;
import com.quad.xpress.models.otpverification.ResendOtpMresp;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by  on 26-04-16.
 */
public class OTPverification extends AppCompatActivity {
    TextView tvResendCode;
    TextView tv_otp_confirmation;
    Button btn_change_email;
    Button btn_verify_done;
    EditText et_otp;
    private static final String REQUIRED_MSG = "OTP should be 6 characters";
    private static final String WRONG_OTP = "Wrong OTP";
    //String GetOTP;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verify);

        //for transparent  status bar
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //    Intent getOtpIntent = getIntent();
        // GetOTP = getOtpIntent.getStringExtra("Reg_OTP");
        btn_change_email = (Button) findViewById(R.id.btn_change_email);
        btn_verify_done = (Button) findViewById(R.id.btn_verify_done);
        et_otp = (EditText) findViewById(R.id.et_otp);
        tvResendCode = (TextView) findViewById(R.id.tv_resend_code);
        Intent intentemail = getIntent();
        String atv_uemail = intentemail.getExtras().getString("email");
                /*Toast.makeText(getApplicationContext(),"Otp Verification",Toast.LENGTH_LONG).show();*/

        StringBuilder sb = new StringBuilder();
        sb.append("Confirmation code has been sent to ");
        sb.append(atv_uemail);
        sb.append(" Please Click resend if the mail failed to deliver.");
     //   Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();

        tv_otp_confirmation = (TextView) findViewById(R.id.text_otp);
        tv_otp_confirmation.setText(sb);



        tvResendCode.setText(Html.fromHtml(getResources().getString(R.string.resend_code)));
        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(OTPverification.this, "Resend Clicked", Toast.LENGTH_LONG).show();
                ResendOtpWebService();





            }
        });

        btn_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(OTPverification.this, "btn_change_email", Toast.LENGTH_LONG).show();
                editor.putBoolean(SharedPrefUtils.SpRegisterSuccess, false);
                editor.commit();
                onBackPressed();


            }
        });
        btn_verify_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TypedOtp = et_otp.getText().toString();
                if (TypedOtp.length() == 6) {
                    OtpVerifyWebService(TypedOtp);
                    //  et_otp.setError(WRONG_OTP);
                    return;
                }
                et_otp.setError(REQUIRED_MSG);

            }
        });

        et_otp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_GO || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    btn_verify_done.performClick();
                }
                return false;
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void OtpVerifyWebService(String typedOTP) {

        RestClient.get(OTPverification.this).OTPVerification(new OTPMreq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),
                typedOTP, sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<OTPMresp>() {
            @Override
            public void success(OTPMresp OtpMresp, Response response) {

                if (OtpMresp.getStatus().equals("OK")) {

                    editor.putString(SharedPrefUtils.SpToken, OtpMresp.getData()[0].getToken());
                    editor.putBoolean(SharedPrefUtils.SpOtpVerify, true);
                    editor.commit();

                    Intent VerifiedIntent = new Intent(OTPverification.this, IntroActivity.class);
                    startActivity(VerifiedIntent);
                    finish();
                } else {
                    et_otp.setError(WRONG_OTP);
                    Toast.makeText(OTPverification.this, "Wrong OTP" + OtpMresp.getStatus(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void failure(RetrofitError error) {

                //   Log.v("failure","OtpVerifyWebService  "+error);
                //   Toast.makeText(OTPverification.this, "Failure " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ResendOtpWebService() {


        RestClient.get(OTPverification.this).ResendOTP(new ResendOtpMreq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),
                sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<ResendOtpMresp>() {
            @Override
            public void success(ResendOtpMresp resendOtpMresp, Response response) {

                if (resendOtpMresp.getStatus().equals("OK")) {
                    Toast.makeText(OTPverification.this, "OTP Successfully Resend. Please Check your mail", Toast.LENGTH_LONG).show();

                } else {
                    //   Log.v("", "Try again later " + arg0.getStatus());
                    Toast.makeText(OTPverification.this, "Resend Failed, Try Again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(OTPverification.this, "Failure " + error, Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}