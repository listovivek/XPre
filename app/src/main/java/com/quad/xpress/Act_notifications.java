package com.quad.xpress;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.models.AlertStream.AdapterAlertStream;
import com.quad.xpress.models.AlertStream.AlertStreamModelList;
import com.quad.xpress.models.AlertStream.AlertStreamReq;
import com.quad.xpress.models.AlertStream.AlertStreamResp;
import com.quad.xpress.models.Follow.FollowRep;
import com.quad.xpress.models.Follow.FollowReq;
import com.quad.xpress.Utills.helpers.LoadingDialog;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.privateAcceptReject.PrivARreq;
import com.quad.xpress.models.privateAcceptReject.PrivARresp;
import com.quad.xpress.models.privateBlock.PrivBlockReq;
import com.quad.xpress.models.receivedFiles.EndlessRecyclerOnScrollListener;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Act_notifications extends Activity implements AdapterAlertStream.OnRecyclerListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    LoadingDialog LD;
    RelativeLayout rv_ll;
    ProgressDialog pDialog;
    Context context;
    Activity _activity;
    String AppName;
    int Index = 1;
    String PreviousNotificOUNT;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    NetConnectionDetector NCD;
     AlertStreamModelList listDatas;
     List<AlertStreamModelList> list = new ArrayList<>();
    boolean loading;
    String EndOfRecords = "0";
    ImageView iv_nothing_to_show;
    Dialog SendDiscardDialog;
    ImageButton   audio_Button,video_Button;
    Button btn_confirm,btn_discard;
    String from_email;
    ProgressBar pbLoading;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification);

        recyclerView = (RecyclerView) findViewById(R.id.RvRcvVideo);
        context = getApplicationContext();
        AppName = getResources().getString(R.string.app_name);
        _activity = this;
        StaticConfig.IsPublicActivity = true;
        pbLoading = (ProgressBar) findViewById(R.id.progressBar_notification);

        ImageButton btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_title.setText("Notifications");
        iv_nothing_to_show = (ImageView) findViewById(R.id.iv_nothing_to_show);
        NCD = new NetConnectionDetector();
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        LD = new LoadingDialog(Act_notifications.this);
        rv_ll = (RelativeLayout) findViewById(R.id.rv_ll);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(layoutManager);
        Intent getCount  = getIntent();
        PreviousNotificOUNT = getCount.getStringExtra("notificationCount");
        adapter = new AdapterAlertStream((list),context,Act_notifications.this);
        recyclerView.setAdapter(adapter);



        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pDialog = new ProgressDialog(_activity);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!loading && EndOfRecords.equals("0")) {

                    loading = true;
                    getData();
                }
                loading = false;
            }


        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    /*String selected_file_path = list.get(position).getFileuploadPath();
                    String Selected_file_url="";
                    if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
                        Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        //Local server
                        Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
                    }
                    //BG image
                    String img_url = "";
                    String img_thumb = "";
                    if(list.get(position).getTBPath() != null){
                        img_thumb = list.get(position).getTBPath();
                        if (img_thumb.contains(StaticConfig.ROOT_URL_Media)) {
                            img_url = StaticConfig.ROOT_URL + img_thumb.replace(StaticConfig.ROOT_URL_Media, "");
                        } else {
                            //Local server
                            img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

                        }
                    }else{
                        Uri path = Uri.parse("android.resource://com.quad.xpress/" + R.drawable.ic_mic_placeholder);

                        img_url = path.toString();
                    }



                    Intent videoIntent = new Intent(Act_notifications.this,Actvity_video.class);
                    videoIntent.putExtra("url",Selected_file_url);
                    videoIntent.putExtra("type",list.get(position).getFileMimeType());
                    videoIntent.putExtra("likes",list.get(position).getLikesCount());
                    videoIntent.putExtra("views",list.get(position).getViewsCount());
                    videoIntent.putExtra("file_id",list.get(position).getFileID());
                    videoIntent.putExtra("title",list.get(position).getTitle());
                    videoIntent.putExtra("tags",list.get(position).getTags());
                    videoIntent.putExtra("upload_date",list.get(position).getCreatedDate());
                    videoIntent.putExtra("isliked",list.get(position).getIsUserLiked());
                    videoIntent.putExtra("img_url",img_url);
                    if(list.get(position).getPrivacy().equals("Private")){
                        videoIntent.putExtra("isPrivate","true");
                    }else {
                        videoIntent.putExtra("isPrivate","false");
                    }

                    startActivity(videoIntent);
*/


                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



    }




    public void getData() {
    pbLoading.setVisibility(View.VISIBLE);


        RestClient.get(context).AlertStream( new AlertStreamReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Integer.toString(Index),"10",PreviousNotificOUNT),
                new Callback<AlertStreamResp>() {


                    @Override
                    public void success(AlertStreamResp alertStreamResp, Response response) {

                        if(alertStreamResp.getCode().equalsIgnoreCase("200") && alertStreamResp.getData().getLast().equals("0") ){

                            for (int i =0 ; i< alertStreamResp.getData().getRecords().length; i++){
                                listDatas = new AlertStreamModelList(
                                        alertStreamResp.getData().getRecords()[i].getFileuploadFilename(),
                                        alertStreamResp.getData().getRecords()[i].getTitle(),
                                        alertStreamResp.getData().getRecords()[i].getCreated_date(),
                                        alertStreamResp.getData().getRecords()[i].getFrom_email(),
                                        alertStreamResp.getData().getRecords()[i].getThumbnailPath(),
                                        alertStreamResp.getData().getRecords()[i].getFilemimeType(),
                                        alertStreamResp.getData().getRecords()[i].getFileuploadPath(),
                                        alertStreamResp.getData().getRecords()[i].getFileuploadFilename(),
                                        alertStreamResp.getData().getRecords()[i].get_id(),
                                        alertStreamResp.getData().getRecords()[i].getTags(),
                                        alertStreamResp.getData().getRecords()[i].getLikeCount(),
                                        alertStreamResp.getData().getRecords()[i].getView_count(),
                                        alertStreamResp.getData().getRecords()[i].getIsUserLiked(),//is userliked
                                        alertStreamResp.getData().getRecords()[i].getPrivacy(),//
                                        alertStreamResp.getData().getRecords()[i].getFrom_name()
                                );

                            list.add(listDatas);

                            }
                            pbLoading.setVisibility(View.GONE);

                            adapter.notifyDataSetChanged();
                            iv_nothing_to_show.setVisibility(View.GONE);
                                Index++;
                                EndOfRecords = "0";


                        } else if(alertStreamResp.getData().getLast().equals("1")){
                            Index = 1;
                            EndOfRecords = "1";
                            pbLoading.setVisibility(View.GONE);

                        }
                        else{

                            pbLoading.setVisibility(View.GONE);
                            iv_nothing_to_show.setVisibility(View.VISIBLE);

                        }

                      //  Index++;

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pbLoading.setVisibility(View.GONE);
                        iv_nothing_to_show.setVisibility(View.VISIBLE);
                    }


                });



                }


    @Override
    protected void onResume() {
        super.onResume();
        if (NCD.isConnected(context)) {
            getData();
        } else {
            Toast.makeText(context,"Your are Offline",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    public void onItemClicked(int position, String data) {


        String selected_file_path = list.get(position).getFileuploadPath();
        String Selected_file_url="";
        if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
            Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
        } else {
            //Local server
            Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
        }
        //BG image
        String img_url = "";
        String img_thumb = "";
        if(list.get(position).getTBPath() != null){
            img_thumb = list.get(position).getTBPath();
            if (img_thumb.contains(StaticConfig.ROOT_URL_Media)) {
                img_url = StaticConfig.ROOT_URL + img_thumb.replace(StaticConfig.ROOT_URL_Media, "");
            } else {
                //Local server
                img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

            }
        }else{

            img_url = "audio";
        }



        Intent videoIntent = new Intent(Act_notifications.this,Actvity_video.class);
        videoIntent.putExtra("url",Selected_file_url);
        videoIntent.putExtra("type",list.get(position).getFileMimeType());
        videoIntent.putExtra("likes",list.get(position).getLikesCount());
        videoIntent.putExtra("views",list.get(position).getViewsCount());
        videoIntent.putExtra("file_id",list.get(position).getFileID());
        videoIntent.putExtra("title",list.get(position).getTitle());
        videoIntent.putExtra("tags",list.get(position).getTags());
        videoIntent.putExtra("upload_date",list.get(position).getCreatedDate());
        videoIntent.putExtra("isliked",list.get(position).getIsUserLiked());
        videoIntent.putExtra("img_url",img_url);
        if(list.get(position).getPrivacy().equals("Private")){
            videoIntent.putExtra("isPrivate","true");
        }else {
            videoIntent.putExtra("isPrivate","false");
        }

        startActivity(videoIntent);

    }

    @Override
    public void onUnfollow(final int position, String data) {


        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putBoolean(SharedPrefUtils.SpSlideViewusedOnce,false );
        editor.commit();
        RestClient.get(context).UnFollowRequest(new FollowReq(list.get(position).getFromEmail(),sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {

                        Toast.makeText(context, "UnSubscribed from "+list.get(position).getFromEmail()+" posts...", Toast.LENGTH_SHORT).show();
                        list.clear();
                        Index =1;



                        getData();

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    @Override
    public void onAccept(final int position, String data) {


        from_email = list.get(position).getFromEmail();


        if(list.get(position).getPrivacy().equalsIgnoreCase("both")){

            final Dialog proceedDiscardDialog = new Dialog(Act_notifications.this,
                    R.style.Theme_Transparent);
            SpannableString ss = new SpannableString(list.get(position).getFromEmail()+"\n"+" has marked this xpression as public, it will be shown for the World to see!");

            ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, list.get(position).getFromEmail().length(), 0);
            ss.setSpan(new RelativeSizeSpan(1.5f), 0, list.get(position).getFromEmail().length(), 0);
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


                    PrivateFileFeedback(list.get(position).getFileID(), list.get(position).getFileMimeType(), "1");
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
            PrivateFileFeedback(list.get(position).getFileID(), list.get(position).getFileMimeType(), "1");

            final Dialog proceedDiscardDialog = new Dialog(Act_notifications.this,
                    R.style.Theme_Transparent);
            String str1 = " It’s your turn now to Xpress! Want to reply right now to ";

            SpannableString ss = new SpannableString(" It’s your turn now to Xpress! Want to reply right now to "+list.get(position).getFromEmail()+" and Xpress yourself back to them? Do it!");

            ss.setSpan(new ForegroundColorSpan(Color.GREEN), str1.length(), str1.length()+list.get(position).getFromEmail().length(), 0);
            ss.setSpan(new RelativeSizeSpan(1.5f), str1.length(), str1.length()+list.get(position).getFromEmail().length(), 0);
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



        final Dialog proceedDiscardDialog = new Dialog(Act_notifications.this,
                R.style.Theme_Transparent);
        SpannableString ss = new SpannableString(list.get(position).getFromEmail()+"\n"+" wanted to Xpress this to you badly. Are you sure you want to reject it?");

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, list.get(position).getFromEmail().length(), 0);
        ss.setSpan(new RelativeSizeSpan(1.5f), 0, list.get(position).getFromEmail().length(), 0);
        proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
        TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
        tv_msg.setText( ss);

        btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
        btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
        proceedDiscardDialog.show();


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PrivateFileFeedback(list.get(position).getFileID(), list.get(position).getFileMimeType(), "0");
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
    public void onBlock(final int position, final String data) {

        final Dialog proceedDiscardDialog = new Dialog(Act_notifications.this,
                R.style.Theme_Transparent);
        SpannableString ss = new SpannableString(list.get(position).getFromEmail()+"\n"+" can’t Xpress themselves to you anymore. Sure you want to proceed?");

        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, list.get(position).getFromEmail().length(), 0);
        ss.setSpan(new RelativeSizeSpan(1.5f), 0, list.get(position).getFromEmail().length(), 0);
        proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
        TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
        tv_msg.setText( ss);

        btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
        btn_confirm.setText("Reject & Block");
        btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
        proceedDiscardDialog.show();


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PrivateBlockUser(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), data);
                PrivateBlockUser((sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),list.get(position).getFromEmail());
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


    public void Reply_alert() {

        SendDiscardDialog = new Dialog(Act_notifications.this,R.style.Theme_Transparent);
        SendDiscardDialog.setContentView(R.layout.send_discard_dialog);
        SendDiscardDialog.show();

        audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
        video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

        audio_Button.setOnClickListener(this);
        video_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

       // Toast.makeText(Act_notifications.this, ""+from_email, Toast.LENGTH_SHORT).show();

        if (audio_Button == view) {


            Intent i = new Intent(getApplicationContext(), AudioRecordActivity.class);
            i.putExtra("tempEmail", from_email);
            SendDiscardDialog.dismiss();
            startActivity(i);
            finish();

        } else if (video_Button == view) {
            Intent i = new Intent(getApplicationContext(), CameraRecordActivity.class);
            i.putExtra("tempEmail",from_email);
            SendDiscardDialog.dismiss();
            startActivity(i);
            finish();
        }
    }


    private void PrivateBlockUser(String user_email, String blocked_email) {
        RestClient.get(context).PrivateBlockWS(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivBlockReq(user_email, blocked_email), new Callback<PrivARresp>() {
            @Override
            public void success(final PrivARresp arg0, Response arg1) {
                if (arg0.getCode().equals("200")) {
                    Toast.makeText(context, "Blocked Successfully", Toast.LENGTH_LONG).show();
                } else if (arg0.getCode().equals("601")) {

                    Toast.makeText(context, "Please try again to block", Toast.LENGTH_LONG).show();
                } else if (arg0.getCode().equals("500")) {
                    Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Please try again to block", Toast.LENGTH_LONG).show();
                }

                list.clear();
                Index= 1;
                getData();
            }

            @Override
            public void failure(RetrofitError error) {/*
                LD.DismissTheDialog();
                errorReporting.SendMail("Xpress Error-ReceiveVideo-RefreshToken", error.toString());
                Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    private void PrivateFileFeedback(String fileID, String FileMimeType, String Feedback) {
        String FileType = "video";
        if (FileMimeType.equals("audio/mp3")) {
            FileType = "audio";
        }
        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putBoolean(SharedPrefUtils.SpSlideViewusedOnce,false );
        editor.commit();

        RestClient.get(context).PrivateAcceptReject(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivARreq(fileID, FileType, Feedback), new Callback<PrivARresp>() {
            @Override
            public void success(final PrivARresp arg0, Response arg1) {
                if (arg0.getCode().equals("200")) {
                    Toast.makeText(context, "Updated your feelings", Toast.LENGTH_LONG).show();
                    Index = 1;
                    list.clear();

                    getData();
                } else if (arg0.getCode().equals("601")) {

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







}
