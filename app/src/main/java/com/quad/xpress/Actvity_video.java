package com.quad.xpress;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.quad.xpress.Utills.audio_visulizer;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.models.abuse_resp.Abuse_Req;
import com.quad.xpress.models.abuse_resp.Abuse_response;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.tagList.TagListRecords;
import com.quad.xpress.models.tagList.TagListReq;
import com.quad.xpress.models.tagList.TagListResp;
import com.quad.xpress.models.tagList.TagsAdapter_list_view;
import com.quad.xpress.models.tagList.TagsModel;
import com.quad.xpress.webservice.RestClient;
import com.squareup.picasso.Picasso;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 11/3/2016.
 */

public class Actvity_video extends AppCompatActivity{
    public static String file_id;
    String url,type,likes,views,title,ul_date,tags,IsUserLiked,bg_img_url;
    VideoView mVideoView;
    ProgressBar pb;
   // TextView tv_timer;
    Animation animBlink;
    Boolean looping=true,orientation_poitrate= true,vid_orintaion_lanscape= false ;
    NetConnectionDetector NDC;
    ImageView iv_bg,iv_audio_bg;
    ImageButton Ib_close,Ib_loop,btn_vv_next,btn_tb_back;
    TextView tv_title,tv_tag,tv_likes,tv_views,tv_comment,tv_date;
    MediaController mc;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout ll_btm_bar,ll_btm;
    int vwidth,vheight;
    RelativeLayout Rl_tb;
    Boolean isPrivate = false;
    int Likes_count  = 0;
    View Bottom_view;
    EditText abuse_edt;
    Dialog mBottomSheetDialog, AVDialog;
    AutoLabelUI tagviewLable;
    static Boolean like_clicked = false, isFromFeatured =false;
    SpannableString spannableStringTag;
    //TextView btn_post,btn_cancel,btn_abuse;
   // EditText et_abuse;
    Boolean abuseOrnot=true;
    public static int LikeChangedValue;
    audio_visulizer  audio_visulizerm;
    Visualizer mVisualizer;
    CircleImageView iv_profile_pic;
    Context context;
    ActionBar actionBar;
    List<TagsModel> tag_list_data = new ArrayList<>();
    int pos=0;
  //  public LoadingDialog LD;
    String description="No Value",file_type= "video";
    MediaPlayer mMediaPlayer;
    RecyclerView rv_tag_list;
    TagsAdapter_list_view adapterTagList;
    LinearLayout Ll_btm,Ll_btm_CP;
    InputMethodManager inputMethodManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        LikeChangedValue = 0;
        context =getApplicationContext();
        Bottom_view = getLayoutInflater().inflate(R.layout.bottom_paper, null);
        Rl_tb = (RelativeLayout) findViewById(R.id.tb);
        btn_vv_next = (ImageButton) findViewById(R.id.btn_vv_next);
        audio_visulizerm = (audio_visulizer) findViewById(R.id.audio_visulizer_videoview);
        iv_profile_pic = (CircleImageView) findViewById(R.id.iv_videoview_profile_circle);
       // tv_timer = (TextView) findViewById(R.id.tv_timer);
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
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);


         TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBottomSheetDialog = new Dialog(Actvity_video.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(Bottom_view);
        tagviewLable = (AutoLabelUI) mBottomSheetDialog.findViewById(R.id.vv_tag_view);


        AutoLabelUISettings autoLabelUISettings =
                new AutoLabelUISettings.Builder()
                        .withBackgroundResource(R.drawable.bg_round_corner)
                        .withIconCross(R.drawable.cross)
                        .withMaxLabels(4)
                        .withShowCross(false)
                        .withLabelsClickables(true)
                        .withTextColor(R.color.primary)
                        .withTextSize(R.dimen.label_title_size)
                        .withLabelPadding(30)
                        .build();

        tagviewLable.setSettings(autoLabelUISettings);





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

        try {
            isFromFeatured = getVurl.getBooleanExtra("fromfeatured",false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String isPvt   = "false";

        try {
            Likes_count = Integer.parseInt(likes);
            LikeChangedValue = Likes_count;
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

        //set text
        tv_title.setText(title);
        tv_tag.setText(tags);

        tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SearchIntent = new Intent(Actvity_video.this,Search_activity_list.class);
                SearchIntent.putExtra("searchstring",tv_tag.getText());
                startActivity(SearchIntent);
            }
        });


        List<String> TagList = Arrays.asList(tags.split("[ ,#]"));

        for (int i = 0; i <TagList.size() ; i++) {

            if(TagList.get(i).trim().length()!= 0){
                String tmp = TagList.get(i);
                tagviewLable.addLabel(tmp);

            }

        }

        tagviewLable.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(View v) {


            }
        });


        tv_views.setText(" "+views);
        tv_likes.setText(" "+likes);
        tv_date.setText(ul_date);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.contains("audio/mp3")){
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
               finish();
            }
        });
        tv_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                like_clicked = true;
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
                //Toast.makeText(Actvity_video.this, ""+file_id, Toast.LENGTH_SHORT).show();
                LikeChangedValue = Likes_count;

                mtd_like_count();

            }
        });
    tv_comment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           // Tag_cloud_activity_new.getInstance().callAct();

            react();

            /*Intent Tag_act = new Intent(_context, Tag_cloud_activity_new.class);

            Tag_act.putExtra("file_id",file_id);
            Tag_act.putExtra("file_type",type);
            startActivity(Tag_act);*/
        }
    });


        String LINK = url;



        mVideoView  = (VideoView) findViewById(R.id.videoView);
        mc = new MediaController(this);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ll_btm_bar.setVisibility(View.VISIBLE);
                mVideoView.setMediaController(mc);
                return false;

            }
        });

        mc.setAnchorView(mVideoView);
        mc.setMediaPlayer(mVideoView);

        Uri video = Uri.parse(LINK);

        if(type.contains("audio/mp3")){

            try {
                initAudio();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
                Toast.makeText(context, "Bad Media", Toast.LENGTH_SHORT).show();
            }

            mVideoView.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.GONE);
            if ( (bg_img_url.equalsIgnoreCase("audio"))|| (bg_img_url.contains("icons/microphone.png")) ){

                iv_audio_bg.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(R.color.translucent_txt_black)
                        .into(iv_bg);


            }else {
                Glide.with(context).load(bg_img_url).bitmapTransform(new BlurTransformation(context)).into(iv_bg);
                Picasso.with(getApplicationContext()).load(bg_img_url).into(iv_profile_pic);

                iv_profile_pic.setVisibility(View.VISIBLE);
            }



        }else {


        if ( (bg_img_url.equalsIgnoreCase("audio"))|| (bg_img_url.contains("icons/microphone.png")) ){

            iv_audio_bg.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(R.color.translucent_txt_black)
                    .into(iv_bg);
            //  Toast.makeText(_context, "Audio file", Toast.LENGTH_SHORT).show();

        }else {
            Picasso.with(getApplicationContext()).load(bg_img_url).into(iv_bg);

        }




        mVideoView.setVideoURI(video);
            mVideoView.seekTo(100);


        }
       // mVideoView.requestFocus();



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




            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(looping){mp.setLooping(true);
                mp.start();}
                else {mp.setLooping(false);
                mp.stop();}

            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                Toast.makeText(context, "That video is not Playable in your device", Toast.LENGTH_SHORT).show();
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

    private void react() {

        ll_btm_bar.setVisibility(View.GONE);


        mBottomSheetDialog.setCancelable(true);
        ImageButton dismiss = (ImageButton) mBottomSheetDialog.findViewById(R.id.btnm_bar_dismiss);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        mBottomSheetDialog.show();



        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(Actvity_video.this, "Dismissed", Toast.LENGTH_SHORT).show();
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        context = getApplicationContext();

        Button abuseBtn = (Button) Bottom_view.findViewById(R.id.abuse_btn);
        abuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_btm_bar.setVisibility(View.VISIBLE);
                mBottomSheetDialog.dismiss();
                    abuseDialog();
            }
        });

        Ll_btm_CP = (LinearLayout) Bottom_view.findViewById(R.id.linearLayout_btm_CP);
        Ll_btm = (LinearLayout) Bottom_view.findViewById(R.id.linearLayout_btm);

        rv_tag_list = (RecyclerView) Bottom_view.findViewById(R.id.Rv_tags_list);

        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        tag_list_data.clear();
        getTagData();

        adapterTagList = new TagsAdapter_list_view(tag_list_data,context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_tag_list.setLayoutManager(mLayoutManager);
        rv_tag_list.setItemAnimator(new DefaultItemAnimator());
        rv_tag_list.setAdapter(adapterTagList);


        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                    ll_btm_bar.setVisibility(View.VISIBLE);



            }
        });


     }

    private void abuseDialog() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AVDialog = new Dialog(Actvity_video.this, R.style.AVdialouge);
        AVDialog.setContentView(R.layout.abuse_sublayout);

        abuse_edt = (EditText) AVDialog.findViewById(R.id.abuse_edittext);
        Button abuse_post_btn = (Button) AVDialog.findViewById(R.id.abuse_send);
        Button abuse_discard_btn = (Button) AVDialog.findViewById(R.id.abuse_discard);




        abuse_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_btm_bar.setVisibility(View.VISIBLE);
                mBottomSheetDialog.dismiss();
                   // abuse_edt.clearFocus();



                   /* inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(abuse_edt.getWindowToken(), 0);*/


                    description = abuse_edt.getText().toString();
                    if(description.length()  < 10){
                        Toast.makeText(getApplicationContext(),"Minimum length 10 characters ",Toast.LENGTH_SHORT).show();
                    }else {
                        //  Toast.makeText(getApplicationContext(),"ft "+file_type,Toast.LENGTH_SHORT).show();
                        AVDialog.dismiss();

                        post_abuse();
                        abuse_edt.clearComposingText();
                        Toast.makeText(getApplicationContext(),"Thank you for your response. We are looking into your concern.",Toast.LENGTH_SHORT).show();
                    }
                }

        });

        abuse_discard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_btm_bar.setVisibility(View.VISIBLE);
                mBottomSheetDialog.dismiss();
                AVDialog.dismiss();
              /*//  abuse_edt.clearFocus();


                  //  Ll_btm.setOrientation(LinearLayout.HORIZONTAL);

                    inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(abuse_edt.getWindowToken(), 0);*/



                }

        });


        AVDialog.show();

        abuseOrnot = true;
       /* Ll_btm.setOrientation(LinearLayout.VERTICAL);

        abuse_edt.requestFocus();

        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(abuse_edt, InputMethodManager.SHOW_FORCED);
        abuse_edt.setCursorVisible(true);

        if (inputMethodManager.isAcceptingText()) {
            Log.d("keypad", "show");
        } else {
            Log.d("keypad", "not show");
        }*/

    }


    private void post_abuse() {

        RestClient.get(context).Post_abuse(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),
                new Abuse_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),description, type, file_id),
                new Callback<Abuse_response>() {
                    @Override
                    public void success(final Abuse_response arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {
                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                            String Post_status = arg0.getStatus();
                            if(Post_status.equalsIgnoreCase("ok")){
                                //   Toast.makeText(context, "Success +1", Toast.LENGTH_LONG).show();
                                inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                               // inputMethodManager.hideSoftInputFromWindow(et_abuse.getWindowToken(), 0);
                            }else {
                                Toast.makeText(context, "Failed retry...", Toast.LENGTH_LONG).show();

                            }

                            //  Populate();
                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(context, "hmm, Trouble..", Toast.LENGTH_LONG).show();
                    }
                });


    }

    public void  getTagData() {

       // LD.ShowTheDialog("Please Wait", "Loading..", true);

        RestClient.get(context).TagCloudApi(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new TagListReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),file_id),
                new Callback<TagListResp>() {
                    @Override
                    public void success(final TagListResp arg0, Response arg1) {
                       // LD.DismissTheDialog();
                        if (arg0.getCode().equals("200")) {
                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                            int RLength = arg0.getData().getRecords().length;
                            for (int i = 0; i < RLength; i++) {
                                TagListRecords iii = arg0.getData().getRecords()[i];
                                //Log.v("Taglist 200"," e "+iii.getEmotion()+" c "+iii.getCount()+" ch "+iii.getIsUserChecked());
                                String t_e,t_c,t_bool;
                                t_e = iii.getEmotion();
                                t_c = iii.getCount();
                                t_bool = iii.getIsUserChecked();
                                TagsModel tagsModel = new TagsModel(t_e,t_c,t_bool,file_id);
                                tag_list_data.add(tagsModel);
                            }
                         //   LD.DismissTheDialog();
                            adapterTagList.notifyDataSetChanged();


                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                     //   LD.DismissTheDialog();
                        //   errorReporting.SendMail("Xpress Error-publicplaylist-getData", error.toString());
                        Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
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
            }else {
                ll_btm_bar.setVisibility(View.VISIBLE);
            }
           // ll_btm.setVisibility(View.VISIBLE);
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

        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        String Emotion="like";

        RestClient.get(context).Liked(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Like_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Emotion,IsUserLiked,file_id),
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
                            Toast.makeText(context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }
                        //Populate();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                       // LD.DismissTheDialog();

                        Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void initAudio() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

       mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                               @Override
                                               public void onPrepared(MediaPlayer mp) {


                                                   mMediaPlayer.start();
                                                   mMediaPlayer.setLooping(true);


                                               }
                                           });



        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {

                      //  initAudio();

                    }
                });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                Toast.makeText(context, "That media is not Playable in your device"+what, Toast.LENGTH_SHORT).show();
                finish();

                return false;
            }
        });


    }

   /* public class AudioRecordCountDown extends CountDownTimer {

        public AudioRecordCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            tv_timer.setText("00:40");

        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            long hmsvalue = TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
            // String hms = String.format("%02d", hmsvalue);
            int hms = (int) hmsvalue - 0;
            //AudioSeekBar.setProgress(hms);
            tv_timer.setText(String.valueOf("00:"+hms));
            if (hms == 5) {
                tv_timer.startAnimation(animBlink);
            }
        }
    }*/

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        audio_visulizerm.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        ll_btm_bar.setVisibility(View.GONE);
        ll_btm.setVisibility(View.GONE);
        orientation_poitrate=false;
        if(orientation_poitrate){

            ll_btm_bar.setVisibility(View.VISIBLE);
            ll_btm.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {
       // Log.d("keypad", "backpressed");
     /*   if(type.contains("audio/mp3")){
      onDestroy();}*/

    if(!orientation_poitrate){

    ll_btm_bar.setVisibility(View.VISIBLE);
    ll_btm.setVisibility(View.VISIBLE);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    Toast.makeText(context,"Press back again to see Controls",Toast.LENGTH_SHORT).show();
    orientation_poitrate=true;
    }else {

        finish();

    }

   }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
