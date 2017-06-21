package com.quad.xpress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.quad.xpress.Utills.StatiConstants;
import com.quad.xpress.Utills.helpers.FieldsValidator;
import com.quad.xpress.Utills.helpers.LoadingDialog;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.PermissionStrings;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.profile_pic.profilepicResp;
import com.quad.xpress.models.receivedFiles.register.LanguageResp;
import com.quad.xpress.models.receivedFiles.register.ReqCL;
import com.quad.xpress.models.receivedFiles.register.RespCountry;
import com.quad.xpress.models.registration.RegRequest;
import com.quad.xpress.models.registration.RegResp;
import com.quad.xpress.webservice.RestClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by Venkatesh on 12-04-16.
 */
public class SettingsActivity extends AppCompatActivity implements
        CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener{


    ImageView Iv_user_pic, imv_bg;
    CropImageView mCropImageView;
    Context context;
    EditText tv_userName, tv_mobile;
    TextView tv_email,tv_user_name,tv_user_status,tv_spinner_lang_holder,tv_spinner_country_holder;
    Switch sw_reminder, sw_notification;
    SharedPreferences sharedpreferences;
    Spinner spin_country;
    Spinner spin_lang;
    Button BtnSave,btn_cancel;
    String LangNo = "";
    ScrollView scrollView;
    String LangNo_OnFirst = "English";
    ArrayList<String>lang_list = new ArrayList<>();
    ArrayList<String>country_list = new ArrayList<>();
    ArrayList<String>country_list_short = new ArrayList<>();
    ArrayList<String >lang_list_short = new ArrayList<>();
    ImageButton btn_edit_pic,btn_tb_back;
    String CountryCode, Permission4;
    String CountryCode_OnFirst;
    TypedFile typed_profile_pic;
    SharedPreferences.Editor editor;
    LoadingDialog LD;
    Activity _activity;
    NetConnectionDetector NDC;
    private int PICK_IMAGE_REQUEST = 1;
    Uri Imageuri;
    CircleImageView circleImageView_profile;
    ImageButton btn_scroll2btm;
    Dialog cropping_dialog;
    String notification=""+1 ,remainder=""+1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 91;
    TextView tv_help,tv_about,tv_support;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        context = getApplicationContext();
        _activity = this;
        Permission4 = PermissionStrings.WRITE_EXTERNAL_STORAGE;
        circleImageView_profile = (CircleImageView) findViewById(R.id.iv_settings_profile_circle);
        btn_edit_pic = (ImageButton) findViewById(R.id.btn_settings_edit_pic);
        btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        tv_user_name = (TextView) findViewById(R.id.settings_tv_username);
        tv_user_status = (TextView) findViewById(R.id.settings_tv_status);
        tv_spinner_lang_holder = (TextView) findViewById(R.id.tv_settings_spinner_holder);
        tv_spinner_country_holder = (TextView) findViewById(R.id.tv_settings_spinner_holder_country);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        CountryCode_OnFirst = sharedpreferences.getString(SharedPrefUtils.SpCountry, Locale.getDefault().getDisplayCountry());
        btn_scroll2btm = (ImageButton) findViewById(R.id.btn_settings_scroll_to_btm);
        LangNo_OnFirst = sharedpreferences.getString(SharedPrefUtils.SpLanguage, Locale.getDefault().getDisplayLanguage());
        editor = sharedpreferences.edit();
        LD = new LoadingDialog(_activity);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_userName = (EditText) findViewById(R.id.textView_userName);
        tv_mobile = (EditText) findViewById(R.id.textView_mobilenNumber);
        tv_email = (TextView) findViewById(R.id.textView_email);
        tv_help = (TextView) findViewById(R.id.tv_settings_help);
        tv_about = (TextView) findViewById(R.id.tv_settings_about);
        tv_support = (TextView) findViewById(R.id.tv_settings_support);

        NDC = new NetConnectionDetector();

        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemParent = new Intent(SettingsActivity.this,Act_HelpSupportAbout.class);
                ItemParent.putExtra("ItemParent","about");
                startActivity(ItemParent);
            }
        });
        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemParent = new Intent(SettingsActivity.this,Act_HelpSupportAbout.class);
                ItemParent.putExtra("ItemParent","support");
                startActivity(ItemParent);
            }
        });

        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show();
                Intent ItemParent = new Intent(SettingsActivity.this,Act_HelpSupportAbout.class);
                ItemParent.putExtra("ItemParent","help");
                startActivity(ItemParent);
            }
        });

        btn_scroll2btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });

      TextView  tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
      ImageButton  btn_back_tb= (ImageButton) findViewById(R.id.tb_normal_back);
        tv_tb_title.setText("My profile");
        btn_back_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        sw_reminder = (Switch) findViewById(R.id.switch_reminder);
        sw_notification = (Switch) findViewById(R.id.switch_Notify);

        spin_country = (Spinner) findViewById(R.id.spinner_country);
        spin_lang = (Spinner) findViewById(R.id.spinner_language);
        BtnSave = (Button) findViewById(R.id.btn_save_settings);
        btn_cancel = (Button) findViewById(R.id.button_setting_cancel);
        imv_bg = (ImageView) findViewById(R.id.dash_imv_bg);
        Iv_user_pic = (ImageView) findViewById(R.id.imageView_profie_pic);

        tv_userName.setText(sharedpreferences.getString(SharedPrefUtils.SpUserName, ""));
        tv_mobile.setText(sharedpreferences.getString(SharedPrefUtils.SpPhone, ""));
        tv_email.setText(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""));
        tv_user_name.setText(sharedpreferences.getString(SharedPrefUtils.SpUserName, ""));
      //  Toast.makeText(context, ""+StatiConstants.user_profilepic_url, Toast.LENGTH_SHORT).show();
        if(StatiConstants.followers_following!=null){
            tv_user_status.setText(StatiConstants.followers_following);
        }
        if(StatiConstants.user_profilepic_url!= null){
        //    Toast.makeText(context, "nn"+StatiConstants.user_profilepic_url, Toast.LENGTH_SHORT).show();
            try {
                Glide.with(context).load(StatiConstants.user_profilepic_url).bitmapTransform(new BlurTransformation(context)).into(imv_bg);
                Glide.with(context).load(StatiConstants.user_profilepic_url).into(circleImageView_profile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        get_lists();

        //Spinner Placeholder


        tv_spinner_lang_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_lang.performClick();
            }
        });

        tv_spinner_country_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_country.performClick();
            }
        });
        tv_spinner_country_holder.setText(CountryCode_OnFirst);
        spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_spinner_country_holder.setText(spin_country.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_spinner_lang_holder.setText(spin_lang.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sw_reminder.setChecked(sharedpreferences.getBoolean(SharedPrefUtils.SpSettingRemainder, true));
        sw_notification.setChecked(sharedpreferences.getBoolean(SharedPrefUtils.SpSettingNotification, true));

        Glide.with(context).load((R.drawable.ic_user_icon)).asBitmap().centerCrop().into(new BitmapImageViewTarget(Iv_user_pic));


      //  Toast.makeText(get)

        btn_edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setContentView(R.layout.cropping_image);

                CheckAndRequestPermission();





            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
        });



        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (FieldsValidator.hasTextUserName(tv_userName) && FieldsValidator.isPhoneNumber(tv_mobile, true))
                {
                    if(!NDC.isOnline()){

                        Toast.makeText(context, "Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                    }else {
                        SaveInServer();
                    }


                }


            }
        });



        sw_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SharedPrefUtils.SpSettingRemainder, isChecked);
              //  Log.v("Setting Switch", "sw_reminder " + isChecked);
                if(isChecked){remainder = ""+1;}
                else {remainder= ""+0;}
              //  editor.commit();

            }
        });

        sw_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                editor.putBoolean(SharedPrefUtils.SpSettingNotification, isChecked);
              //  Log.v("Setting Switch", "sw_notification " + isChecked);
                if(isChecked){notification = ""+1;}
                else {notification= ""+0;}
              //  editor.commit();
                        }

        });

    }

    public void CheckAndRequestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            if (checkPermission(Permission4)) {
                // Intent2Activity();
                return;
            }
            requestPermission(Permission4);
        } else {
            // Intent2Activity();
        }

    }

    private boolean checkPermission(String permission) {

        int result = ContextCompat.checkSelfPermission(_activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
            return true;
        } else {

            return false;
        }
    }

    private void permissionGranted() {
        cropping_dialog = new Dialog(SettingsActivity.this, R.style.AVdialouge);
        cropping_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        cropping_dialog.setContentView(R.layout.cropping_image);


        mCropImageView = (CropImageView) cropping_dialog.findViewById(R.id.croppingImageView);
        init();

        CropImage.startPickImageActivity(SettingsActivity.this);
        Button close = (Button) cropping_dialog.findViewById(R.id.btn_ci_exit);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BtnSave.setVisibility(View.VISIBLE);
                cropping_dialog.dismiss();

                // onBackPressed();
            }
        });
        ImageButton rotate = (ImageButton) cropping_dialog.findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView.rotateImage(90);
            }
        });

        Button crop = (Button) cropping_dialog.findViewById(R.id.cropping);
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView.getCroppedImageAsync();
                BtnSave.setVisibility(View.VISIBLE);

            }
        });
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, STORAGE_PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted();
                    //Intent2Activity();
                } else {
                   /*CheckAndRequestPermission();
                    Toast.makeText(_context, "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();*/
                  //  this.finish();
                    Toast.makeText(context, "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();
                   // CheckAndRequestPermission();

                }
                break;

        }
    }

    private void init() {

        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);


    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE &&
                resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {

             Imageuri = data.getData();
            BtnSave.setVisibility(View.INVISIBLE);
            cropping_dialog.show();


            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            mCropImageView.setImageUriAsync(imageUri);

        }
    }



    private void get_lists() {

        LD.ShowTheDialog("Please Wait", "Loading..", true);

        final ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(this,R.layout.spinner_custom_settings, country_list);


        RestClient.get(this).GetCountry(new ReqCL("country"), new Callback<RespCountry>() {
            @Override
            public void success(RespCountry respCountry, Response response) {
                if(respCountry.getCode().equals("200")) {
                    int RLength = respCountry.getData().length;
                    for (int i = 0; i < RLength; i++) {

                        country_list.add(respCountry.getData()[i].getCountry_name());
                        country_list_short.add(respCountry.getData()[i].getIso());

                    }
                    country_list.add(0, CountryCode_OnFirst);
                    spin_country.setAdapter(adapter_country);

                    mtd_langs();
                }


            }

            @Override
            public void failure(RetrofitError error) {

                country_list.add(0,Locale.getDefault().getDisplayCountry());
                spin_country.setAdapter(adapter_country);

            }
        });


    }


    private void mtd_langs() {

        final ArrayAdapter<String> adapter_lanugage = new ArrayAdapter<String>(this,R.layout.spinner_custom_settings, lang_list);

        RestClient.get(this).GetLanguage(new ReqCL("language"), new Callback<LanguageResp>() {


            @Override
            public void success(LanguageResp languageResp, Response response) {
                if(languageResp.getCode().equals("200")) {
                    int size =  languageResp.getData().length;
                    for (int i = 0; i < size; i++) {
                        lang_list.add(languageResp.getData()[i].getName());
                        lang_list_short.add(languageResp.getData()[i].getIso());
                    }


                    lang_list.add(0,LangNo_OnFirst);
                    spin_lang.setAdapter(adapter_lanugage);
                    tv_spinner_lang_holder.setText(lang_list.get(0));
                    LD.DismissTheDialog();

                    scrollView.scrollTo(0, (int) btn_scroll2btm.getY());

                }


            }

            @Override
            public void failure(RetrofitError error) {

                lang_list.add(0,LangNo_OnFirst);
                spin_lang.setAdapter(adapter_lanugage);
                LD.DismissTheDialog();

            }

        });


    }


    private void SaveInServer() {
        LD.ShowTheDialog("Please Wait", "Loading..", true);
        LangNo=spin_lang.getSelectedItem().toString();
        CountryCode=spin_country.getSelectedItem().toString();


        RestClient.get(_activity).Registration(new RegRequest(tv_userName.getText().toString(),
                sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), tv_mobile.getText().toString(),
                CountryCode, LangNo, sharedpreferences.getString(SharedPrefUtils.SpDeviceId, ""),
                sharedpreferences.getString(SharedPrefUtils.SpGcmToken, ""), "Android",
                Integer.toString(Build.VERSION.SDK_INT), Build.MANUFACTURER + " " + Build.MODEL,"1",notification,remainder), new Callback<RegResp>() {
            @Override
            public void success(final RegResp arg0, Response arg1) {
                LD.DismissTheDialog();
                if (arg0.getStatus().equals("OK")) {

                    editor.putString(SharedPrefUtils.SpUserName, tv_userName.getText().toString().trim());
                    editor.putString(SharedPrefUtils.SpEmail, tv_email.getText().toString().trim());
                    editor.putString(SharedPrefUtils.SpPhone, tv_mobile.getText().toString().trim());
                    editor.putString(SharedPrefUtils.SpLanguage, LangNo);
                    editor.putString(SharedPrefUtils.SpCountry, CountryCode);
                    editor.commit();
                    btn_cancel.setText("Back");

                    Toast.makeText(_activity, "Saved...", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(_activity, "Settings save failed. Try again later ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LD.DismissTheDialog();
                Toast.makeText(_activity, "Settings save failed. Try again later " + error, Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if(result != null){
            /*Bitmap b = mCropImageView.getCroppedImage();
            imv_bg.setImageBitmap(b);*/
            callWebService();
            cropping_dialog.dismiss();
        }
    }

    private void callWebService() {

        try {



            final Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(),
                    mCropImageView.getImageUri());

            //Log.d("", String.valueOf(bitmap));
             //StatiConstants.user_profilepic_url = mCropImageView.getImageUri());
                Glide.with(context).load((R.drawable.ic_user_icon)).asBitmap().centerCrop().into(new BitmapImageViewTarget(Iv_user_pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        //imv_bg.setImageBitmap(bitmap);
                    }
                });
            Bitmap bitmap = mCropImageView.getCroppedImage();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);

        String filePath = getRealPathFromURI(Uri.parse(path));

        typed_profile_pic = new TypedFile("image/jpg", new File(filePath));

        RestClient.get(context).UploadProfilePic(typed_profile_pic, sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), new Callback<profilepicResp>() {
            @Override
            public void success(profilepicResp profilepicRes, Response response) {
                if(profilepicRes.getCode().equals("200")){
                    // Toast.makeText(context, "Success "+profilepicRes.getData().getFilepath(), Toast.LENGTH_SHORT).show();
                    String imgUrl= profilepicRes.getData().getFilepath();
                    if (imgUrl.contains(StaticConfig.ROOT_URL_Media)) {
                        imgUrl = StaticConfig.ROOT_URL + imgUrl.replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        imgUrl = StaticConfig.ROOT_URL + "/" +imgUrl;
                    }


                    Glide.with(context).load(imgUrl).placeholder(R.drawable.ic_user_icon)
                            .bitmapTransform(new BlurTransformation(context)).fitCenter().into(imv_bg);
                    Glide.with(context).load(imgUrl).into(circleImageView_profile);
                    StatiConstants.user_profilepic_url = imgUrl;

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("typed file Error", error.toString());
            }
        });

    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {

           // cropping_dialog.dismiss();
           //  Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Image load failed "+error, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  Toast.makeText(context, "Bk ex", Toast.LENGTH_SHORT).show();
        this.LD.DismissTheDialog();
        this.finish();

    }


}
