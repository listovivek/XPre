package com.quad.xpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.models.otpverification.OTPMreq;
import com.quad.xpress.models.otpverification.OTPMresp;
import com.quad.xpress.models.otpverification.ResendOtpMreq;
import com.quad.xpress.models.otpverification.ResendOtpMresp;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.concurrent.TimeUnit;

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
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false,toomany = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String TAG = "FireBasePhoneVerification";
     String atv_phnumber;
     String TypedOtp;
    TextWatcher mTextEditorWatcher;
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

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
                String a1;
                a1 ="Phone number \n";
                ToastCustom toastCustom = new ToastCustom(OTPverification.this);
                SpannableString ss = new SpannableString(a1 +atv_phnumber+", verified.");

                ss.setSpan(new ForegroundColorSpan(Color.GREEN), a1.length(), a1.length()+atv_phnumber.length(), 0);
                ss.setSpan(new RelativeSizeSpan(1.5f),a1.length(), a1.length()+atv_phnumber.length(), 0);
                toastCustom.ShowToast("iXprez ",ss,1);


                editor.putBoolean(SharedPrefUtils.SpOtpVerify, true);
                editor.commit();

                Intent VerifiedIntent = new Intent(OTPverification.this, IntroActivity.class);
                startActivity(VerifiedIntent);
                finish();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
               // Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    // mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    toomany = true;
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //  updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //   updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };



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
        atv_phnumber = intentemail.getExtras().getString("phnumber");
      //  mVerificationId = intentemail.getExtras().getString("verificationId");

        //        Toast.makeText(getApplicationContext(),"Otp Verification"+mVerificationId,Toast.LENGTH_LONG).show();
          mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Toast.makeText(OTPverification.this, "s--"+count, Toast.LENGTH_SHORT).show();
                if (count == 6){
                    btn_verify_done.performClick();

                }

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
              //  et_otp.setText(String.valueOf(s.length()));
               // Toast.makeText(OTPverification.this, "tc"+count, Toast.LENGTH_SHORT).show();

                if (count == 6){
                    btn_verify_done.performClick();

                }

            }

            public void afterTextChanged(Editable s) {

                if (s.length() == 6){
                    btn_verify_done.performClick();

                }
               // Toast.makeText(OTPverification.this, "s--"+s, Toast.LENGTH_SHORT).show();
            }
        };
        et_otp.addTextChangedListener(mTextEditorWatcher);
/*
        String a1 = "Confirmation code has been sent to ",
                a2 =" Please Click resend if the code failed to deliver."
                        ;
        SpannableString ss = new SpannableString(a1 +atv_phnumber+" \n &  ");

        SpannableString ss2 = new SpannableString( atv_uemail+" ." +a2);

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), a1.length(), a1.length()+atv_phnumber.length()+1, 0);
        ss.setSpan(new RelativeSizeSpan(1.0f),a1.length(), a1.length()+atv_phnumber.length()+1,0);
        ss2.setSpan(new ForegroundColorSpan(Color.GREEN),0,atv_uemail.length(), 0);
        ss2.setSpan(new RelativeSizeSpan(1.0f),0, atv_uemail.length(),0);*/


        StringBuilder sb = new StringBuilder();
        sb.append("Confirmation code has been sent to ");
        sb.append(atv_uemail);
        sb.append(" & "+atv_phnumber);
        sb.append(" Please Click resend if the code failed to deliver.");
     //   Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();

        tv_otp_confirmation = (TextView) findViewById(R.id.text_otp);
        tv_otp_confirmation.setText(sb.toString());

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
               atv_phnumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

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

                TypedOtp = et_otp.getText().toString();
                if (TypedOtp.length() == 6) {

                   // OtpVerifyWebService(TypedOtp);
                   // Toast.makeText(OTPverification.this, ""+mVerificationId+ " --ff--  "+et_otp.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (!toomany){
                    verifyPhoneNumberWithCode(mVerificationId,TypedOtp);}
                    else{
                        try {
                            OtpVerifyWebService(TypedOtp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    return;
                }else {
                et_otp.setError(REQUIRED_MSG);}

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

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void OtpVerifyWebService(String typedOTP) {

        RestClient.get(OTPverification.this).OTPVerification(new OTPMreq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),
                typedOTP, sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<OTPMresp>() {
            @Override
            public void success(OTPMresp OtpMresp, Response response) {

                if (OtpMresp.getStatus().equals("OK")) {

                  //  editor.putString(SharedPrefUtils.SpToken, OtpMresp.getData()[0].getToken());
                    editor.putBoolean(SharedPrefUtils.SpOtpVerify, true);
                    editor.commit();

                    Intent VerifiedIntent = new Intent(OTPverification.this, IntroActivity.class);
                    startActivity(VerifiedIntent);
                    finish();
                } else {
                    et_otp.setError(WRONG_OTP);
                    //Toast.makeText(OTPverification.this, "Wrong OTP" + OtpMresp.getStatus(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void failure(RetrofitError error) {

                //   Log.v("failure","OtpVerifyWebService  "+error);
                //   Toast.makeText(OTPverification.this, "Failure " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void verifyPhoneNumberWithCode(String verificationIds, String codes) {
        // [START verify_with_code]
        PhoneAuthCredential credential = null;
        // [END verify_with_code]
        try {
             credential = PhoneAuthProvider.getCredential(verificationIds, codes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                          //  editor.putString(SharedPrefUtils.SpToken, "sms");

                            editor.putBoolean(SharedPrefUtils.SpOtpVerify, true);
                            editor.commit();

                            Intent VerifiedIntent = new Intent(OTPverification.this, IntroActivity.class);
                            startActivity(VerifiedIntent);
                            finish();

                            // [START_EXCLUDE]
                          //  updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {

                            OtpVerifyWebService(TypedOtp);
                            // Sign in failed, display a message and update the UI


                           /* try {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        et_otp.setError(WRONG_OTP);
                                    }
                                }, 5200);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
*/

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                            }


                        }
                    }
                });
    }


    private void ResendOtpWebService() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                atv_phnumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);


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