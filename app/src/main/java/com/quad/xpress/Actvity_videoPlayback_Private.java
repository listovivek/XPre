package com.quad.xpress;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.models.privateAcceptReject.PrivARreq;
import com.quad.xpress.models.privateAcceptReject.PrivARresp;
import com.quad.xpress.models.tagList.Tag_cloud_activity_new;
import com.quad.xpress.webservice.RestClient;
import com.squareup.picasso.Picasso;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 11/3/2016.
 */

public class Actvity_videoPlayback_Private extends AppCompatActivity implements View.OnClickListener{
    String url,type,likes,views,file_id,title,ul_date,tags,IsUserLiked,bg_img_url,fromemail,isBoth;
    VideoView mVideoView;
    ProgressBar pb;
    Boolean looping=true,orientation_poitrate= true,vid_orintaion_lanscape= false,IsMyUploads ;
    NetConnectionDetector NDC;
    ImageView iv_bg,iv_audio_bg;
    ImageButton Ib_close,Ib_loop,btn_vv_next,btn_tb_back;
    TextView tv_title,tv_tag,tv_likes,tv_views,tv_comment,tv_date;
    Context _context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout ll_btm_bar,ll_btm;
    int vwidth,vheight;
    RelativeLayout Rl_tb;
    Boolean isPrivate = false;
    int Likes_count  = 0;
    Uri video;
    Dialog SendDiscardDialog;
    Button btn_confirm,btn_discard;
   ImageButton audio_Button,video_Button;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        _context =getApplicationContext();

        Rl_tb = (RelativeLayout) findViewById(R.id.tb);
        btn_vv_next = (ImageButton) findViewById(R.id.btn_vv_next);

     //   Toast.makeText(_context, "Pvt", Toast.LENGTH_SHORT).show();
        btn_vv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                orientation_poitrate = false;

                ll_btm_bar.setVisibility(View.GONE);
                ll_btm.setVisibility(View.GONE);



            }
        });
        btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);

         TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ll_btm_bar = (LinearLayout) findViewById(R.id.ll_vv_data_bar);
        ll_btm = (LinearLayout) findViewById(R.id.ll_vv_btm);
        pb = (ProgressBar) findViewById(R.id.progressBar_video_view);
        Ib_close = (ImageButton) findViewById(R.id.btn_vv_back);
        Ib_loop = (ImageButton) findViewById(R.id.btn_vv_repeat);
        iv_bg = (ImageView) findViewById(R.id.videoView_bg_image);
        iv_audio_bg = (ImageView) findViewById(R.id.videoView_bg_image_audio);
        Intent getVurl = getIntent();
        url = getVurl.getStringExtra("url");
        type = getVurl.getStringExtra("type");
        likes = getVurl.getStringExtra("likes");
        views = getVurl.getStringExtra("views");
        file_id = getVurl.getStringExtra("file_id");
        title   = getVurl.getStringExtra("title");
        tags   = getVurl.getStringExtra("tags");
        ul_date   = getVurl.getStringExtra("upload_date");
        IsUserLiked = getVurl.getStringExtra("isliked");
        bg_img_url = getVurl.getStringExtra("img_url");
        fromemail = getVurl.getStringExtra("FromEmail");
        isBoth = getVurl.getStringExtra("isBoth");

        try {
            IsMyUploads  = getVurl.getBooleanExtra("IsMyUploads",false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String isPvt   = "true";

        try {
            Likes_count = Integer.parseInt(likes);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        try {
            isPvt = getVurl.getStringExtra("isPrivate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isPvt.equals("true")){
                isPrivate=true;
                    ll_btm_bar.setVisibility(View.GONE);

                }


        //set content
        tv_tb_title.setText(title);
        tv_title = (TextView) findViewById(R.id.tv_vv_title);
        tv_tag = (TextView) findViewById(R.id.tv_vv_tag);
        tv_likes = (TextView) findViewById(R.id.tv_vv_likes);
        tv_views = (TextView) findViewById(R.id.tv_vv_views);
        tv_comment = (TextView) findViewById(R.id.tv_vv_emotion);
        tv_date  = (TextView) findViewById(R.id.tv_vv_date);
        if(IsUserLiked.equals("1")){
            tv_likes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_heart_white_liked, 0, 0, 0);
        }else {

        }

        //img bg
      //  Glide.with(_context).load(bg_img_url).centerCrop().into(iv_bg);

            if ( (bg_img_url.equalsIgnoreCase("audio"))|| (bg_img_url.contains("icons/microphone.png")) ){

                iv_audio_bg.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(R.color.translucent_txt_black)
                       .into(iv_bg);
              //  Toast.makeText(_context, "Audio file", Toast.LENGTH_SHORT).show();

            }else {
                Picasso.with(getApplicationContext()).load(bg_img_url).into(iv_bg);

            }



        //set text
        tv_title.setText(title);
        tv_tag.setText(tags);
        tv_views.setText(views);
        tv_likes.setText(" "+likes);
        tv_date.setText(ul_date);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(IsUserLiked.equals("1")){
                    tv_likes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_heart_white, 0, 0, 0);
                    IsUserLiked="0";
                    Likes_count =  Likes_count-1;

                    tv_likes.setText(" "+Likes_count);

                  //  Toast.makeText(getApplicationContext(),"1 -"+Likes_count,Toast.LENGTH_LONG).show();
                }else {
                    tv_likes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_heart_white_liked, 0, 0, 0);
                    IsUserLiked="1";
                    Likes_count =   Likes_count+1;

                    tv_likes.setText(" "+Likes_count);

                  //  Toast.makeText(getApplicationContext(),"0 -"+Likes_count,Toast.LENGTH_LONG).show();
                }

                mtd_like_count();

            }
        });
    tv_comment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent Tag_act = new Intent(_context, Tag_cloud_activity_new.class);

            Tag_act.putExtra("file_id",file_id);
            Tag_act.putExtra("file_type",type);
            startActivity(Tag_act);
        }
    });


        String LINK = url;



        mVideoView  = (VideoView) findViewById(R.id.videoView);
        final MediaController mc = new MediaController(this);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                mVideoView.setMediaController(mc);
                return false;

            }
        });


        mc.setAnchorView(mVideoView);
        mc.setMediaPlayer(mVideoView);

         video = Uri.parse(LINK);


        mVideoView.setVideoURI(video);
       // mVideoView.requestFocus();
      //  mVideoView.seekTo(2000);


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

               mVideoView.start();
                mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


                pb.setVisibility(View.GONE);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        // TODO Auto-generated method stub
                       // Log.e(TAG, "Changed");
                        pb.setVisibility(View.GONE);
                        iv_bg.setVisibility(View.GONE);


                    }
                });



          /*      if(type.equalsIgnoreCase("video/mp4")){
                  //  video_type = "video";
                    mp.setLooping(false);
                }else {
                   // video_type = "audio";
                    mc.show();
                    mp.setLooping(false);
                }*/
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(!IsMyUploads) {
                    mtd_accept_reject();
                }
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                Toast.makeText(_context, "That video is not Playable in your device", Toast.LENGTH_SHORT).show();
                 finish();



                return false;
            }
        });

        Ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Ib_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(looping){Ib_loop.setImageResource(R.drawable.ic_repeat_off);
                looping=false;
                }
                else {Ib_loop.setImageResource(R.drawable.ic_repeat);
                looping=true;
                }

            }
        });


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ll_btm_bar.setVisibility(View.GONE);
            ll_btm.setVisibility(View.GONE);
            Rl_tb.setVisibility(View.GONE);

            if (vwidth> vheight ){

            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            Rl_tb.setVisibility(View.VISIBLE);

            if(isPrivate){
                ll_btm_bar.setVisibility(View.GONE);
            }
        }
    }
    public int getscrOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();

        int orientation = getOrient.getOrientation();

        // Sometimes you may get undefined orientation Value is 0
        // simple logic solves the problem compare the screen
        // X,Y Co-ordinates and determine the Orientation in such cases
        if(orientation== Configuration.ORIENTATION_UNDEFINED){

            Configuration config = getResources().getConfiguration();
            orientation = config.orientation;

            if(orientation==Configuration.ORIENTATION_UNDEFINED){
                //if height and widht of screen are equal then
                // it is square orientation
                if(getOrient.getWidth()==getOrient.getHeight()){
                    orientation = Configuration.ORIENTATION_SQUARE;
                }else{ //if widht is less than height than it is portrait
                    if(getOrient.getWidth() < getOrient.getHeight()){
                        orientation = Configuration.ORIENTATION_PORTRAIT;
                    }else{ // if it is not any of the above it will defineitly be landscape
                        orientation = Configuration.ORIENTATION_LANDSCAPE;
                    }
                }
            }
        }
        return orientation; // return value 1 is portrait and 2 is Landscape Mode
    }

    private void mtd_like_count() {

        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        String Emotion="like";

        RestClient.get(_context).Liked(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Like_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Emotion,IsUserLiked,file_id),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {

                            String Post_status = arg0.getStatus();
                            Log.d("Mes-Like",Post_status);
                            if(Post_status.equals("")){
                                // Toast.makeText(_context, "Success +1", Toast.LENGTH_LONG).show();
                            }else {
                                // Toast.makeText(_context, "Failed ", Toast.LENGTH_LONG).show();
                            }
                            // LD.DismissTheDialog();
                            //  Populate();
                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(_context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(_context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(_context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }
                        //Populate();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                       // LD.DismissTheDialog();

                        Toast.makeText(_context, "Error Raised", Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        ll_btm_bar.setVisibility(View.GONE);
        ll_btm.setVisibility(View.GONE);
        orientation_poitrate=false;
        if(orientation_poitrate){



        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {

     getMenuInflater().inflate(R.menu.menu_video_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {


           /* case R.id.menu_vv_ful_scr:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                ab.hide();
                ll_btm_bar.setVisibility(View.GONE);
                ll_btm.setVisibility(View.GONE);

                orientation_poitrate = true;


            break;*/

            case android.R.id.home:
                  this.finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;
    }

    void mtd_accept_reject() {
        final Dialog Dialouge_accept_reject = new Dialog(Actvity_videoPlayback_Private.this, R.style.AVdialouge);
        Dialouge_accept_reject.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialouge_accept_reject.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Dialouge_accept_reject.setContentView(R.layout.alert_accept_reject);
        TextView tv_accept, tv_reject, tv_acceptNreply, tv_rejectNreply, tv_title_dialouge;

        ImageButton btn_replay, btn_back_dialouge;

        btn_back_dialouge = (ImageButton) Dialouge_accept_reject.findViewById(R.id.tb_normal_back);
        btn_back_dialouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialouge_accept_reject.dismiss();
                finish();
            }
        });

        tv_title_dialouge = (TextView) Dialouge_accept_reject.findViewById(R.id.tb_normal_title);
        tv_title_dialouge.setText(title);


        tv_accept = (TextView) Dialouge_accept_reject.findViewById(R.id.vv_pvt_text_accept);
        tv_reject = (TextView) Dialouge_accept_reject.findViewById(R.id.vv_pvt_text_reject);
        /*tv_acceptNreply = (TextView) Dialouge_accept_reject.findViewById(R.id.vv_pvt_tv_acceptnreply);
        tv_rejectNreply = (TextView) Dialouge_accept_reject.findViewById(R.id.vv_pvt_tv_rejectnreply);*/
        btn_replay = (ImageButton) Dialouge_accept_reject.findViewById(R.id.vv_pvt_replay);


        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVideoView.setVideoURI(video);
                pb.setVisibility(View.VISIBLE);

                Dialouge_accept_reject.dismiss();

            }
        });

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isBoth.equalsIgnoreCase("both")) {

                    final Dialog proceedDiscardDialog = new Dialog(Actvity_videoPlayback_Private.this,
                            R.style.Theme_Transparent);
                    SpannableString ss = new SpannableString(fromemail + "\n" + " has marked this xpression as public, it will be shown for the World to see!");

                    ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, fromemail.length(), 0);
                    ss.setSpan(new RelativeSizeSpan(1.5f), 0, fromemail.length(), 0);
                    proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
                    TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
                    tv_msg.setText(ss);

                    btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
                    btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);

                    btn_confirm.setText("Accept & Make it Public");
                    proceedDiscardDialog.show();


                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PrivateFileFeedback(file_id, type, "1");

                            proceedDiscardDialog.dismiss();

                        }
                    });
                    btn_discard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            proceedDiscardDialog.dismiss();
                        }
                    });


                } else {



                    final Dialog proceedDiscardDialog = new Dialog(Actvity_videoPlayback_Private.this,
                            R.style.Theme_Transparent);
                    String str1 = " It’s your turn now to Xpress! Want to reply right now to ";

                    SpannableString ss = new SpannableString(" It’s your turn now to Xpress! Want to reply right now to " + fromemail + " and Xpress yourself back to them? Do it!");

                    ss.setSpan(new ForegroundColorSpan(Color.GREEN), str1.length(), str1.length() + fromemail.length(), 0);
                    ss.setSpan(new RelativeSizeSpan(1.5f), str1.length(), str1.length() + fromemail.length(), 0);
                    proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
                    TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
                    tv_msg.setText(ss);

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
                            PrivateFileFeedback(file_id, type, "1");
                        }
                    });


                }


            }
        });

        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog proceedDiscardDialog = new Dialog(Actvity_videoPlayback_Private.this,
                        R.style.Theme_Transparent);
                SpannableString ss = new SpannableString(fromemail + "\n" + " wanted to Xpress this to you badly. Are you sure you want to reject it?");

                ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, fromemail.length(), 0);
                ss.setSpan(new RelativeSizeSpan(1.5f), 0, fromemail.length(), 0);
                proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
                TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
                tv_msg.setText(ss);

                btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
                btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
                proceedDiscardDialog.show();


                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        PrivateFileFeedback(file_id, type, "0");
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
        });

       /* tv_acceptNreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateFileFeedback(file_id, type, "1");
                ContactMainActivity.finalEmail = fromemail;
                Intent DasWithmail = new Intent(getApplicationContext(),DashBoard.class);
                startActivity(DasWithmail);

                finish();

            }
        });

        tv_rejectNreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateFileFeedback(file_id, type, "0");
                ContactMainActivity.finalEmail = fromemail;
                Intent DasWithmail = new Intent(getApplicationContext(),DashBoard.class);
                startActivity(DasWithmail);

                finish();

            }
        });*/



        Dialouge_accept_reject.show();

        Dialouge_accept_reject.setOnKeyListener(new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    event.getAction() == KeyEvent.ACTION_UP &&
                    !event.isCanceled()) {

                Dialouge_accept_reject.dismiss();
                return true;
            }
            return false;
        }
    });

}



    public void Reply_alert() {

        SendDiscardDialog = new Dialog(Actvity_videoPlayback_Private.this,R.style.Theme_Transparent);
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
            i.putExtra("tempEmail", fromemail);
            SendDiscardDialog.dismiss();
            startActivity(i);
            PrivateFileFeedback(file_id, type, "1");
            finish();


        } else if (video_Button == view) {
            Intent i = new Intent(getApplicationContext(), CameraRecordActivity.class);
            i.putExtra("tempEmail", fromemail);
            SendDiscardDialog.dismiss();
            startActivity(i);
            PrivateFileFeedback(file_id, type, "1");
            finish();
        }
    }




    private void PrivateFileFeedback(String fileID, String FileMimeType, String Feedback) {
        String FileType = "video";
        if (FileMimeType.equals("audio/mp3")) {
            FileType = "audio";
        }

        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        RestClient.get(_context).PrivateAcceptReject(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PrivARreq(fileID, FileType, Feedback,
                sharedpreferences.getString(SharedPrefUtils.SpEmail,"")), new Callback<PrivARresp>() {
            @Override
            public void success(final PrivARresp arg0, Response arg1) {
                if (arg0.getCode().equals("200")) {
                    Toast.makeText(_context, "Updated your feelings", Toast.LENGTH_LONG).show();
                    finish();
                } else if (arg0.getCode().equals("601")) {
                    Toast.makeText(_context, "Please try again to accept or reject", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(_context, "Please try again to accept or reject", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void failure(RetrofitError error) {


            }
        });

    }



    @Override
    public void onBackPressed() {
    if(!orientation_poitrate){


    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    Toast.makeText(_context,"Press back again to see Controls",Toast.LENGTH_SHORT).show();
    orientation_poitrate=true;
    }else {



        finish();

    }
        finish();

    }
}
