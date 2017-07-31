package com.quad.xpress;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.contacts.Contact;
import com.quad.xpress.contacts.ContactMainActivity;
import com.quad.xpress.contacts.DatabaseHandler;
import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.utills.StatiConstants;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.PermissionStrings;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.localNotification.LocalNotify;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.models.send.SVideoResp;
import com.quad.xpress.models.videocapture.MediaHelper;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.wysaid.camera.CameraInstance;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static com.quad.xpress.DashBoard.FileNameWithMimeType;


public class CameraRecordActivity extends Activity implements View.OnClickListener{



    ArrayList<String> EmailTemp = new ArrayList<>();

    CounterClass timer;
    Animation animBlink;
    //private Context myContext;
    static int MapKey;
    LinearLayout linearLayoutbtm;
    RelativeLayout m_linear_feelingwith;
    private CameraRecordGLSurfaceView mCameraView;
    ToastCustom toastCustom;
    RelativeLayout toolBar;
    public final static String LOG_TAG = CameraRecordGLSurfaceView.LOG_TAG;

    Button  customFilter1, customFilter2, customFilter3, customFilter4;
   // MyCameraProperty myCameraProperty;
    boolean FrontCamAvailable = false;

    ImageButton recordBtn;
    TextView tv_spinner_mode;
    Button SdDiscard, SdSend;
    HorizontalScrollView Hs_filter;
    String AppName;
    String DEBUG_TAG = "Xpress";
    private Uri fileUri;
    ArrayAdapter AVTagsListAdapter;
    private String mCurrentConfig;
    int ShareAsPos = 0;
    ArrayList<String> emlRecs = new ArrayList<String>();
    Dialog AVDialog;
    String ShareAsText = "Private";
    Spinner Spinner_ShareAsType;
    String ToEmail = "";
    String AVTitle = "";
    String Tags = "";
    AutoCompleteTextView av_email;
    boolean GoingToRecordVideo = true;
    MultiAutoCompleteTextView MATTags;
    TextInputLayout TagsTIL;
    Activity _activity;
    TextView tv_title;
    public static final int CONTACT_PICKER_RESULT = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 33;
    private static final int VIDEO_PERMISSION_REQUEST_CANCEL = 93;
    private static final int VIDEO_PERMISSION_REQUEST_CODE = 91;
    private static final int AUDIO_PERMISSION_REQUEST_CODE = 92;
    private static final int GET_ACCOUNTS_Reqcode = 11;

    String ToSaveURI;
    int LOCAL_NOTIFY_STATIC_ID = 20;
    LocalNotify localNotify;
    NetConnectionDetector CD;
    String RefreshTokenMethodName = "";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TypedValue typedValue;
    LinearLayout linear_discard, Linear_cameracapture;
    PulsatorLayout pulsator;
    PulsatorLayout pulsator_dialouge;

    int contactPosition, recentPosition ;
    String mTempName, mTempEmail;


    private TextView timerValue;
    Button switchBtn;
    String Permission4, permission5, permission6, Permission2;



    private void DeleteUploadedFile(Uri UploadedFileUri) {
        if (UploadedFileUri != null) {
            new File(String.valueOf(UploadedFileUri)).delete();
        }
    }


    public String getThumbNailUri(Uri VideoUri) {
        String FileNamePath = "/sdcard/ixprez/ThumbTemp.png";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileNamePath);
            Bitmap bitmap = MediaHelper.GetThumbnailFromVideo(VideoUri, 0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileNamePath;
    }

    public void VideoUploadToServer(final Uri videoUri) {
        //  LD.ShowTheDialog("Please Wait", "Sending..", false);

        LOCAL_NOTIFY_STATIC_ID = LOCAL_NOTIFY_STATIC_ID + 2;
        localNotify.ShowFileUploadNotification(AVTitle + " ", LOCAL_NOTIFY_STATIC_ID);
        final TypedFile VideoTypedFile = new TypedFile("video/mp4", new File(String.valueOf(videoUri)));
        //    TypedFile ThumbnailTypedFile = new TypedFile("video/mp4", new File(String.valueOf(videoUri)));

        final TypedFile ThumbnailTypedFile = new TypedFile("image/png", new File(getThumbNailUri(videoUri)));
        //  Log.v("ThumbNail Uri", " " + getThumbNailUri(context, videoUri));
        String sptoken = sharedpreferences.getString(SharedPrefUtils.SpToken, "");
        String spCOuntry = sharedpreferences.getString(SharedPrefUtils.SpCountry, "");
        String spLauguge = sharedpreferences.getString(SharedPrefUtils.SpLanguage, "");
        Log.v("preference", sptoken + spCOuntry + spLauguge);


    //    Toast.makeText(_activity, ""+ToEmail+AVTitle+ShareAsText+VideoTypedFile+ThumbnailTypedFile, Toast.LENGTH_SHORT).show();
        StatiConstants.mapTo = ToEmail;
        StatiConstants.mapTitile = AVTitle;
        StatiConstants.mapType = ShareAsText;
        StatiConstants.mapVideoTyped = VideoTypedFile;
        StatiConstants.mapThumbnailTyped = ThumbnailTypedFile;

        RestClient.get(getApplicationContext()).UploadVideo(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), VideoTypedFile, sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), ToEmail, AVTitle, Tags, ShareAsText,
                sharedpreferences.getString(SharedPrefUtils.SpCountry, "IN"), sharedpreferences.getString(SharedPrefUtils.SpLanguage, Locale.getDefault().getDisplayLanguage()), ThumbnailTypedFile, new Callback<SVideoResp>() {
                    @Override
                    public void success(SVideoResp sVideoResp, Response response) {
                        if (sVideoResp.getCode().equals("200")) {

                            localNotify.FinishedUploadNotification(LOCAL_NOTIFY_STATIC_ID, AVTitle + " successful.");

                            SpannableString ss = new SpannableString(AVTitle+ " xpressed to "+ ToEmail);

                            ss.setSpan(new ForegroundColorSpan(Color.RED), 0, AVTitle.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), 0, AVTitle.length(), 0);
                            ss.setSpan(new ForegroundColorSpan(Color.GREEN), ss.length()-ToEmail.length(), ss.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), ss.length()-ToEmail.length(), ss.length(), 0);
                          //  toastCustom.ShowToast("Ixprez",ss,2);
                          //  Toast.makeText(getApplicationContext(), "Xpressed ", Toast.LENGTH_LONG).show();

                            File dir = new File(Environment.getExternalStorageDirectory() + "/" + AppName + "/");
                            if (dir.isDirectory())
                            {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++)
                                {
                                    new File(dir, children[i]).delete();
                                }
                            }

                            DeleteUploadedFile(videoUri);

                        } else if (sVideoResp.getCode().equals("601")) {
                            RefreshTokenMethodName = "VideoUploadToServer";
                            RefreshToken(videoUri);
                            // Toast.makeText(context, "RefreshTokenMethodName ", Toast.LENGTH_LONG).show();

                        }
                        else if (sVideoResp.getCode().equals("701")) {
                           // Toast.makeText(getApplicationContext(), "User has blocked you !", Toast.LENGTH_LONG).show();
                            localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID,  "You have been Blocked by user",_activity, videoUri);
                            DeleteUploadedFile(videoUri);

                        }
                        else {
                            localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID, "Upload failed. " + sVideoResp.getCode(),_activity,videoUri);
                            DeleteUploadedFile(videoUri);

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        localNotify.UploadNotificationFailedRetry(LOCAL_NOTIFY_STATIC_ID, AVTitle,_activity,VideoTypedFile,ToEmail,ShareAsText,ThumbnailTypedFile);

                       // DeleteUploadedFile(videoUri);
                    }
                });
    }

    public void RefreshToken(final Uri FileUri) {

        RestClient.get(getApplicationContext()).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {

                if (arg0.getCode().equals("200")) {
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();
                    if (RefreshTokenMethodName.equals("VideoUploadToServer")) {
                        VideoUploadToServer(FileUri);
                    }
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(CameraRecordActivity.this, "Connectivity Problem ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    public static class MyButtons extends Button {

        public String filterConfig;

        public MyButtons(Context context, String config) {
            super(context);
            filterConfig = config;
        }
    }

    private View.OnClickListener mFilterSwitchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            MyButtons btn = (MyButtons)v;
            v.setBackgroundResource(R.drawable.btn_selector_filter);

            /* if (typedValue.resourceId != 0) {
                v.setBackgroundResource(typedValue.resourceId);
            } else {
                // this should work whether there was a resource id or not
                v.setBackgroundColor(typedValue.data);
            }*/
            mCameraView.setFilterWithConfig(btn.filterConfig);
            mCurrentConfig = btn.filterConfig;
            //cc
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_video_capture);
        toolBar = (RelativeLayout) findViewById(R.id.tb_top);
        Hs_filter = (HorizontalScrollView) findViewById(R.id.scroll_filter);

         typedValue = new TypedValue();
         getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        toastCustom = new ToastCustom(CameraRecordActivity.this);

        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);

        _activity = this;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        localNotify = new LocalNotify(getApplicationContext(), mBuilder,_activity);
        LinearLayout layout = (LinearLayout) findViewById(R.id.menuLinearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);

        String effectConfigsLocal[] = {

                StatiConstants.effectConfigs[99],
                StatiConstants.effectConfigs[39],
                StatiConstants.effectConfigs[19],
                StatiConstants.effectConfigs[11],
                StatiConstants.effectConfigs[12],
                StatiConstants.effectConfigs[59],
                StatiConstants.effectConfigs[62],
                StatiConstants.effectConfigs[26],
                StatiConstants.effectConfigs[25],
                StatiConstants.effectConfigs[24]

        };



        String effectConfigsLocalname[] = {
                "Gorgeous","Cold","Charcoal","Waves","Pencil","Chrome","Lomo","Aqua","Reddish","Dotted"
        };


        for(int i = 0; i != effectConfigsLocal.length; ++i) {

            MyButtons button = new MyButtons(this, effectConfigsLocal[i]);
            button.setAllCaps(false);
            button.setTextColor(Color.BLACK);
            button.setTypeface(null, Typeface.ITALIC);
            button.setBackgroundResource(R.drawable.btn_roundededges_videorecorder);
            button.setAlpha((float) .5);

            button.setLayoutParams(params);

            button.setText(effectConfigsLocalname[i]);



            button.setOnClickListener(mFilterSwitchListener);
            layout.addView(button);
        }



        Permission4 = PermissionStrings.WRITE_EXTERNAL_STORAGE;
        permission5 = PermissionStrings.CAMERA;
        permission6 = PermissionStrings.RECORD_AUDIO;
        Permission2 = PermissionStrings.GET_ACCOUNTS;

        AppName = getResources().getString(R.string.app_name);
        linearLayoutbtm = (LinearLayout) findViewById(R.id.act_video_capture_btn_linerar);
        linearLayoutbtm.setVisibility(View.INVISIBLE);

        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String Tags[] = StatiConstants.TagList.split(",");

       /* for (int i = 0; i <AVTagsList.length ; i++) {
            int insertposition = Tags.length+i;
            Tags[insertposition] = AVTagsList[i];
        }*/
        AVTagsListAdapter = new ArrayAdapter(this, R.layout.spinner_autofill_av_dialouge, Tags);
        AVDialog = new Dialog(CameraRecordActivity.this, R.style.AVdialouge);

       /* AVDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AVDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);*/


        AVDialog.setContentView(R.layout.av_details);
        tv_spinner_mode= (TextView) AVDialog.findViewById(R.id.tv_avd_spinner_holder);
        av_email = (AutoCompleteTextView) AVDialog.findViewById(R.id.av_email);
        ImageButton btn_AVD_back = (ImageButton) AVDialog.findViewById(R.id.tb_normal_back);
        TextView tv_AVD_title = (TextView) AVDialog.findViewById(R.id.tb_normal_title);
        tv_title = (TextView) findViewById(R.id.tb_normal_title);
        ImageButton btn_back = (ImageButton) findViewById(R.id.tb_normal_back);
        ImageButton btn_help = (ImageButton) AVDialog.findViewById(R.id.btn_avd_help);


        ImageButton btnContacts = (ImageButton) AVDialog.findViewById(R.id.btn_contacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CameraRecordActivity.this, ContactMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(CameraRecordActivity.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Share As ?");
                builderSingle.setMessage(R.string.help);

                builderSingle.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Spinner_ShareAsType.performClick();
                        dialog.dismiss();
                    }
                });
                builderSingle.show();

            }
        });

        tv_spinner_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner_ShareAsType.performClick();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        m_linear_feelingwith = (RelativeLayout) AVDialog.findViewById(R.id.ll_share_target_parent);
        pulsator_dialouge = (PulsatorLayout) AVDialog.findViewById(R.id.pulsator);
        pulsator_dialouge.start();

        try {

            recentPosition = getIntent().getExtras().getInt("ixpresspositionEmailID");
            contactPosition = getIntent().getExtras().getInt("positionEmailID");
            if(recentPosition == 1){
                if(DatabaseHandler.dbEmailList.get(contactPosition) != null && contactPosition != -1){
                    av_email.setText(DatabaseHandler.dbEmailList.get(contactPosition));
                }

            }else{

                mTempEmail = getIntent().getExtras().getString("tempEmail");
                mTempName = getIntent().getExtras().getString("tempName");

                if(Contact.getInstance().ixpressemail.get(contactPosition) != null && contactPosition != -1) {
                    av_email.setText(getIntent().getExtras().getString("tempEmail"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        linear_discard = (LinearLayout) findViewById(R.id.linear_discard_button);
        Linear_cameracapture = (LinearLayout) findViewById(R.id.linear_camera_capture);


        SdDiscard = (Button) findViewById(R.id.sd_discard);
        SdSend = (Button) findViewById(R.id.sd_send);

        SdSend.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Uri RecordedFileUri = null;

                                        /*  if (CD.isConnected(getApplicationContext())) {*/
                                               RecordedFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + AppName + "/" +
                                                      FileNameWithMimeType);
                                           //   Log.v("", "RecordedFileUri " + RecordedFileUri);
                                              VideoUploadToServer(RecordedFileUri);
                                              finish();

                                        /*  }else{

                                              localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID, "Slow Internet Connection, Push failed, TryAgain",_activity,RecordedFileUri);
                                              Toast.makeText(_activity, "You are offline", Toast.LENGTH_SHORT).show();
                                              finish();
                                          }*/


                                      }
                                  }
        );

        SdDiscard.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                           DeleteUploadedFile(fileUri);
                                             linearLayoutbtm.setVisibility(View.INVISIBLE);
                                             AVDialog.show();
                                         }
                                     }
        );



        tv_AVD_title.setText("Capture your feelings...");


        CD = new NetConnectionDetector();


        if (CD.isConnected(getApplicationContext())) {
            RefreshTokenMethodName = "";
            final Uri UriDummy = Uri.parse("");
            RefreshToken(UriDummy);
        }


        AlphaAnimation blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);

        GoingToRecordVideo = true;
        AVDetailsDialog(GoingToRecordVideo, ".mp4");



        timerValue = (TextView) findViewById(R.id.timerValue);
        recordBtn = (ImageButton) findViewById(R.id.button_capture);
        mCameraView = (CameraRecordGLSurfaceView)findViewById(R.id.myGLSurfaceView);
        mCameraView.presetCameraForward(true);
        mCameraView.setFitFullView(true);




        customFilter1 = (Button)  findViewById(R.id.customFilterBtn1);
        customFilter2 = (Button)  findViewById(R.id.customFilterBtn2);
        customFilter3 = (Button)  findViewById(R.id.customFilterBtn3);
        customFilter4 = (Button)  findViewById(R.id.customFilterBtn4);

        customFilter1.setOnClickListener(this);
        customFilter2.setOnClickListener(this);
        customFilter3.setOnClickListener(this);
        customFilter4.setOnClickListener(this);





        timer = new CounterClass(40000, 1000);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        recordBtn.setOnClickListener(new RecordListener());

        btn_AVD_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                linearLayoutbtm.setVisibility(View.INVISIBLE);
                AVDialog.show();
            }
        });

        AVDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK)

                {

                    onBackPressed();

                }



                return false;
            }
        });



        switchBtn = (Button)findViewById(R.id.ivFlipCam1);
        switchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraView.switchCamera();
            }
        });
      /*  if (FrontCamAvailable) {

            switchBtn.setVisibility(View.VISIBLE);

        }else{
            switchBtn.setVisibility(View.GONE);
        }*/
        mCameraView.stopPreview();
        switchBtn.post(new Runnable() {
            @Override
            public void run() {
                switchBtn.performClick();
            }
        });

        //640 × 480
     /*   mCameraView.setPictureSize(480, 640, true);
        mCameraView.presetRecordingSize(480, 640);*/
        mCameraView.setPictureSize(720, 1280, true);
        mCameraView.presetRecordingSize(720, 1280);
//      mCameraView.presetRecordingSize(720, 1280);
        mCameraView.setZOrderOnTop(true);
        mCameraView.setZOrderMediaOverlay(true);

        mCameraView.setOnCreateCallback(new CameraRecordGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                if (success) {
                    Log.i(LOG_TAG, "view create OK");
                } else {
                    switchBtn.post(new Runnable() {
                        @Override
                        public void run() {
                            switchBtn.performClick();
                        }
                    });
                    Log.e(LOG_TAG, "view create failed!");
                }
            }
        });

        mCameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.i(LOG_TAG, String.format("Tap to focus: %g, %g", event.getX(), event.getY()));
                        final float focusX = event.getX() / mCameraView.getWidth();
                        final float focusY = event.getY() / mCameraView.getHeight();

                        mCameraView.focusAtPoint(focusX, focusY, new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                    Log.e(LOG_TAG, String.format("Focus OK, pos: %g, %g", focusX, focusY));
                                } else {
                                    Log.e(LOG_TAG, String.format("Focus failed, pos: %g, %g", focusX, focusY));
                                    mCameraView.cameraInstance().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                                }
                            }
                        });
                    }
                    break;
                    default:
                        break;
                }

                return true;
            }
        });



      //  mCameraView.setPictureSize(600, 800, true);
        CheckAndRequestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case GET_ACCOUNTS_Reqcode:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.GET_ACCOUNTS}, 93);

                }
              else{

                   /* Toast.makeText(getApplicationContext(), "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();*/
               this.finish();

                }

                break;

            case VIDEO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.CAMERA},
                            VIDEO_PERMISSION_REQUEST_CANCEL);

                    mCameraView.stopPreview();

                    switchBtn.post(new Runnable() {
                        @Override
                        public void run() {

                            mCameraView.setEnabled(true);
                            mCameraView.cameraInstance().isPreviewing();

                     //       switchBtn.performClick();


                        }
                    });

                } else {
                /*
                    Toast.makeText(getApplicationContext(), "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();*/
                    this.finish();
                  //  CheckAndRequestPermission();

                }
                break;
            case VIDEO_PERMISSION_REQUEST_CANCEL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.RECORD_AUDIO},
                            AUDIO_PERMISSION_REQUEST_CODE);

                    mCameraView.stopPreview();
                    switchBtn.post(new Runnable() {
                        @Override
                        public void run() {

                            mCameraView.setEnabled(true);
                            mCameraView.cameraInstance().isPreviewing();


                        }
                    });

                }else{
                 /*   Toast.makeText(getApplicationContext(), "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();*/
                    this.finish();
                }
            break;
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mCameraView.stopPreview();

                    switchBtn.post(new Runnable() {
                        @Override
                        public void run() {
                            mCameraView.setEnabled(true);
                            mCameraView.cameraInstance().isPreviewing();

                         //   switchBtn.performClick();


                        }
                    });





                }else{
                    /*Toast.makeText(getApplicationContext(), "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();
                    */
                    this.finish();

                }

                break;
        }
    }

    public void CheckAndRequestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            if (checkPermission(Permission2) && checkPermission(Permission4) && checkPermission(permission5) && checkPermission(permission6)) {

                return;
            }
            requestPermission(Permission4);

        } else {
            // Intent2Activity();
        }

    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, VIDEO_PERMISSION_REQUEST_CODE);
    }


    private void AVDetailsDialog(final boolean GoingToRecordVideo, final String fileMimeType) {

        AVDialog.show();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            AVDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pulsator_dialouge.stop();

            pulsator.start();

        }

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        AVDialog.setCanceledOnTouchOutside(false);
        Spinner_ShareAsType = (Spinner) AVDialog.findViewById(R.id.av_share_type);
        TagsTIL = (TextInputLayout) AVDialog.findViewById(R.id.av_tags_til);
        MATTags = (MultiAutoCompleteTextView) AVDialog.findViewById(R.id.av_tags);
        MATTags.setAdapter(AVTagsListAdapter);
        MATTags.setThreshold(1);
        MATTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        final ImageButton btn_start_recording = (ImageButton) AVDialog.findViewById(R.id.btn_start_recording);
        Button btn_cancel_recording = (Button) AVDialog.findViewById(R.id.button_cancel_recording);
        final EditText av_name = (EditText) AVDialog.findViewById(R.id.av_name);

        try {
            AutoSuggestAdapter adapter = new AutoSuggestAdapter(this,
                    R.layout.spinner_autofill_av_dialouge, mtd_conts());
            av_email.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        av_email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == EditorInfo.IME_ACTION_NEXT  ) {
                    av_name.requestFocus();
                }

                return false;
            }
        });

        TagsTIL.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_NEXT  ) {
                    av_email.requestFocus();
                }

                return false;
            }
        });

        av_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_GO || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    btn_start_recording.performClick();
                }

                return false;
            }
        });



        ArrayAdapter<CharSequence> adapter_Type = ArrayAdapter.createFromResource(this, R.array.share_type, R.layout.spinner_custom_settings);
        adapter_Type.setDropDownViewResource(R.layout.spinner_custom);



        Spinner_ShareAsType.setAdapter(adapter_Type);

        Spinner_ShareAsType.setOnItemSelectedListener(new ShareTypeListener());






       // btn_start_recording.setText("Proceed");
        //btn_start_recording.setAlpha(1);

        btn_cancel_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVDialog.cancel();
            }
        });
        btn_start_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayoutbtm.setVisibility(View.VISIBLE);
                Linear_cameracapture.setVisibility(View.VISIBLE);
                Hs_filter.setVisibility(View.VISIBLE);
                recordBtn.setBackgroundResource(R.drawable.circle_with_cemara);
                timer = new CounterClass(40000, 1000);
                timerValue.setText("00:40");

                linear_discard.setVisibility(View.GONE);

                if (av_email.getVisibility() == view.VISIBLE) {
                    if (!FieldsValidator.isEmailAddressOK(av_email, true)) {
                        return;
                    }
                }

                if (av_name.getVisibility() == view.VISIBLE) {
                    if (!FieldsValidator.hasText(av_name)) {
                        return;
                    }
                }

                if (TagsTIL.getVisibility() == view.VISIBLE) {
                    if (!FieldsValidator.hasTextTags(MATTags)) {
                        return;
                    }
                }

                ToEmail = av_email.getText().toString();
                AVTitle = av_name.getText().toString();
                tv_title.setText(AVTitle);


                String AvTitleSpaceRemover = AVTitle.replace(" ", "-");
                FileNameWithMimeType = AvTitleSpaceRemover + fileMimeType;

                ToSaveURI = CreateVideoFolder(MEDIA_TYPE_VIDEO, FileNameWithMimeType);
                Log.v("Camera ToSaveUri", ToSaveURI);
                Tags = TagResizer(MATTags.getText().toString());
                ShareAsText = Spinner_ShareAsType.getSelectedItem().toString();
                if (AVDialog.isShowing()) {
                   // av_email.setText("");
                   // av_name.setText("");
                   // MATTags.setText("");

                    callWeb(ToEmail);
                    AVDialog.dismiss();

                }

            }

            private void callWeb(final String toEmail) {
                ArrayList<String> emailList = new ArrayList<String>();
                emailList.add(toEmail);
                RestClient.get(CameraRecordActivity.this).PostContacts(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new ContactsReq(emailList),
                        new Callback<ContactsResp>() {
                            @Override
                            public void success(ContactsResp contactsResp, Response response) {

                                if(recentPosition == 0) {
                                    if (contactPosition == -1) {
                                        for (int i = 0; i < Contact.getInstance().ixpressemail.size(); i++) {

                                            if (Contact.getInstance().ixpressemail.get(i).equalsIgnoreCase(toEmail)) {
                                                DatabaseHandler handler = new DatabaseHandler(CameraRecordActivity.this);
                                                handler.addContact(Contact.getInstance().ixpressemail.get(i),
                                                        Contact.getInstance().ixpressname.get(i),
                                                        Contact.getInstance().ixpress_user_pic.get(i)
                                                        , contactPosition);
                                            }
                                        }

                                    } else {
                                        if (contactsResp.getData().length != 0) {
                                            for (int i = 0; i < contactsResp.getData().length; i++) {
                                                String emailID = contactsResp.getData()[i].getEmail_id();
                                                String name = contactsResp.getData()[i].getUser_name();
                                                String pic_url = contactsResp.getData()[i].getProfile_image();

                                                if (emailID != null && name != null && pic_url != null) {
                                                    DatabaseHandler handler = new DatabaseHandler(CameraRecordActivity.this);
                                                    handler.addContact(emailID, name, pic_url, contactPosition);
                                                }
                                            }
                                        } else {

                                            DatabaseHandler handler = new DatabaseHandler(CameraRecordActivity.this);
                                            handler.addContact(mTempEmail,
                                                    mTempName,
                                                    Contact.getInstance().ixpress_user_pic.get(contactPosition)
                                                    , contactPosition);
                                        }
                                    }
                                }else{
                                   /* DatabaseHandler handler = new DatabaseHandler(CameraRecordActivity.this);
                                    handler.deleteContact(contactPosition);*/
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("ixpressemail error", "error");
                            }
                        });
            }
        });

        /*av_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (av_email.getRight() - av_email.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (!checkPermission(PermissionStrings.GET_ACCOUNTS)) {
                                ActivityCompat.requestPermissions(_activity, new String[]{PermissionStrings.GET_ACCOUNTS}, CONTACT_PERMISSION_REQUEST_CODE);
                                return false;
                            }
                        }
                        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
                        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);

                        return true;
                    }
                }
                return false;
            }
        });*/

    }

    private class ShareTypeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position == 0) {
                av_email.requestFocus();
            }else {
                TagsTIL.requestFocus();
            }

            tv_spinner_mode.setText(Spinner_ShareAsType.getSelectedItem().toString());

            ShareAsPos = position;
            if (ShareAsPos == 1) {
                av_email.setVisibility(view.GONE);
                m_linear_feelingwith.setVisibility(view.GONE);
                TagsTIL.setVisibility(view.VISIBLE);
                return;
            }
            if (ShareAsPos == 2) {
                TagsTIL.setVisibility(view.VISIBLE);
                av_email.setVisibility(view.VISIBLE);
                m_linear_feelingwith.setVisibility(view.VISIBLE);
                return;
            }
            if (ShareAsPos == 0) {
                av_email.setVisibility(view.VISIBLE);
                m_linear_feelingwith.setVisibility(view.VISIBLE);
                TagsTIL.setVisibility(view.GONE);
                return;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "", name = "";
                    try {
                        Uri result = data.getData();

                        Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());

                        ContentResolver cr = getApplicationContext().getContentResolver();

                        String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.PHOTO_ID,
                                ContactsContract.CommonDataKinds.Email.DATA,
                                ContactsContract.CommonDataKinds.Photo.CONTACT_ID};

                        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
                        cursor = cr.query(result, PROJECTION, filter, null, null);

                        int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            name = cursor.getString(nameId);
                            Log.v(DEBUG_TAG, "Got email: " + email);
                        } else {
                            Log.w(DEBUG_TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, "Failed to get email data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        av_email.setText(email);
                        if (email.length() == 0 && name.length() == 0) {
                            Toast.makeText(this, "No Email for Selected Contact", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;


            }

        } else {
            Log.w(DEBUG_TAG, "Warning: activity result not ok");
        }

    }

    private Uri getOutputMediaFileUri(int type, String FileName) {

        return Uri.fromFile(getOutputMediaFile(type, FileName));
    }

    private File getOutputMediaFile(int type, String FileName) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppName);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + FileName);

        } else if (type == MEDIA_TYPE_IMAGE) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + FileName);

        } else {
            return null;
        }

        return mediaFile;
    }


    public String CreateVideoFolder(int type, String VideoNameMp4) {
        fileUri = getOutputMediaFileUri(type, VideoNameMp4);
        ToSaveURI = fileUri.getPath();
        Log.v("fileUri", "fileUri " + fileUri + " ToSaveURI " + ToSaveURI);
        return ToSaveURI;
    }

    public String TagResizer(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }




    class RecordListener implements View.OnClickListener {

        boolean isRecording = false;

        @Override
        public void onClick(View v) {

            isRecording = !isRecording;
            if(isRecording)
            {
                timer.start();
                Log.i(LOG_TAG, "Start recording...");
                //recordFilename = ImageUtil.getPath() + "/rec_" + System.currentTimeMillis() + ".mp4";
//
                mCameraView.startRecording(ToSaveURI, new CameraRecordGLSurfaceView.StartRecordingCallback() {
                    @Override
                    public void startRecordingOver(boolean success) {
                        if (success) {

                           /* Drawable tempImage = getResources().getDrawable(R.drawable.ic_mic);
                            Bitmap bm = ((BitmapDrawable)tempImage).getBitmap();
                            recordBtn.setImageBitmap(bm);*/

                           // showText("Start recording OK");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recordBtn.setBackgroundResource(0);
                                    recordBtn.setBackgroundResource(R.drawable.circle_with_stop);
//stuff that updates ui

                                }
                            });


                        } else {
                           // showText("Start recording failed");
                        }
                    }
                });
            }
            else {
                if (timer != null) {
                    timer.cancel();
                }
                stopRecording();
            }
        }
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        if (SendDiscardDialog.isShowing())
            SendDiscardDialog.dismiss();
    }*/

    @Override
    protected void onDestroy() {

        super.onDestroy();

        stopRecording();

    }



  /*  public void ShowSendOrDiscardDialog() {

                SendDiscardDialog.show();
                 SendDiscardDialog.setCancelable(false);
                SdDiscard = (Button) SendDiscardDialog.findViewById(R.id.sd_discard);
                SdSend = (Button) SendDiscardDialog.findViewById(R.id.sd_send);

                SdSend.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  if (SendDiscardDialog.isShowing())
                                                      SendDiscardDialog.dismiss();
                                          *//*Uri RecordedFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + AppName + "/" + FileNameWithMimeType);
                                          Log.v("", "RecordedFileUri " + RecordedFileUri);
                                          if (GoingToRecordVideo) {
                                              VideoUploadToServer(RecordedFileUri);
                                          } else {
                                              AudioUploadToServer(RecordedFileUri);
                                          }*//*
                                                  Uri RecordedFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + AppName + "/" + FileNameWithMimeType);
                                                  Log.v("", "RecordedFileUri " + RecordedFileUri);
                                                  VideoUploadToServer(RecordedFileUri);
                                                  finish();
                                              }
                                          }
                );

                SdDiscard.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     if (SendDiscardDialog.isShowing())
                                                         SendDiscardDialog.dismiss();
                                                     DeleteUploadedFile(fileUri);
                                                     finish();
                                                 }
                                             }
                );




    }*/


    private void stopRecording() {
        //showText("Recorded as: " + recordFilename);
        Log.i(LOG_TAG, "End recording...");
        mCameraView.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        mCameraView.endRecording(new CameraRecordGLSurfaceView.EndRecordingCallback() {
            @Override
            public void endRecordingOK() {
                Log.i(LOG_TAG, "End recording OK");

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      //ShowSendOrDiscardDialog();
                                      linear_discard.setVisibility(View.VISIBLE);
                                      Linear_cameracapture.setVisibility(View.GONE);
                                      Hs_filter.setVisibility(View.GONE);
                                  }
                              });

                /*ShowSendOrDiscardDialog();
                FileUtil.saveTextContent(ToSaveURI, lastVideoPathFileName);*/

            }
        });

        /*Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();*/
    }

    private boolean checkPermission(String permission) {

        int result = ContextCompat.checkSelfPermission(_activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            mCameraView.setEnabled(true);
            mCameraView.cameraInstance().isPreviewing();



            return true;
        } else {

            return false;
        }
    }




    @Override
    public void onPause() {
        super.onPause();
        CameraInstance.getInstance().stopCamera();
        mCameraView.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
        CameraInstance.getInstance().stopCamera();

        mCameraView.endRecording();
    }

    @Override
    public void onResume() {
        super.onResume();

        mCameraView.onResume();
    }



    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            timerValue.setText("00:00");
            stopRecording();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );
            System.out.println(hms);
            timerValue.setText(hms);
            if (hms.equals("00:05")) {
                // load the animation
                timerValue.startAnimation(animBlink);
            }
        }
    }



    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.customFilterBtn1){
   /*         customFilter1.setBackgroundColor(getResources().getColor(R.color.primary));
            customFilter1.setTextColor(Color.WHITE);
            customFilter2.setTextColor(Color.BLACK);
            customFilter3.setTextColor(Color.BLACK);
            customFilter4.setTextColor(Color.BLACK);
            customFilter4.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter2.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter3.setBackgroundColor(getResources().getColor(R.color.white));*/
            int Filter1=1;
            Filter1 %= CGENativeLibrary.cgeGetCustomFilterNum();
            mCameraView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    long customFilter = CGENativeLibrary.cgeCreateCustomNativeFilter(1, 1.0f);
                    mCameraView.getRecorder().setNativeFilter(customFilter);
                }
            });
        }else if(view.getId() == R.id.customFilterBtn2){
           /* customFilter2.setBackgroundColor(getResources().getColor(R.color.primary));
            customFilter2.setTextColor(Color.WHITE);
            customFilter1.setTextColor(Color.BLACK);
            customFilter3.setTextColor(Color.BLACK);
            customFilter4.setTextColor(Color.BLACK);
            customFilter1.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter4.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter3.setBackgroundColor(getResources().getColor(R.color.white));*/
            int Filter2=2;
            Filter2 %= CGENativeLibrary.cgeGetCustomFilterNum();
            mCameraView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    long customFilter = CGENativeLibrary.cgeCreateCustomNativeFilter(2, 1.0f);
                    mCameraView.getRecorder().setNativeFilter(customFilter);
                }
            });
        }else if(view.getId() == R.id.customFilterBtn3){

            mCameraView.setFilterWithConfig(StatiConstants.effectConfigs[0]);
            int Filter3=3;
           /* customFilter3.setBackgroundColor(getResources().getColor(R.color.primary));
            customFilter3.setTextColor(Color.WHITE);
            customFilter2.setTextColor(Color.BLACK);
            customFilter4.setTextColor(Color.BLACK);
            customFilter1.setTextColor(Color.BLACK);
            customFilter1.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter2.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter4.setBackgroundColor(getResources().getColor(R.color.white));*/



          /*  Filter3 %= CGENativeLibrary.cgeGetCustomFilterNum();
            mCameraView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    long customFilter = CGENativeLibrary.cgeCreateCustomNativeFilter(3, 1.0f);
                    mCameraView.getRecorder().setNativeFilter(customFilter);
                }
            });*/
        }else if(view.getId() == R.id.customFilterBtn4){
           /* customFilter4.setBackgroundColor(getResources().getColor(R.color.primary));
            customFilter4.setTextColor(Color.WHITE);
            customFilter2.setTextColor(Color.BLACK);
            customFilter3.setTextColor(Color.BLACK);
            customFilter1.setTextColor(Color.BLACK);
            customFilter1.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter2.setBackgroundColor(getResources().getColor(R.color.white));
            customFilter3.setBackgroundColor(getResources().getColor(R.color.white));*/
            int Filter4=4;
            Filter4 %= CGENativeLibrary.cgeGetCustomFilterNum();
            mCameraView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    long customFilter = CGENativeLibrary.cgeCreateCustomNativeFilter(3, 1.0f);
                    mCameraView.getRecorder().setNativeFilter(customFilter);
                }
            });
        }

    }
public  ArrayList<String>mtd_conts() {
    HashSet<String> hashSet = new HashSet<String>();

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {

        ContentResolver cer = getContentResolver();

        Cursor cur = cer.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cer.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
              /*  mNameColIdx = cur.getColumnIndex(ContactsContract.Contacts.
                        DISPLAY_NAME_PRIMARY);
                mIdColIdx = cur.getColumnIndex(ContactsContract.Contacts._ID);*/
                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //   Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    //  Log.e("Email", email);
             /*   long contactId = cur.getLong(mIdColIdx);
                String contact_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                        contactId).toString();*/
                    if (email != null) {
                        // names.add(name);

                        emlRecs.add(email);
                    }
                }
                cur1.close();
            }

            //  Toast.makeText(context, ""+Contact.getInstance().email_list, Toast.LENGTH_SHORT).show();
        }

        cur.close();


    } else {
        Cursor cur;
        int mNameColIdx, mIdColIdx;
        ContentResolver cr = this.getContentResolver();
        String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
                filter, null, order);

        mNameColIdx = cur.getColumnIndex(ContactsContract.Contacts.
                DISPLAY_NAME_PRIMARY);
        mIdColIdx = cur.getColumnIndex(ContactsContract.Contacts._ID);

        for (int t = 0; t < cur.getCount(); t++) {
            cur.moveToPosition(t);
            String contactName = cur.getString(mNameColIdx);
            long contactId = cur.getLong(mIdColIdx);
            String email = cur.getString(cur.getColumnIndex
                    (ContactsContract.CommonDataKinds.Email.DATA));
            String contact_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    contactId).toString();

            emlRecs.add(email);
            // Log.e("Emailx", email);
        }

        cur.close();

    }

    hashSet.addAll(emlRecs);
    emlRecs.clear();
    emlRecs.addAll(hashSet);
    return emlRecs;
}


  public class NotificationRetry extends BroadcastReceiver{

      @Override
      public void onReceive(final Context context, Intent intent) {


          SharedPreferences sharedpreferences;
          sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

          String sptoken = sharedpreferences.getString(SharedPrefUtils.SpToken, "");
          String spCOuntry = sharedpreferences.getString(SharedPrefUtils.SpCountry, "");
          String spLauguge = sharedpreferences.getString(SharedPrefUtils.SpLanguage, "");


         /* Toast.makeText(context, "dd"+
                 sharedpreferences.getString(SharedPrefUtils.SpEmail, "")+
                 StatiConstants.mapTo+
                 StatiConstants.mapTitile
                 *//*StatiConstants.mapType+
                 spCOuntry+
                spLauguge+
                  Locale.getDefault().getDisplayLanguage())+*//*
                 , Toast.LENGTH_SHORT).show();*/

          if(StatiConstants.mapVideoTyped != null && StatiConstants.mapThumbnailTyped != null && StatiConstants.mapTitile != null
                  && StatiConstants.mapTo != null  && StatiConstants.mapType != null) {



  //      Log.v("Offline upload log", sptoken + spCOuntry + spLauguge);


                MapKey =1;

              RestClient.get(context).UploadVideo(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),
                StatiConstants.mapVideoTyped,
                sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),
                StatiConstants.mapTo,
                StatiConstants.mapTitile,
                "Retry,Upload",
                StatiConstants.mapType,
                sharedpreferences.getString(SharedPrefUtils.SpCountry, "IN"),
                sharedpreferences.getString(SharedPrefUtils.SpLanguage,
                Locale.getDefault().getDisplayLanguage()),
                StatiConstants.mapThumbnailTyped,

                new Callback<SVideoResp>() {
                    @Override
                    public void success(SVideoResp sVideoResp, Response response) {
                        if (sVideoResp.getCode().equals("200")) {


                            Toast.makeText(context, "Xpressed successfully, Swipe to clear data.", Toast.LENGTH_SHORT).show();
                         /*
                            toastCustom = new ToastCustom(NotificationRetry.this);
                            SpannableString ss = new SpannableString(StatiConstants.mapTitile+ " Xpressed to "+ ToEmail);
                            ss.setSpan(new ForegroundColorSpan(Color.RED), 0, StatiConstants.mapTitile.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), 0, StatiConstants.mapTitile.length(), 0);
                            ss.setSpan(new ForegroundColorSpan(Color.GREEN), ss.length()-StatiConstants.mapTo.length(), ss.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), ss.length()-StatiConstants.mapTo.length(), ss.length(), 0);
                            toastCustom.ShowToast("iXprez",ss,2);*/

                        } else if (sVideoResp.getCode().equals("601")) {

                            Toast.makeText(context, "601", Toast.LENGTH_SHORT).show();
                        }
                        else if (sVideoResp.getCode().equals("701")) {
                            Toast.makeText(context, "User has blocked you !", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(context, ""+sVideoResp.getCode(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(context, "failed Retry", Toast.LENGTH_SHORT).show();

                    }
                });





          }else {
              Toast.makeText(context, "Empty data lost, send a new video. ", Toast.LENGTH_SHORT).show();
          }
      }
  }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

       finish();


    }
}
