package com.quad.xpress.viewPagers;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
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
import com.quad.xpress.R;
import com.quad.xpress.utills.audio_visulizer;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.StaticConfig;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.tagList.TagsAdapter_list_view;
import com.quad.xpress.models.tagList.TagsModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kural on 29/6/17.
 */

public class AdapterSwipePlayback extends PagerAdapter {


    ArrayList<String> titts = new ArrayList<>();
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
    AppCompatActivity activity;
    EditText abuse_edt;
    Dialog mBottomSheetDialog, AVDialog;
    static Boolean like_clicked = false, isFromFeatured =false;
    SpannableString spannableStringTag;
    //TextView btn_post,btn_cancel,btn_abuse;
    // EditText et_abuse;
    Boolean abuseOrnot=true;
    public static int LikeChangedValue;
    audio_visulizer audio_visulizerm;
    Visualizer mVisualizer;
    CircleImageView iv_profile_pic;

    List<TagsModel> tag_list_data = new ArrayList<>();
    int pos=0;
    //  public LoadingDialog LD;
    String description="No Value",file_type= "video";
    MediaPlayer mMediaPlayer;
    RecyclerView rv_tag_list;
    TagsAdapter_list_view adapterTagList;
    LinearLayout Ll_btm,Ll_btm_CP;
    InputMethodManager inputMethodManager;

    private Context context;
    private List<PlayListitems_emotion> dataObjectList;
    private LayoutInflater layoutInflater;
    public AdapterSwipePlayback(List<PlayListitems_emotion> dataObjectList, Context context, AppCompatActivity activity){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.dataObjectList = dataObjectList;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return dataObjectList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {




        return view == ((View)object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Toast.makeText(activity, "INIT"+position, Toast.LENGTH_SHORT).show();
        View layout = this.layoutInflater.inflate(R.layout.adapter_swipe_playback, container, false);
        context =container.getContext();
        NetConnectionDetector NCD;
        NCD = new NetConnectionDetector();
        LikeChangedValue = 0;
        Bottom_view = layoutInflater.inflate(R.layout.bottom_paper, null);

        audio_visulizerm = (audio_visulizer) layout.findViewById(R.id.audio_visulizer_videoview);
        iv_profile_pic = (CircleImageView) layout.findViewById(R.id.iv_videoview_profile_circle);
        tv_title = (TextView) layout.findViewById(R.id.tv_vv_title);
        tv_tag = (TextView) layout.findViewById(R.id.tv_vv_tag);
        tv_likes = (TextView) layout.findViewById(R.id.tv_vv_likes);
        tv_views = (TextView) layout.findViewById(R.id.tv_vv_views);
        tv_comment = (TextView) layout.findViewById(R.id.tv_vv_emotion);
        tv_date  = (TextView) layout.findViewById(R.id.tv_vv_date);
        iv_bg = (ImageView) layout.findViewById(R.id.videoView_bg_image);
        ll_btm_bar = (LinearLayout) layout.findViewById(R.id.ll_vv_data_bar);
        ll_btm = (LinearLayout) layout.findViewById(R.id.ll_vv_btm);
        pb = (ProgressBar) layout.findViewById(R.id.progressBar_video_view);
        Ib_close = (ImageButton) layout.findViewById(R.id.btn_vv_back);
        Ib_loop = (ImageButton) layout.findViewById(R.id.btn_vv_repeat);
        iv_audio_bg = (ImageView) layout.findViewById(R.id.videoView_bg_image_audio);
        mVideoView  = (VideoView) layout.findViewById(R.id.videoView);

     /*   if(layout.isFocused()){
            Toast.makeText(activity, ""+position, Toast.LENGTH_SHORT).show();
        }*/



        // tv_tag.setText(dataObjectList.get(position).getTitle());



        try {
            url = dataObjectList.get(position).getFileuploadPath();
            type = dataObjectList.get(position).getFileMimeType();
            likes = dataObjectList.get(position).getLikesCount();
            views = dataObjectList.get(position).getViewsCount();
            file_id = dataObjectList.get(position).getFileID();
            title = dataObjectList.get(position).getTitle();
            ul_date = dataObjectList.get(position).getCreatedDate();
            IsUserLiked =dataObjectList.get(position).getIsUserLiked();
            bg_img_url = dataObjectList.get(position).getTBPath();

            if(IsUserLiked.equals("1")){
                tv_likes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_heart_white_liked, 0, 0, 0);
            }else {

            }

            tv_views.setText(" "+views);
            tv_likes.setText(" "+likes);



        } catch (Exception e) {
            e.printStackTrace();
        }


        if (NCD.isConnected(context)) {
            //Live server
            if (url.contains(StaticConfig.ROOT_URL_Media)) {

                url = StaticConfig.ROOT_URL + url.replace(StaticConfig.ROOT_URL_Media, "");

                // Toast.makeText(DashBoard.this, "url --"+Selected_file_url, Toast.LENGTH_SHORT).show();
            } else {
                //Local server
                //  Toast.makeText(DashBoard.this, "else --"+Selected_file_url, Toast.LENGTH_SHORT).show();
                url = StaticConfig.ROOT_URL + "/" + url;

            }
        }

            // TbImage
            String TBPath = "audio";

            if (type.equalsIgnoreCase("video/mp4")) {

                if (bg_img_url.contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + bg_img_url.replace(StaticConfig.ROOT_URL_Media, "");

                    // Toast.makeText(DashBoard.this, "url"+Selected_file_url, Toast.LENGTH_SHORT).show();

                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + bg_img_url;
                }


            }
            else if ( type.equalsIgnoreCase("audio/mp3") ){



                if (bg_img_url.contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + bg_img_url.replace(StaticConfig.ROOT_URL_Media, "");
                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + bg_img_url;
                }

            }else {
                TBPath = "audio";

            }
            iv_bg.setVisibility(View.VISIBLE);
        Glide.with(context).load(bg_img_url).bitmapTransform(new BlurTransformation(context)).into(iv_bg);
        //Toast.makeText(context, ""+TBPath, Toast.LENGTH_SHORT).show();
        if(type.contains("audio/mp3")){

            try {
                initAudio();
            } catch (Exception e) {
                e.printStackTrace();
              //  finish();
                Toast.makeText(context, "Bad Media", Toast.LENGTH_SHORT).show();
            }

            mVideoView.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.GONE);
            if ( (bg_img_url.equalsIgnoreCase("audio"))|| (bg_img_url.contains("icons/microphone.png")) ){

                iv_audio_bg.setVisibility(View.VISIBLE);
                Picasso.with(context).load(R.color.translucent_txt_black)
                        .into(iv_bg);


            }else {
                Glide.with(context).load(bg_img_url).bitmapTransform(new BlurTransformation(context)).into(iv_bg);
                Picasso.with(context).load(bg_img_url).into(iv_profile_pic);

                iv_profile_pic.setVisibility(View.VISIBLE);
            }



        }else {


            if ( (bg_img_url.equalsIgnoreCase("audio"))|| (bg_img_url.contains("icons/microphone.png")) ){

                iv_audio_bg.setVisibility(View.VISIBLE);
                Picasso.with(context).load(R.color.translucent_txt_black)
                        .into(iv_bg);
                //  Toast.makeText(_context, "Audio file", Toast.LENGTH_SHORT).show();

            }else {
                Picasso.with(context).load(bg_img_url).into(iv_bg);

            }




            mVideoView.setVideoURI(Uri.parse(url));
            mVideoView.seekTo(100);


        }



        container.addView(layout);
        return layout;
    }

    private void initAudio() {
       activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
              //  finish();

                return false;
            }
        });


    }
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
    public void destroyItem(ViewGroup container, int position, Object object) {


       // Toast.makeText(activity, "ex"+position, Toast.LENGTH_SHORT).show();
        container.removeView((View) object);
    }
    void mtd_destoy_mp(){
    if (mMediaPlayer != null) {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}

}
