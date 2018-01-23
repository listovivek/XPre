package com.quad.xpress;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.contacts.Contact;
import com.quad.xpress.contacts.DatabaseHandler;
import com.quad.xpress.contacts.FullContactDBOBJ;
import com.quad.xpress.contacts.FullContactsDBhandler;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.privateAcceptReject.PrivARreq;
import com.quad.xpress.models.privateAcceptReject.PrivARresp;
import com.quad.xpress.models.privateBlock.PrivBlockReq;
import com.quad.xpress.models.receivedFiles.EndlessRecyclerOnScrollListener;
import com.quad.xpress.models.receivedFiles.PlayListRecords;
import com.quad.xpress.models.receivedFiles.PlayListResp;
import com.quad.xpress.models.receivedFiles.PlayListitems;
import com.quad.xpress.models.receivedFiles.PrivatePlayListReq;
import com.quad.xpress.models.spam.spam_req;
import com.quad.xpress.models.spam.spamresp;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.StaticConfig;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created  on 20-05-16.
 */
public class PrivatePlayListActivity extends AppCompatActivity implements PrivatePlayListAdapter.OnRecyclerListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Button btn_confirm,btn_discard;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout rv_ll;
    String RefreshTokenMethodName = "";
    ProgressDialog pDialog;
    Context context;
    Activity _activity;
    String AppName;
    Boolean isResumed = false;
    int Index = 0;
    boolean loading;
    String EndOfRecords = "0";
    public static Boolean RvDisabler= false;
    NetConnectionDetector NCD;
    private List<PlayListitems> playlist = new ArrayList<>();
    PlayListitems playlistItems;
    ProgressBar pb_loading;
    TabLayout tabLayout;
    View.OnClickListener  ClickOk;
    ImageButton   audio_Button,video_Button;
    Dialog SendDiscardDialog;
    ImageView iv_nothing_to_show;
    DatabaseHandler dbh;
    ArrayList<String>ixprez_email = new ArrayList<String>();
    ArrayList<String>ixprez_username = new ArrayList<String>();
    ArrayList<String>ixprez_profilepic = new ArrayList<String>();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_videos);
        dbh = new DatabaseHandler(PrivatePlayListActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.RvRcvVideo);
        recyclerView.setHasFixedSize(true);
        context = getApplicationContext();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_private);
        tabLayout.setVisibility(View.GONE);
        iv_nothing_to_show = (ImageView) findViewById(R.id.iv_nothing_to_show);
        pb_loading = (ProgressBar) findViewById(R.id.progressBar_private);

        AppName = getResources().getString(R.string.app_name);
        _activity = this;
        StaticConfig.IsPublicActivity = false;

        NCD = new NetConnectionDetector();
        rv_ll = (LinearLayout) findViewById(R.id.rv_ll);
        layoutManager = new LinearLayoutManager(this);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        recyclerView.setLayoutManager(layoutManager);
        //Toast.makeText(context, ""+ Contact.getInstance().email_list, Toast.LENGTH_SHORT).show();


        pDialog = new ProgressDialog(_activity);
          ImageButton   btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_title.setText("My private xpressions");


        if (NCD.isConnected(context)) {
            getDatas();
            RvDisabler = false;
        } else {
            RvDisabler = true;
            Snackbar.make(findViewById(android.R.id.content), "You Are Offline, Can't view Offline Media!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", ClickOk).setActionTextColor(Color.RED).show();
        }



        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (EndOfRecords.equals("0")) {
                    loading = true;
                    Index++;
                    getDatas();
                }else{loading = false;
                    Index = 0;
                }
            }


        });
        adapter = new PrivatePlayListAdapter(playlist, context, PrivatePlayListActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private void getDatas() {
       // playlist.clear();
        pb_loading.setVisibility(View.VISIBLE);

        RestClient.get(context).MyPrivateLists(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivatePlayListReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), Integer.toString(Index), "30"),
                new Callback<PlayListResp>() {
                    @Override
                    public void success(final PlayListResp arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {

                            ParsePrivateFiles(arg0);
                            iv_nothing_to_show.setVisibility(View.GONE);
                            pb_loading.setVisibility(View.GONE);
                        } else if (arg0.getCode().equals("601")) {
                            RefreshTokenMethodName = "getData";
                            RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            adapter.notifyDataSetChanged();
                          //  Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                            iv_nothing_to_show.setVisibility(View.VISIBLE);
                            pb_loading.setVisibility(View.GONE);

                        }

                        else {
                            Toast.makeText(context, "Error " + arg0.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                      //  errorReporting.SendMail("Xpress Error-ReceiveVideo-getData", error.toString());
                        Toast.makeText(context, "Please check your NW Connection", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void ParsePrivateFiles(PlayListResp arg0) {
        int RLength = arg0.getData().getRecords().length;
        for (int i = 0; i < RLength; i++) {
            PlayListRecords iii = arg0.getData().getRecords()[i];
            try {
                if (iii.getFileuploadFilename().equals("")) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            playlistItems = new PlayListitems(iii.getFileuploadFilename(), iii.getTitle(), iii.getCreated_date(), iii.getFrom_email()
                    , iii.getThumbtokenizedUrl(), iii.getFilemimeType(), iii.getTokenizedUrl(), iii.getFileuploadFilename()
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getStatus(),iii.getUpName(),
                    iii.getPrivacy(),iii.getPhone_number(),iii.getUser_id());
            playlist.add(playlistItems);
        }
        EndOfRecords = arg0.getData().getLast();

        adapter.notifyDataSetChanged();


    }


    @Override
    public void onItemClicked(int position) {

        if(PrivatePlayListActivity.RvDisabler){

        }else {

            playlistItems = playlist.get(position);


            if (NCD.isConnected(context)) {


                String selected_file_path = playlist.get(position).getFileuploadPath();
                String Selected_file_url="";
                if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
                    Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
                }
                else if  (selected_file_path.contains("https")){
                    Selected_file_url = selected_file_path;
                }else {
                    //Local server
                    Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
                }
                //BG image
                String img_url = "";
                String img_thumb = "";
                if(playlist.get(position).getTBPath() != null){
                    img_thumb = playlist.get(position).getTBPath();
                    if (img_thumb.contains(StaticConfig.ROOT_URL_Media)) {
                        img_url = StaticConfig.ROOT_URL + img_thumb.replace(StaticConfig.ROOT_URL_Media, "");
                    }
                    else if  (img_thumb.contains("https")){
                        img_url = img_thumb;
                    }
                    else {
                        //Local server
                        img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

                    }
                }else{

                    img_url = "audio";
                }

                Intent videoIntent = new Intent(PrivatePlayListActivity.this, Actvity_videoPlayback_Private.class);
                videoIntent.putExtra("url",Selected_file_url);
                videoIntent.putExtra("type",playlistItems.getFileMimeType());
                videoIntent.putExtra("likes",playlistItems.getLikesCount());
                videoIntent.putExtra("views",playlistItems.getViewsCount());
                videoIntent.putExtra("file_id",playlistItems.getFileID());
                videoIntent.putExtra("title",playlistItems.getTitle());
                videoIntent.putExtra("tags",playlistItems.getTags());
                videoIntent.putExtra("upload_date",playlistItems.getCreatedDate());
                videoIntent.putExtra("isliked","0");
                videoIntent.putExtra("img_url",img_url);
                videoIntent.putExtra("isPrivate","true");
                videoIntent.putExtra("FromEmail",playlistItems.getFromEmail());
                videoIntent.putExtra("isBoth",playlistItems.getPrivacy());
                videoIntent.putExtra("phonenumber",playlistItems.getPhonenumber());
                videoIntent.putExtra("user_id",playlistItems.getUser_id());

                startActivity(videoIntent);

            } else if (!NCD.isConnected(context)) {
                Toast.makeText(context, "No Internet to download", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onAccept(final int position, String data) {

        playlistItems = playlist.get(position);

        if(playlistItems.getPrivacy().equalsIgnoreCase("both")){

            final Dialog proceedDiscardDialog = new Dialog(PrivatePlayListActivity.this,
                    R.style.Theme_Transparent);
            SpannableString ss = new SpannableString(playlistItems.getFromEmail()+"\n"+" has marked this xpression as public, it will be shown for the World to see!");

            ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, playlistItems.getFromEmail().length(), 0);
            ss.setSpan(new RelativeSizeSpan(1.5f), 0, playlistItems.getFromEmail().length(), 0);
            proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
            TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
            tv_msg.setText( ss);

            btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
            btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);

            btn_confirm.setText("Accept & Make it Public");
            proceedDiscardDialog.show();


            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PrivateFileFeedback(playlistItems.getFileID(), playlistItems.getFileMimeType(), "1", position);
                    proceedDiscardDialog.dismiss();

                }
            });
            btn_discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proceedDiscardDialog.dismiss();
                }
            });


        }
        else {
            PrivateFileFeedback(playlistItems.getFileID(), playlistItems.getFileMimeType(), "1", position);

            final Dialog proceedDiscardDialog = new Dialog(PrivatePlayListActivity.this,
                    R.style.Theme_Transparent);
            String str1 = " It’s your turn now to Xpress! Want to reply right now to ";

            SpannableString ss = new SpannableString(" It’s your turn now to Xpress! Want to reply right now to "+playlistItems.getFromEmail()+" and Xpress yourself back to them? Do it!");

            ss.setSpan(new ForegroundColorSpan(Color.GREEN), str1.length(), str1.length()+playlistItems.getFromEmail().length(), 0);
            ss.setSpan(new RelativeSizeSpan(1.5f), str1.length(), str1.length()+playlistItems.getFromEmail().length(), 0);
            proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
            TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
            tv_msg.setText( ss);

            btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
            btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);

            btn_confirm.setText("Xpress back now ...");
            proceedDiscardDialog.show();


            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    proceedDiscardDialog.dismiss();
                    Reply_alert();



                }
            });
            btn_discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    proceedDiscardDialog.dismiss();
                }
            });




        }



    }

    @Override
    public void onReject(final int position, String data) {

        playlistItems = playlist.get(position);

        final Dialog proceedDiscardDialog = new Dialog(PrivatePlayListActivity.this,
                R.style.Theme_Transparent);
        SpannableString ss = new SpannableString(playlistItems.getFromEmail()+"\n"+" wanted to Xpress this to you immediately. Are you sure you want to reject it?");

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, playlistItems.getFromEmail().length(), 0);
        ss.setSpan(new RelativeSizeSpan(1.5f), 0, playlistItems.getFromEmail().length(), 0);
        proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
        TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
        tv_msg.setText( ss);

        btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
        btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
        proceedDiscardDialog.show();


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PrivateFileFeedback(playlistItems.getFileID(), playlistItems.getFileMimeType(), "0", position);
                proceedDiscardDialog.dismiss();

            }
        });
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedDiscardDialog.dismiss();
            }
        });





    }

    @Override
    public void onBlock(final int position, final String data, Boolean isnewContact, String user_id) {





        playlistItems = playlist.get(position);

        final Dialog proceedDiscardDialog = new Dialog(PrivatePlayListActivity.this,
                R.style.Theme_Transparent);
        SpannableString ss = new SpannableString(playlistItems.getUsername()+"\n"+" can’t Xpress themselves to you anymore. Sure you want to proceed?");

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, playlistItems.getFromEmail().length(), 0);
        ss.setSpan(new RelativeSizeSpan(1.5f), 0, playlistItems.getFromEmail().length(), 0);
        proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
        TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
        tv_msg.setText( ss);
        Button btn_spam,btn_add_contact;
        btn_spam = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_spam);
        btn_add_contact = (Button) proceedDiscardDialog.findViewById(R.id.pvt_add_contact);
        if(isnewContact){
            btn_spam.setVisibility(View.VISIBLE);

            btn_add_contact.setVisibility(View.VISIBLE);
        }


        btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
        btn_confirm.setText("Block");
        btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
        proceedDiscardDialog.show();

        btn_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceedDiscardDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, playlistItems.getUsername());
                //intent.putExtra(ContactsContract.Intents.Insert.PHONE, bean.getMobileNo());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, playlistItems.getFromEmail());

                context.startActivity(intent);

                Contact.getInstance().email_list.contains(playlistItems.from_email);
                Contact.getInstance().email_list.add(Contact.getInstance().email_list.size(),playlistItems.from_email);
                adapter.notifyDataSetChanged();

            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                PrivateBlockUser(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), data,user_id);
                PrivateFileFeedback(playlistItems.getFileID(), playlistItems.getFileMimeType(), "0", position);
                proceedDiscardDialog.dismiss();

            }
        });
        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceedDiscardDialog.dismiss();

            }
        });

        btn_spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ToastCustom tc = new ToastCustom(PrivatePlayListActivity.this);

                String a,b;
                a = "Long press to report ";
                b =" as a spammer .";
                SpannableString ss = new SpannableString(a+data+b);
                ss.setSpan(new ForegroundColorSpan(Color.RED), ss.length()-data.length()-b.length(),ss.length()-b.length() , 0);

                ss.setSpan(new RelativeSizeSpan(1.5f), ss.length()-data.length()-b.length(), ss.length()-b.length(), 0);

                tc.ShowToast("Alert",ss,1);


                 }



        });


        btn_spam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                 AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);


                RestClient.get(context).ReportSpam(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new spam_req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), data ,user_id),
                        new Callback<spamresp>() {
                            @Override
                            public void success(spamresp spamrespx, Response response) {

                                if(spamrespx.getCode().equals("200")){

                                }
                                ToastCustom tc = new ToastCustom(PrivatePlayListActivity.this);
                                String a,b;
                                a = "Your Report has been successfuly registered. We are working on validating your claim to block, ";
                                b =" .";
                                SpannableString ss = new SpannableString(a+data+b);
                                ss.setSpan(new ForegroundColorSpan(Color.RED), ss.length()-data.length()-b.length(),ss.length()-b.length() , 0);

                                ss.setSpan(new RelativeSizeSpan(1.5f), ss.length()-data.length()-b.length(), ss.length()-b.length(), 0);

                                tc.ShowToast("ixprez",ss,1);
                                Vibrator vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
                                vibe.vibrate( 200 );
                                proceedDiscardDialog.dismiss();

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }


                        });


                return true;
            }
        });

    }

    private void PrivateBlockUser(String user_email, String blocked_email, String user_id) {
        RestClient.get(context).PrivateBlockWS(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivBlockReq(user_email, blocked_email,user_id), new Callback<PrivARresp>() {
            @Override
            public void success(final PrivARresp arg0, Response arg1) {
                if (arg0.getCode().equals("200")) {
                    Toast.makeText(context, "Blocked Successfully", Toast.LENGTH_LONG).show();
                } else if (arg0.getCode().equals("601")) {
                    RefreshToken();
                    Toast.makeText(context, "Please try again to block", Toast.LENGTH_LONG).show();
                } else if (arg0.getCode().equals("500")) {
                    Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Please try again to block", Toast.LENGTH_LONG).show();
                }

                playlist.clear();
                getDatas();
            }

            @Override
            public void failure(RetrofitError error) {/*
                LD.DismissTheDialog();
                errorReporting.SendMail("Xpress Error-ReceiveVideo-RefreshToken", error.toString());
                Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();*/
            }
        });
    }






    private void PrivateFileFeedback(String fileID, String FileMimeType, String Feedback, final int position) {
        String FileType = "video";
        if (FileMimeType.equals("audio/mp3")) {
            FileType = "audio";
        }
        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putBoolean(SharedPrefUtils.SpSlideViewusedOnce,false );
        editor.commit();


        RestClient.get(context).PrivateAcceptReject(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivARreq(fileID, FileType, Feedback
                ,sharedpreferences.getString(SharedPrefUtils.SpEmail,"")), new Callback<PrivARresp>() {
            @Override
            public void success(final PrivARresp arg0, Response arg1) {
                if (arg0.getCode().equals("200")) {
                  //  Toast.makeText(context, "Updated your feelings", Toast.LENGTH_LONG).show();
                    Index = 0;
                    playlist.clear();
                    getDatas();
                   // Toast.makeText(context, "Reloading Your Private List.", Toast.LENGTH_LONG).show();

                } else if (arg0.getCode().equals("601")) {
                    RefreshToken();
                    Toast.makeText(context, "Please try again to accept or reject", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Please try again to accept or reject", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {/*
                LD.DismissTheDialog();
                errorReporting.SendMail("Xpress Error-ReceiveVideo-RefreshToken", error.toString());
                Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();*/
            }
        });

    }

    public void Reply_alert() {

        SendDiscardDialog = new Dialog(PrivatePlayListActivity.this,R.style.Theme_Transparent);
        SendDiscardDialog.setContentView(R.layout.send_discard_dialog);
        SendDiscardDialog.show();

        audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
        video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

        audio_Button.setOnClickListener(this);
        video_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (audio_Button == view) {
            Intent i = new Intent(getApplicationContext(), AudioRecordActivity.class);
            i.putExtra("tempEmail", playlistItems.from_email);
            SendDiscardDialog.dismiss();
            startActivity(i);
            finish();

        } else if (video_Button == view) {
            Intent i = new Intent(getApplicationContext(), CameraRecordActivity.class);
            i.putExtra("tempEmail", playlistItems.from_email);
            SendDiscardDialog.dismiss();
            startActivity(i);
            finish();
        }
    }


    public void RefreshToken() {

        RestClient.get(context).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {

                if (arg0.getCode().equals("200")) {
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();
                    if (RefreshTokenMethodName.equals("getData()")) {
                       // Index = 0;
                        getDatas();

                    }
                    // Toast.makeText(context, "TokenRefreshed", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            }

            @Override
            public void failure(RetrofitError error) {


                Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getDatas();
    }

    @Override
    protected void onPause() {
        super.onPause();



    }

    @Override
    protected void onStop() {
        super.onStop();
        isResumed = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
            if( Contact.getInstance().email_list.size()== 0){
                Contact contactData = new Contact();


                contactData.getInstance().ixpressemail.clear();
                contactData.getInstance().ixpressname.clear();
                contactData.getInstance().ixpress_user_pic.clear();
                contactData.getInstance().is_ixpress_user.clear();

                FullContactsDBhandler fDB = new FullContactsDBhandler(getApplicationContext());
                List<FullContactDBOBJ> Dbcontacts = fDB.getAllContacts();

                if(Dbcontacts.size() <= 0 || Contact.getInstance().ixpressemail.size() > Dbcontacts.size()){

                    contactData.getInstance().ixpressemail.clear();
                    contactData.getInstance().ixpressname.clear();
                    contactData.getInstance().ixpress_user_pic.clear();
                    contactData.getInstance().is_ixpress_user.clear();

                    contactData = Contact.getInstance();
                    //Toast.makeText(getActivity(), ""+Contact.getInstance().ixpressemail.size(), Toast.LENGTH_SHORT).show();


                }

                for (FullContactDBOBJ cn : Dbcontacts) {

                    if(cn.get_ixprezuser().equalsIgnoreCase("true")){

                        contactData.getInstance().ixpressemail.add(cn.getPhoneNumber());
                        contactData.getInstance().ixpressname.add(cn.getName());
                        contactData.getInstance().ixpress_user_pic.add(cn.get_profile_pic());
                        contactData.getInstance().is_ixpress_user.add(true);
                    }

                }


                TreeMap<String,String> map = new TreeMap<>();

                for (FullContactDBOBJ cn : Dbcontacts) {
                    if(cn.get_ixprezuser().equalsIgnoreCase("false")){
                        map.put(cn.getName(),cn.getPhoneNumber());
                    }


                }

                contactData.getInstance().ixpressemail.addAll(map.values());
                contactData.getInstance().ixpressname.addAll(map.keySet());

                for (int i = 0; i < map.size(); i++) {
                    contactData.getInstance().ixpress_user_pic.add(String.valueOf(R.drawable.ic_user_icon));
                    contactData.getInstance().is_ixpress_user.add(false);
                }

            }


        if(isResumed){

            Index = 0;
            playlist.clear();

            getDatas();

            isResumed =false;
        }


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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
