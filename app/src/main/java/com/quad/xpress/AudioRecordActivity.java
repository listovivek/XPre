package com.quad.xpress;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quad.xpress.Contacts.Contact;
import com.quad.xpress.Contacts.ContactMainActivity;
import com.quad.xpress.Contacts.DatabaseHandler;
import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.Utills.StatiConstants;
import com.quad.xpress.Utills.helpers.FieldsValidator;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.PermissionStrings;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.localNotification.LocalNotify;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.models.send.SVideoResp;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by ctl on 25/1/17.
 */

public class AudioRecordActivity extends Activity {

    Dialog SendDiscardDialog, AVDialog;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri, tmpUri;
    String ToSaveURI;
    public static final int MEDIA_TYPE_IMAGE = 33;
    ImageButton StartRecord;
    Boolean isAudioRecording;
    AudioRecordCountDown ARTimer;
    NetConnectionDetector CD;
    MediaRecorder mAudioRecorder;
    int ShareAsPos = 0;
    ArrayAdapter AVTagsListAdapter;
    Button SdDiscard, SdSend;
    String RefreshTokenMethodName = "";
    String AppName;
    TextView tv_spinner_mode, recordinvisible;
    ToastCustom toastCustom;
    RelativeLayout m_linear_feelingwith;
    ArrayList<String> emlRecs = new ArrayList<String>();
    String ShareAsText = "Private";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public Animation animBlink;
    public TextView AudioTimerValue,tv_PvtCount, text_hold_back, text_xp_it;
    public SeekBar AudioSeekBar;
    private RecorderVisualizerView visualizerView, visualizerView_avdiaoluge;
    LocalNotify localNotify;
    private static final int AUDIO_PERMISSION_REQUEST_CODE = 91;
    private static final int AUDIO_PERMISSION_REQUEST_CANCEL = 93;
    private static final int CONTACT_READ_REQUEST_CODE = 23;
    Spinner ShareAsType;
    MultiAutoCompleteTextView MATTags;
    LinearLayout TagsTIL;
    String ToEmail = "";
    String AVTitle = "";
    String Tagsa = "";
    TextView tv_AD_title;
    File audiofile;
    CircleImageView Civ_audio;
    AutoCompleteTextView av_email;
    public static String FileNameWithMimeType;
    int LOCAL_NOTIFY_STATIC_ID = 20;
    private Handler handler = new Handler();
    public static final int REPEAT_INTERVAL = 40;
    Context _context;
    PulsatorLayout pulsator,pulsator_pic;
    Activity _activity;
    String Permission4, permission5;
    DatabaseHandler dBhandler;

    String mTempName, mTempEmail;

    int contactPosition, recentPosition;

    public String TagResizer(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_audio);
        _context = AudioRecordActivity.this;
        _activity= AudioRecordActivity.this;


        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator_pic = (PulsatorLayout) findViewById(R.id.pulsator_around_pic);
        Civ_audio = (CircleImageView) findViewById(R.id.circleImageView_profile_img_audio_recorder);


        Glide.with(_context).load(StatiConstants.user_profilepic_url).dontAnimate().placeholder(R.drawable.ic_user_icon)
                .fitCenter().into(Civ_audio);



        toastCustom = new ToastCustom(this);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        CD = new NetConnectionDetector();
        AppName = getResources().getString(R.string.app_name);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AVDialog = new Dialog(AudioRecordActivity.this, R.style.AVdialouge);

        AVDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        AVDialog.setContentView(R.layout.audio_details_dialouge);

        tv_spinner_mode= (TextView) AVDialog.findViewById(R.id.tv_avd_spinner_holder);
        tv_spinner_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareAsType.performClick();
            }
        });

        Permission4 = PermissionStrings.WRITE_EXTERNAL_STORAGE;
        permission5 = PermissionStrings.RECORD_AUDIO;

        PulsatorLayout pulsator_dialouge = (PulsatorLayout) AVDialog.findViewById(R.id.pulsator);
        pulsator_dialouge.start();
        ImageButton btn_AVD_back = (ImageButton) AVDialog.findViewById(R.id.tb_normal_back);









        ImageButton btn_help = (ImageButton) AVDialog.findViewById(R.id.btn_avd_help);
        ImageButton btnContacts = (ImageButton) AVDialog.findViewById(R.id.btn_contacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AudioRecordActivity.this, ContactMainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(AudioRecordActivity.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Share As ?");
                builderSingle.setMessage(R.string.help);

                builderSingle.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ShareAsType.performClick();
                        dialog.dismiss();
                    }
                });


                builderSingle.show();

            }
        });



        TextView tv_AVD_title = (TextView) AVDialog.findViewById(R.id.tb_normal_title);

        tv_AVD_title.setText("Voice your thoughts ...");

        btn_AVD_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton btn_AD_back = (ImageButton) findViewById(R.id.tb_normal_back);

        SdDiscard = (Button) findViewById(R.id.sd_discard);
        SdSend = (Button) findViewById(R.id.sd_send);

        SdSend.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          /*if (SendDiscardDialog.isShowing())
                                              SendDiscardDialog.dismiss();*/
                                          Uri RecordedFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + AppName + "/" + FileNameWithMimeType);
                                          Log.v("", "RecordedFileUri " + RecordedFileUri);

                                          AudioUploadToServer(RecordedFileUri);
                                          finish();

                                      }
                                  }
        );

        SdDiscard.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             /*if (SendDiscardDialog.isShowing())
                                                 SendDiscardDialog.dismiss();*/
                                             SdDiscard.setVisibility(View.INVISIBLE);
                                             SdSend.setVisibility(View.INVISIBLE);
                                             visualizerView.clear();
                                             visualizerView.invalidate();
                                             StartRecord.setBackgroundResource(R.drawable.circle_with_mic);

                                             AVDialog.show();
                                             DeleteUploadedFile(fileUri);


                                         }
                                     }
        );

        m_linear_feelingwith = (RelativeLayout) AVDialog.findViewById(R.id.ll_share_target_parent);
        tv_AD_title = (TextView) findViewById(R.id.tb_normal_title);

        btn_AD_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        av_email = (AutoCompleteTextView) AVDialog.findViewById(R.id.av_email);

        /*String emailID = null;
        try {

            if(ContactMainActivity.finalEmail != null){
                av_email.setText(ContactMainActivity.finalEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
           // Contact.getInstance().ixpressemail.addAll(DatabaseHandler.dbEmailList);


            recentPosition = getIntent().getExtras().getInt("ixpresspositionEmailID");
            contactPosition = getIntent().getExtras().getInt("positionEmailID");
            if(recentPosition == 1){
                if(DatabaseHandler.dbEmailList.get(contactPosition) != null && contactPosition != -1){
                    av_email.setText(DatabaseHandler.dbEmailList.get(contactPosition));
                }
            }else{

                mTempEmail = getIntent().getExtras().getString("tempEmail");
                mTempName = getIntent().getExtras().getString("tempName");

                Log.d("name", mTempEmail + mTempName);
                if(Contact.getInstance().ixpressemail.get(contactPosition) !=
                        null && contactPosition != -1){
                    av_email.setText(mTempEmail);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);

        ARTimer = new AudioRecordCountDown(40000, 1000);
        AudioTimerValue = (TextView) findViewById(R.id.txttime);
        text_hold_back = (TextView) findViewById(R.id.texthold_back);
        text_xp_it = (TextView) findViewById(R.id.textxp_it);
        StartRecord = (ImageButton) findViewById(R.id.startrecord);
        //AudioSeekBar = (SeekBar) findViewById(R.id.audioSeekbar);
        visualizerView = (RecorderVisualizerView)findViewById(R.id.visualizer);

       // recordinvisible = (TextView) findViewById(R.id.record_invisible);
      //  Log.v("Dash GCM", "Id " + sharedpreferences.getString(SharedPrefUtils.SpGcmToken, "Welcome"));



        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        localNotify = new LocalNotify(getApplicationContext(), mBuilder, _activity);


        AlphaAnimation  blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);

        AVDetailsDialog(".mp3");

    }


    private void AVDetailsDialog(final String fileMimeType) {

        AVDialog.show();
        ShareAsType = (Spinner) AVDialog.findViewById(R.id.av_share_type);
        TagsTIL = (LinearLayout) AVDialog.findViewById(R.id.av_tags_til);
        MATTags = (MultiAutoCompleteTextView) AVDialog.findViewById(R.id.av_tags);
        String Tags[] = StatiConstants.TagList.split(",");
        AVTagsListAdapter = new ArrayAdapter(this, R.layout.spinner_autofill_av_dialouge, Tags);
        MATTags.setAdapter(AVTagsListAdapter);
        MATTags.setThreshold(1);
        MATTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        final ImageButton btn_start_recording = (ImageButton) AVDialog.findViewById(R.id.btn_start_recording);

        final EditText av_name = (EditText) AVDialog.findViewById(R.id.av_name);

        AutoSuggestAdapter adapter = null;
        try {
            adapter = new AutoSuggestAdapter(this,
                    R.layout.spinner_autofill_av_dialouge, getNameEmailDetails());
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

                if(keyCode == EditorInfo.IME_ACTION_NEXT) {
                    av_email.requestFocus();
                }

                return false;
            }
        });

        av_name.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_GO || event.getKeyCode() ==
                        KeyEvent.KEYCODE_ENTER) {

                    btn_start_recording.performClick();

                }
                return false;
            }
        });

        ArrayAdapter<CharSequence> adapter_Type = ArrayAdapter.createFromResource(this,
                R.array.share_type, R.layout.spinner_custom_settings);

        adapter_Type.setDropDownViewResource(R.layout.spinner_custom);

        ShareAsType.setAdapter(adapter_Type);
        ShareAsType.setOnItemSelectedListener(new ShareTypeListener());
        av_name.setHint(" Feelings!");

        //btn_start_recording.setText("Proceed");

        AVDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK) {
                    AVDialog.cancel();
                     onBackPressed();
                }

                return false;

            }
        });


        btn_start_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SdDiscard.setVisibility(View.INVISIBLE);
                SdSend.setVisibility(View.INVISIBLE);
                StartRecord.setEnabled(true);
                AudioTimerValue.setText("00:40" );
                //ARTimer = new AudioRecordCountDown(40000, 1000);

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
                tv_AD_title.setText(av_name.getText().toString());
                String AvTitleSpaceRemover = AVTitle.replace(" ", "-");
                FileNameWithMimeType = AvTitleSpaceRemover + fileMimeType;
                Tagsa = TagResizer(MATTags.getText().toString());
                ShareAsText = ShareAsType.getSelectedItem().toString();
                if (AVDialog.isShowing()) {
                    callWeb(ToEmail);

                    /*av_email.setText("");
                    av_name.setText("");
                    MATTags.setText("");*/

                    AVDialog.dismiss();

                    //animate n stuff
                    pulsator.start();

                    text_xp_it.setVisibility(View.VISIBLE);
                    text_hold_back.setVisibility(View.VISIBLE);
                    AudioTimerValue.setVisibility(View.VISIBLE);
                }

                ShowAudioRecordDialog(CreateVideoFolder(MEDIA_TYPE_VIDEO, FileNameWithMimeType));

            }

            private void callWeb(final String toEmail) {

                ArrayList<String> emailList = new ArrayList<String>();
                emailList.add(toEmail);
                RestClient.get(AudioRecordActivity.this).PostContacts(new ContactsReq(emailList),
                        new Callback<ContactsResp>() {
                            @Override
                            public void success(ContactsResp contactsResp, Response response) {

                                if(recentPosition == 0){
                                    if(contactPosition == -1){
                                        for(int i=0; i<Contact.getInstance().ixpressemail.size(); i++){
                                            if(Contact.getInstance().ixpressemail.get(i).equalsIgnoreCase(toEmail)){
                                                dBhandler = new DatabaseHandler(AudioRecordActivity.this);
                                                dBhandler.addContact(Contact.getInstance().ixpressemail.get(i),
                                                        Contact.getInstance().ixpressname.get(i),
                                                        Contact.getInstance().ixpress_user_pic.get(i)
                                                        ,contactPosition);
                                            }
                                        }

                                    }else {
                                        if (contactsResp.getData().length != 0) {

                                            for (int i = 0; i < contactsResp.getData().length; i++) {
                                                String emailID = contactsResp.getData()[i].getEmail_id();
                                                String name = contactsResp.getData()[i].getUser_name();
                                                String pic_url = contactsResp.getData()[i].getProfile_image();

                                                if (emailID != null && name != null && pic_url != null) {
                                                    dBhandler = new DatabaseHandler(AudioRecordActivity.this);
                                                    dBhandler.addContact(emailID, name, pic_url, contactPosition);
                                                }
                                            }
                                        } else {

                                            dBhandler = new DatabaseHandler(AudioRecordActivity.this);
                                            dBhandler.addContact(mTempEmail,
                                                    mTempName,
                                                    Contact.getInstance().ixpress_user_pic.get(contactPosition)
                                                    , contactPosition);
                                        }
                                    }
                                }else{

                                }


                                /*if(contactsResp.getData().length != 0) {


                                    for (int i = 0; i < contactsResp.getData().length; i++) {
                                        String emailID = contactsResp.getData()[i].getEmail_id();
                                        String name = contactsResp.getData()[i].getUser_name();
                                        String pic_url = contactsResp.getData()[i].getProfile_image();

                                        if(emailID != null && name != null && pic_url != null) {
                                            DatabaseHandler handler = new DatabaseHandler(AudioRecordActivity.this);L
                                            handler.addContact(emailID, name, pic_url, contactPosition);
                                        }
                                    }
                                }else{
                                   *//* DatabaseHandler handler = new DatabaseHandler(AudioRecordActivity.this);
                                    handler.addContact(Contact.getInstance().ixpressemail.get(contactPosition),
                                            Contact.getInstance().ixpressname.get(contactPosition),
                                            Contact.getInstance().ixpress_user_pic.get(contactPosition)
                                            , contactPosition);*//*
                                }*/
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("ixpressemail error", "error");
                            }
                        });
            }
        });

        CheckAndRequestPermission();



    }


    private class ShareTypeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position == 0) {
                av_email.requestFocus();
            }else {
                TagsTIL.requestFocus();
            }


            tv_spinner_mode.setText(ShareAsType.getSelectedItem().toString());
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


    public ArrayList<String> getNameEmailDetails() {

        HashSet<String> emlRecsHS = new HashSet<String>();
        Context context = getApplicationContext();
        ContentResolver cr = context.getContentResolver();
        String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
        if (cur.moveToFirst()) {
            do {
                // names comes in hand sometimes
                String name = cur.getString(1);
                String emlAddr = cur.getString(3);

                // keep unique only
                if (emlRecsHS.add(emlAddr.toLowerCase())) {
                    emlRecs.add(emlAddr);

                }
            } while (cur.moveToNext());
        }

        cur.close();
        return emlRecs;
    }


    private boolean checkPermission(String permission) {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else {

            return false;
        }
    }





    public String CreateVideoFolder(int type, String VideoNameMp4) {
        fileUri = getOutputMediaFileUri(type, VideoNameMp4);
        ToSaveURI = fileUri.getPath();
        Log.v("fileUri", "fileUri " + fileUri + " ToSaveURI " + ToSaveURI);
        return ToSaveURI;
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

    public void ShowAudioRecordDialog(final String AudioFileUri) {
        //AudioRecordDialog.show();
        // AudioRecordDialog.setCancelable(false);
        isAudioRecording = false;
        StartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAudioRecording) {
                    //StartRecord.setBackgroundDrawable(null);
                    StartAudioRecording(AudioFileUri);
                    pulsator_pic.start();
                } else {
                    StopAudioRecording();
                    pulsator_pic.stop();
                }

            }
        });
    }

    public void StartAudioRecording(String AudioFileUri) {
        try {
            StartRecord.setBackgroundResource(0);
            StartRecord.setBackgroundResource(R.drawable.circle_with_stop);

        } catch (Exception e) {

        }
        mAudioRecorder = new MediaRecorder();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mAudioRecorder.setOutputFile(AudioFileUri);
        AudioTimerValue.setVisibility(TextView.VISIBLE);
        try {
            mAudioRecorder.prepare();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare failed");
        }
        mAudioRecorder.start();
        isAudioRecording = true;
        ARTimer.start();
        handler.post(updateVisualizer);

    }

    public void StopAudioRecording() {
        // Stop recording and release resource
        //AudioTimerValue.setVisibility(TextView.GONE);
        //AudioSeekBar.setProgress(0);
        if(mAudioRecorder!= null) {

            mAudioRecorder.stop();
            mAudioRecorder.release();
            mAudioRecorder = null;
            StartRecord.setEnabled(false);
            StartRecord.setBackgroundResource(R.drawable.circle_with_crossmic);
            if (ARTimer != null)
                ARTimer.cancel();
            // Change isRecording flag to false
            isAudioRecording = true;
            if (AudioTimerValue.getText().equals("00:40")) {
                AudioTimerValue.setText("Out of time.");
                pulsator_pic.stop();
            } else {
                AudioTimerValue.setText("Done.");
            }

            AudioTimerValue.clearAnimation();
            SdDiscard.setVisibility(View.VISIBLE);
            SdSend.setVisibility(View.VISIBLE);

            handler.removeCallbacks(updateVisualizer);
        }
    }


    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isAudioRecording) // if we are already recording
            {
                // get the current amplitude
                int x = mAudioRecorder.getMaxAmplitude();
                visualizerView.addAmplitude(x); // update the VisualizeView
                visualizerView.invalidate(); // refresh the VisualizerView

                // update in 40 milliseconds
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };


    public class AudioRecordCountDown extends CountDownTimer {

        public AudioRecordCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            AudioTimerValue.setText("00:40");
            StopAudioRecording();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            long hmsvalue = TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
            // String hms = String.format("%02d", hmsvalue);
            int hms = (int) hmsvalue - 0;
            //AudioSeekBar.setProgress(hms);
            AudioTimerValue.setText(String.valueOf("00:"+hms));
            if (hms == 5) {
                AudioTimerValue.startAnimation(animBlink);
            }
        }
    }



    private void AudioUploadToServer(final Uri AudioUri) {
        // LD.ShowTheDialog("Please Wait", "Sending..", false);
        LOCAL_NOTIFY_STATIC_ID = LOCAL_NOTIFY_STATIC_ID + 2;
        localNotify.ShowFileUploadNotification(AVTitle + " audio xpressing", LOCAL_NOTIFY_STATIC_ID);
        TypedFile AudioTypedFile = new TypedFile("audio/mp3", new File(String.valueOf(AudioUri)));
        final String ShareType = ShareAsText;
        //  Log.v("ThumbNail Uri", " " + getThumbNailUri(context, videoUri));
        String sptoken = sharedpreferences.getString(SharedPrefUtils.SpToken, "");
        String spEmail = sharedpreferences.getString(SharedPrefUtils.SpEmail, "");
        String spCountry = sharedpreferences.getString(SharedPrefUtils.SpCountry, "IN");
        String splanguaege = sharedpreferences.getString(SharedPrefUtils.SpLanguage, Locale.getDefault().getDisplayLanguage());

        Log.v("", sptoken+spEmail+spCountry+splanguaege);

        RestClient.get(getApplicationContext()).UploadAudio(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), AudioTypedFile, sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), ToEmail, AVTitle, Tagsa, ShareAsText,
                sharedpreferences.getString(SharedPrefUtils.SpCountry, "IN"), sharedpreferences.getString(SharedPrefUtils.SpLanguage, Locale.getDefault().getDisplayLanguage()), new Callback<SVideoResp>() {
                    @Override
                    public void success(SVideoResp sVideoResp, Response response) {
                        if (sVideoResp.getCode().equals("200")) {

                            localNotify.FinishedUploadNotification(LOCAL_NOTIFY_STATIC_ID, AVTitle + " successfully");


                            SpannableString ss = new SpannableString(AVTitle+ " xpressed to "+ ToEmail);
                            ss.setSpan(new ForegroundColorSpan(Color.RED), 0, AVTitle.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), 0, AVTitle.length(), 0);
                            ss.setSpan(new ForegroundColorSpan(Color.GREEN), ss.length()-ToEmail.length(), ss.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.2f), ss.length()-ToEmail.length(), ss.length(), 0);
                         //   toastCustom.ShowToast("Ixprez",ss,2);

                            File dir = new File(Environment.getExternalStorageDirectory() + "/" + AppName + "/");
                            if (dir.isDirectory())
                            {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++)
                                {
                                    new File(dir, children[i]).delete();

                                }
                            }


                        } else if (sVideoResp.getCode().equals("601")) {
                            RefreshTokenMethodName = "AudioUploadToServer";
                           // RefreshToken(AudioUri);
                            // Toast.makeText(context, "RefreshTokenMethodName ", Toast.LENGTH_LONG).show();
                        }
                        else if (sVideoResp.getCode().equals("701")) {
                            //Toast.makeText(getApplicationContext(), "User has blocked you !", Toast.LENGTH_LONG).show();

                            localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID,  "You have been Blocked by user",_activity,AudioUri);
                            DeleteUploadedFile(AudioUri);

                        }else {
                            localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID, "Upload failed. " + sVideoResp.getCode(),_activity,AudioUri);



                        }

                        // delete
                        if (ShareType.equals("Private")) {

                            DeleteUploadedFile(AudioUri);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        localNotify.UploadNotificationFailed(LOCAL_NOTIFY_STATIC_ID, "Failed to Xpress, try again" + error.toString(),_activity,AudioUri);


                       // DeleteUploadedFile(AudioUri);
                    }
                });
    }

    private void DeleteUploadedFile(Uri UploadedFileUri) {
        if (UploadedFileUri != null) {
            new File(String.valueOf(UploadedFileUri)).delete();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // ActivityCompat.requestPermissions(_activity, new String[]{PermissionStrings.CAMERA}, VIDEO_PERMISSION_REQUEST_CODE);

                } else {
                    ActivityCompat.requestPermissions(_activity,
                            new String[]{PermissionStrings.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
                    //Toast.makeText(_context, "Kindly, give audio permission to record your voice", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.RECORD_AUDIO},
                            AUDIO_PERMISSION_REQUEST_CANCEL);
                    //Intent2Activity();
                } else {
                   /* CheckAndRequestPermission();
                    Toast.makeText(_context, "Kindly, give storage permission to store and" +
                            " access the video and audio", Toast.LENGTH_SHORT).show();*/
                    this.finish();
                  //  CheckAndRequestPermission();

                }
                break;
            case AUDIO_PERMISSION_REQUEST_CANCEL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else{
                    this.finish();
                }

                break;
        }
    }

    public void CheckAndRequestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            if (checkPermission(Permission4) && checkPermission(permission5)) {
               // Intent2Activity();
                return;
            }
            requestPermission(Permission4);
        } else {
            // Intent2Activity();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopAudioRecording();
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, AUDIO_PERMISSION_REQUEST_CODE);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkPermission(PermissionStrings.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(AudioRecordActivity.this, new
                        String[]{PermissionStrings.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
                return;
            }
        }
    }*/
}
