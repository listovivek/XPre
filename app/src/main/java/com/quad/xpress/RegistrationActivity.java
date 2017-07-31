package com.quad.xpress;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.quad.xpress.models.registration.RegRequest;
import com.quad.xpress.models.registration.RegResp;
import com.quad.xpress.utills.StatiConstants;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.LoadingDialog;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.PermissionStrings;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.pushnotification.NotifyConfig;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Venkatesh on 12-04-16.
 */
public class RegistrationActivity extends AppCompatActivity {

    EditText et_uname;
    AutoCompleteTextView atv_uemail,atv_country,atv_languadge;
    Spinner spinner_ucountry;
    Spinner spinner_ulang;
    EditText et_umob_no;
    Button Bsave;
    ArrayAdapter mailAdapter;
    String Device_id;
    String Permission4;
    Boolean NoPermisson = true;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Boolean ChangeEmail;
    NetConnectionDetector NDC;
    String CountryPhoneText;
    TextView tv_term_of_service;
    String x;
    private static final int CONTACT_PERMISSION_REQUEST_CODE = 93;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Dialog OutOFStore;


  /*  Spanned sp_text;*/

    String TAG = "RegistrationActivity",PhoneNumberWithCode ="unavailable";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String GCMToken = null;
    String CountryFullName;
    String[] languages;
    ArrayList<String> country_list = new ArrayList<String>();
    ArrayList<String> Sorted_Clist = new ArrayList<String>();
    ArrayList<String> Sorted_Pnlist = new ArrayList<String>();
    ArrayList<String> country_list_short = new ArrayList<String>();
    ArrayList<String> phone_code = new ArrayList<String>();
    ArrayList<String> languadgeList = new ArrayList<>();
    ArrayList<String> localcountries=new ArrayList<String>();

    Activity _activity;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registration);


        if (checkPlayServices()) {
           // Log.v(TAG, "checkPlayServices");
            registerGCM();

        }
        Permission4 = PermissionStrings.WRITE_EXTERNAL_STORAGE;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final View activityRootView = findViewById(R.id.rootview);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(RegistrationActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                }
            }
        });



        atv_uemail = (AutoCompleteTextView) findViewById(R.id.uemail);
        atv_uemail.setDropDownBackgroundResource(R.color.black);

        //for transparent  status bar
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Intent i = getIntent();
        context = getApplicationContext();
        _activity = RegistrationActivity.this;
        ChangeEmail = i.getBooleanExtra("ChangeEmail", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CheckAndRequestPermission();
        }else {
            getUserEmails();
        }


        NDC = new NetConnectionDetector();
        if (NDC.isConnected(context)) {


        }else{
            Toast.makeText(getApplicationContext(),"Check internet Connectivity",Toast.LENGTH_LONG).show();
        }

        if (checkPlayServices()) {
            Log.v(TAG, "checkPlayServices");
            registerGCM();
        }
        if(isStoreVersion(context)){
            //  Toast.makeText(SplashActivity.this, "PStore", Toast.LENGTH_SHORT).show();
        }else {
           // Toast.makeText(RegistrationActivity.this, "This Package is downloaded out side of Playstore Kindly download App from Playstore to get Secure Application.", Toast.LENGTH_SHORT).show();
           // Out_of_Store_alert();
        }


        atv_country = (AutoCompleteTextView) findViewById(R.id.autotv_country);
        atv_country.setDropDownBackgroundResource(R.color.black);
        atv_languadge = (AutoCompleteTextView) findViewById(R.id.autotv_lang);
        atv_languadge.setDropDownBackgroundResource(R.color.black);



        et_uname = (EditText) findViewById(R.id.uname);
        et_uname.requestFocus();


        spinner_ucountry = (Spinner) findViewById(R.id.ucountry);
        spinner_ulang = (Spinner) findViewById(R.id.ulang);
        et_umob_no = (EditText) findViewById(R.id.umob_no);
        Bsave = (Button) findViewById(R.id.save);
        tv_term_of_service = (TextView) findViewById(R.id.text_terms_of_service);

        atv_uemail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_NEXT && ChangeEmail ) {
                   Bsave.performClick();
                }else if (keyCode == EditorInfo.IME_ACTION_NEXT  ) {
                    atv_country.requestFocus();
                }
                return false;
            }
        });

        atv_uemail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                atv_country.requestFocus();
            }
        });

        atv_country.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

            //    Toast.makeText(context, ""+keyCode, Toast.LENGTH_SHORT).show();

                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    atv_languadge.requestFocus();
                }
                return false;
            }
        });
        atv_languadge.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
             //   Toast.makeText(context, ""+keyCode, Toast.LENGTH_SHORT).show();

                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    et_umob_no.requestFocus();
                }
                return false;
            }
        });
        atv_languadge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_umob_no.requestFocus();
            }
        });

        et_umob_no.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_GO || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                   Bsave.performClick();
                }
                return false;
            }
        });


        SpannableStringBuilder spanTxt = new SpannableStringBuilder("By joining xPress, you agree to our \n");
        spanTxt.append("Term of services");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), "Terms of services Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - "Term of services".length(), spanTxt.length(), 0);
        spanTxt.append(" and ");
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 36, spanTxt.length(), 0);
        spanTxt.append("Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), "Privacy Policy Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - "Privacy Policy".length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 36, spanTxt.length(), 0);
        tv_term_of_service.setMovementMethod(LinkMovementMethod.getInstance());
        tv_term_of_service.setText(spanTxt);




        tv_term_of_service.setMovementMethod(LinkMovementMethod.getInstance());







        Device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        Set<String> set = PhoneNumberUtil.getInstance().getSupportedRegions();
        String[] arr = set.toArray(new String[set.size()]);

        for (int iv = 0; iv < set.size(); iv++) {
            Locale locale = new Locale("en", arr[iv]);
            country_list.add(locale.getDisplayCountry());
            country_list_short.add(arr[iv]);
        }



        for (int ic = 0; ic < country_list.size(); ic++) {

            phone_code.add(Integer.toString(PhoneNumberUtil.getInstance().getCountryCodeForRegion(country_list_short.get(ic))));

        }
        Map<String, String> map1 = new HashMap<String, String>();
        for (int is =0 ; is < country_list.size(); is++){
            map1.put(country_list.get(is),phone_code.get(is));
        }
        Collections.sort(country_list);

        for (String s : country_list){
            System.out.println(s + "..........." + map1.get(s));
            Sorted_Clist.add(s);
            Sorted_Pnlist.add(map1.get(s));
        }




        final ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(this,R.layout.spinner_custom, Sorted_Clist);
        spinner_ucountry.setAdapter(adapter_country);


        String compareValue = Locale.getDefault().getDisplayCountry();

        spinner_ucountry.setAdapter(adapter_country);
        int spinnerPosition_country = 0;
        if (!compareValue.equals(null)) {
            spinnerPosition_country = adapter_country.getPosition(compareValue);
            spinner_ucountry.setSelection(spinnerPosition_country);
        }

        //auto complete Tv
        String[] Country_str = Sorted_Clist.toArray(new String[0]);
        atv_country.setText(Country_str[spinnerPosition_country]);
        ArrayAdapter atv_country_adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, Country_str);
        atv_country.setThreshold(1);
        atv_country.setAdapter(atv_country_adapter);

      atv_country.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {

              atv_country.setText("");

              return false;
          }
      });
    atv_languadge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                atv_languadge.setText("");

                return false;
            }
        });


        atv_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                et_umob_no.setText("+"+Sorted_Pnlist.get(Sorted_Clist.indexOf(atv_country.getText().toString()))+"");
                atv_languadge.requestFocus();

            }
        });



        Locale[] locales = Locale.getAvailableLocales();

        for(Locale l:locales)
        {
            localcountries.add(l.getDisplayName().toString());
        }
        languages=(String[]) localcountries.toArray(new String[localcountries.size()]);
        languages[0] = Locale.getDefault().getDisplayName();
/*
        ArrayAdapter<String> adapter_lanugage = new ArrayAdapter<String>(this,R.layout.spinner_custom, languages);
        spinner_ulang.setAdapter(adapter_lanugage);*/
        Collections.addAll(languadgeList, languages);
        ArrayAdapter atv_lang_adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, languages);
        atv_languadge.setAdapter(atv_lang_adapter);
        atv_languadge.setThreshold(1);
        atv_languadge.setText(languages[0]);

        spinner_ucountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_umob_no.setText("+"+Sorted_Pnlist.get(position)+"");
                et_umob_no.setFocusableInTouchMode(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(NotifyConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                    Log.v("token", " GCM " + token);
                    //  Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();
                    GCMToken = token;
                } else if (intent.getAction().equals(NotifyConfig.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    String token = intent.getStringExtra("token");
                    GCMToken = token;
                  //   Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();
                }
            }
        };


       // Log.v("Build ", " " + Integer.toString(Build.VERSION.SDK_INT) + " , " + Build.MODEL);


        Bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // Toast.makeText(getApplicationContext(), "clicked ", Toast.LENGTH_LONG).show();
                FieldsTextWatcher();
                if (checkValidation()) {
                    //GCMToken = "xcx";
                      if( !Sorted_Clist.contains(atv_country.getText().toString().trim()) ){
                        atv_country.setError("Please select from list");
                    }else if(!languadgeList.contains(atv_languadge.getText().toString().trim())){
                        atv_languadge.setError("Please select from list");
                    }

                    if (GCMToken == null) {

                        registerGCM();

                        //  Toast.makeText(getApplicationContext(), "Something went wrong. Try Again ", Toast.LENGTH_LONG).show();

                    }else{

                        PhoneNumberWithCode = et_umob_no.getText().toString();

                        StartRegistration();






                    }


                }
                    else
                    {
                      //  Toast.makeText(_activity, "Validation Failed...", Toast.LENGTH_SHORT).show();
                    }






            }
        });

        et_umob_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

              // et_umob_no.setGravity(Gravity.CENTER_HORIZONTAL);
                x = et_umob_no.getText().toString();
                et_umob_no.post(new Runnable() {
                    @Override
                    public void run() {
                        et_umob_no.setSelection(x.length());
                    }
                });


            }
        });

        //et_umob_no.setText("+91 8072876069");
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }




    private void FieldsTextWatcher() {
        FieldsValidator.hasTextUserName(et_uname);
        FieldsValidator.isEmailAddressOK(atv_uemail, true);
        FieldsValidator.isPhoneNumber(et_umob_no, true);
        FieldsValidator.hasText(atv_country);
        FieldsValidator.hasText(atv_languadge);

       /* int indc = Sorted_Clist.indexOf(atv_country.getText().toString());

        if (indc == 0 || indc > 0){

        }else {
            Toast.makeText(_activity, "Select your Country from LIST.", Toast.LENGTH_SHORT).show();
        }
       *//* if (atv_languadge.getText().toString().trim().contains(languages.toString())){

        }else {
            Toast.makeText(_activity, "Select your Language from LIST.", Toast.LENGTH_SHORT).show();
        }*//*
*/


    }


    public void Out_of_Store_alert() {

        final Dialog proceedDiscardDialog = new Dialog(RegistrationActivity.this,
                R.style.Theme_Transparent);
        SpannableString ss = new SpannableString("Alert this Package was downloaded from unknown sources ,Kindly download App from Playstore to get a secure copy."+"\n"+ "You can still proceed with added risks");

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 5, 0);
        ss.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
        proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
        TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
        tv_msg.setText( ss);

        Button btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
        btn_confirm.setText("Continue with risks");
        Button  btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
        proceedDiscardDialog.show();


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                proceedDiscardDialog.dismiss();

            }
        });
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceedDiscardDialog.dismiss();
                finish();

            }
        });



    }
    public static boolean isStoreVersion(Context context) {
        boolean result = false;

        try {
            String installer = context.getPackageManager()
                    .getInstallerPackageName(context.getPackageName());
            result = !TextUtils.isEmpty(installer);
        } catch (Throwable e) {
        }

        return result;
    }
    private void StartRegistration() {
         // OnVerificationStateChangedCallbacks

        final LoadingDialog LD;
        LD = new LoadingDialog(RegistrationActivity.this);
        LD.ShowTheDialog("Setting up your account...", "Please wait..", true);



        RestClient.get(RegistrationActivity.this).Registration(new RegRequest(et_uname.getText().toString().trim(), atv_uemail.getText().toString().trim(), PhoneNumberWithCode.trim(),
                atv_country.getText().toString().trim(), atv_languadge.getText().toString().trim(), Device_id.trim(), GCMToken, "Android", Integer.toString(Build.VERSION.SDK_INT), Build.MANUFACTURER + " " + Build.MODEL, "0","1","1"), new Callback<RegResp>() {
            @Override
            public void success(final RegResp arg0, Response arg1) {

                if (arg0.getCode().equals("200")) {


                    editor.putBoolean(SharedPrefUtils.SpRegisterSuccess, true);
                    PhoneNumberWithCode =  et_umob_no.getText().toString();
                    editor.putString(SharedPrefUtils.SpUserName, et_uname.getText().toString());
                    editor.putString(SharedPrefUtils.SpEmail, atv_uemail.getText().toString());
                    editor.putString(SharedPrefUtils.SpDeviceId, Device_id);
                    editor.putString(SharedPrefUtils.SpLanguage, atv_languadge.getText().toString());
                    editor.putString(SharedPrefUtils.SpPhone, PhoneNumberWithCode);
                    editor.putString(SharedPrefUtils.SpCountry, atv_country.getText().toString());
                    editor.putString(SharedPrefUtils.SpPhoneCode, CountryPhoneText);
                    editor.putString(SharedPrefUtils.SpGcmToken, StatiConstants.Gcm);
                    editor.putString(SharedPrefUtils.SpCountryFullName, atv_country.getText().toString());
                    editor.commit();




                    ChangeEmail=true;
                    LD.DismissTheDialog();


                    OTPVerificationIntent();

                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                    LD.DismissTheDialog();

                    Toast.makeText(RegistrationActivity.this, "Try again " + arg0.getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                LD.DismissTheDialog();

                Toast.makeText(RegistrationActivity.this, "Failure " + error, Toast.LENGTH_LONG).show();
            }
        });

    }



    private void OTPVerificationIntent() {


        Intent OtpVerify = new Intent(RegistrationActivity.this, OTPverification.class);

        OtpVerify.putExtra("email",atv_uemail.getText().toString());
        OtpVerify.putExtra("phnumber",PhoneNumberWithCode.trim());

        OtpVerify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(OtpVerify);

    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!FieldsValidator.hasTextUserName(et_uname)) ret = false;
        if (!FieldsValidator.isEmailAddressOK(atv_uemail, true)) ret = false;
        if (!FieldsValidator.isPhoneNumber(et_umob_no, true)) ret = false;
        if ((et_umob_no.length()< 6)) ret = false;
        return ret;
    }



    public void getUserEmails() {
        AccountManager manager = AccountManager.get(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();
        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }
        mailAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, possibleEmails);
        Log.v("", " mailAdapter " + mailAdapter);
        atv_uemail.setAdapter(mailAdapter);
    }

    // starting the service to register with GCM
    private void registerGCM() {

        try {
            GCMToken =   FirebaseInstanceId.getInstance().getToken();
            FirebaseMessaging.getInstance().subscribeToTopic("personalreceiver");
            // Toast.makeText(_activity, ""+GCMToken, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "firebase"+" "+GCMToken);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (GCMToken == null) {

            registerGCM();

                   }


        if(ChangeEmail){
            atv_uemail.requestFocus();
            atv_uemail.setText("");
        }else {
            et_uname.requestFocus();
        }

        if (StatiConstants.newSignIn){
            finish();
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.GET_ACCOUNTS}, 93);
                    //Intent2Activity();
                    NoPermisson =false;
                    getUserEmails();
                } else {
                    //CheckAndRequestPermission();
                    NoPermisson = true;
                    Toast.makeText(_activity, "This Permission is Required for the application to perform all basic functions, Kindly accept. For more information kindly vist our website.", Toast.LENGTH_LONG).show();
                   // finish();

                }
                break;
        }
    }

    public void CheckAndRequestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            /*if (checkPermission(Permission4)) {
               // Intent2Activity();
                return;
            }*/
            requestPermission(PermissionStrings.GET_ACCOUNTS);
        } else {
           // Intent2Activity();
        }
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, PERMISSION_REQUEST_CODE);
    }

}
