package com.quad.xpress.Adapters_horizontal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.quad.xpress.Act_user_data;
import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.clickResponce.Viewed_Req;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.webservice.RestClient;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class adapter_autoplay extends RecyclerView.Adapter<adapter_autoplay.MyViewHolder> {
    private List<PlayListitems_emotion> rvListitems;
    Context _context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public OnRecyclerListener recyclerListener;
    public String file_id_;

    String video_type;
    String Status = "";
    String TBPath;
    VideoView vv;
    int finalHeight, finalWidth;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView RvVideoTitle;
        public TextView RvSender;
        public TextView RvTags,RvTags_full;
        public TextView RvTime, RvMoreTag, Rvlikes;
        public ImageView RvImage, Play_icon, audio_icon;
        public LinearLayout rv_menu_button_ll,ll_follow_user;
        public int position;
        public String data = null;
        public ImageButton button = null;
        public PopupWindow popupWindow = null;
        private OnRecyclerListener recyclerListener = null;

        public ProgressBar pbar;
        TextView  tv_follow,tv_emotion_count;

        public MyViewHolder(View view, final Context context, OnRecyclerListener recyclerListeners) {
            super(view);
            this.recyclerListener = recyclerListeners;
            Rvlikes = (TextView) itemView.findViewById(R.id.textView_likes);
            tv_follow = (TextView) itemView.findViewById(R.id.textView_followuser);
            tv_emotion_count = (TextView) itemView.findViewById(R.id.textView_comments);
           // Rl_moreTags = (RelativeLayout) itemView.findViewById(R.id.RelativeLayout_moreTgas);
            audio_icon = (ImageView) itemView.findViewById(R.id.imageView_audio_icon);
            rv_menu_button_ll = (LinearLayout) itemView.findViewById(R.id.rv_menu_button_ll);
            button = (ImageButton) itemView.findViewById(R.id.menu_button);
            RvImage = (ImageView) itemView.findViewById(R.id.RvImage);
            Play_icon = (ImageView) itemView.findViewById(R.id.imageView_play_icon);
            RvVideoTitle = (TextView) itemView.findViewById(R.id.RvVideoTitle);
            RvSender = (TextView) itemView.findViewById(R.id.RvSender);
            RvTags = (TextView) itemView.findViewById(R.id.RvTags);
            RvTags_full = (TextView) itemView.findViewById(R.id.RvTags_new);
            RvTime = (TextView) itemView.findViewById(R.id.RvTime);
            RvMoreTag = (TextView) itemView.findViewById(R.id.textView_viewsMoreTags);
            pbar = (ProgressBar) itemView.findViewById(R.id.progressBar_publiclsit);
            popupWindow = new PopupWindow(context);
            ll_follow_user = (LinearLayout) itemView.findViewById(R.id.ll_followuser);
            vv = (VideoView) itemView.findViewById(R.id.videoView_auto);

        }


        @Override
        public void onClick(View v) {




        }


    }


    public adapter_autoplay(List<PlayListitems_emotion> rvListitems, Context _context, OnRecyclerListener recyclerListener) {
        this.rvListitems = rvListitems;
        this._context = _context;
        this.recyclerListener = recyclerListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_autoplay_list, parent, false);

                  return new MyViewHolder(itemView, _context, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PlayListitems_emotion list = rvListitems.get(position);
        holder.pbar.setVisibility(View.INVISIBLE);
        holder.RvVideoTitle.setText(StringUtils.capitalize(list.getTitle().trim()));

        holder.RvSender.setText(list.getFromEmail());
        holder.RvTime.setText(list.getCreatedDate());
        holder.RvMoreTag.setText(""+list.getViewsCount()+" Views");
        holder.setIsRecyclable(false);
        try {
            holder.RvTags_full.setText(list.getTags().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }


        int emotionCount  =  Integer.parseInt(list.getEmotion_count());
        holder.tv_emotion_count.setText(""+emotionCount+" Rx");
        if(emotionCount > 99 ){
            emotionCount = Integer.parseInt(Integer.toString(emotionCount).substring(0, 1));
            holder.tv_emotion_count.setText(emotionCount+" h Rx");
        } else if(emotionCount > 999 ){

            emotionCount = Integer.parseInt(Integer.toString(emotionCount).substring(0, 1));
            holder.tv_emotion_count.setText(emotionCount+" k Rx");
        } else if(emotionCount > 9999 ){
            emotionCount = Integer.parseInt(Integer.toString(emotionCount).substring(0, 2));
            holder.tv_emotion_count.setText(emotionCount+" k Rx");
        } if(emotionCount > 9999999 ){
            emotionCount = Integer.parseInt(Integer.toString(emotionCount).substring(0, 1));
            holder.tv_emotion_count.setText(emotionCount+" m Rx");
        }




        holder.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IAct = new Intent(_context, Act_user_data.class);
                    String getMydp;

                if (list.getMydp().contains(StaticConfig.ROOT_URL_Media)) {
                    getMydp = StaticConfig.ROOT_URL + list.getMydp().replace(StaticConfig.ROOT_URL_Media, "");

                } else {
                    getMydp = StaticConfig.ROOT_URL + "/" + list.getMydp();
                }

                IAct.putExtra("ProfilePic",getMydp);
               /* IAct.putExtra("username",list.from_user);
                IAct.putExtra("fromemail",list.from_email);*/
                IAct.putExtra("isfollowing",list.getIsUserFollowing());

                v.getContext().startActivity(IAct);


            }
        });



     /*   ViewTreeObserver vto = holder.RvImage.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // Remove after the first run so it doesn't fire forever
                holder.RvImage.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = holder.RvImage.getMeasuredHeight();
                finalWidth = holder.RvImage.getMeasuredWidth();

                return true;
            }
        });
*/


        if (list.getIsUserFollowing().equalsIgnoreCase("1")){

            holder.tv_follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unfollow,0,0,0);

        }else {
            holder.tv_follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_follow_user ,0,0,0);

        }



       /* if (list.getIsUserFollowing().equalsIgnoreCase("1")){

            holder.tv_follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unfollow,0,0,0);
            holder.tv_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerListener.UnSubcribed(position,list.getFromEmail());


                }});


            holder.ll_follow_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerListener.UnSubcribed(position,list.getFromEmail());

                }
            });


                }else {
                holder.tv_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerListener.Subcribed(position,list.getFromEmail());


                }});

            holder.ll_follow_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerListener.Subcribed(position,list.getFromEmail());

                }
            });



        }
*/




        int likes_count  =  Integer.parseInt(list.getLikesCount());
         holder.Rvlikes.setText(""+likes_count+" Likes");

        if(likes_count > 99 ){
            likes_count = Integer.parseInt(Integer.toString(likes_count).substring(0, 1));
            holder.Rvlikes.setText(likes_count+" h Likes");
        } else if(likes_count > 999 ){

            likes_count = Integer.parseInt(Integer.toString(likes_count).substring(0, 1));
            holder.Rvlikes.setText(likes_count+" k Likes");
        } else if(likes_count > 9999 ){
            likes_count = Integer.parseInt(Integer.toString(likes_count).substring(0, 2));
            holder.Rvlikes.setText(likes_count+" k Likes");
        } if(likes_count > 9999999 ){
            likes_count = Integer.parseInt(Integer.toString(likes_count).substring(0, 1));
            holder.Rvlikes.setText(likes_count+" m Likes");
        }





        if (!list.getTags().equals("")) {
            holder.RvTags.setVisibility(TextView.VISIBLE);
            holder.RvTags.setText(list.getTags());
        }else {}


        //Tb Images

        try {
            if ( list.getFileMimeType().equalsIgnoreCase("audio/mp3") && list.getTBPath().contains("icons/microphone.png")){

                if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");

                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                }
               // Log.v("", "TBPath " + TBPath);

                Glide.with(_context).load(TBPath).fitCenter().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.RvImage);

                holder.Play_icon.setVisibility(View.GONE);
                holder.audio_icon.setVisibility(View.GONE);

            }else if (list.getFileMimeType().equalsIgnoreCase("video/mp4") && list.getTBPath()!=null) {

                    if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                        TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                    }


                Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,10,0))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) .dontAnimate().into(holder.RvImage);

            }
            else if ( list.getFileMimeType().equalsIgnoreCase("audio/mp3") && list.getTBPath()!=null){

                    if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                        TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                    }

                Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,10,0))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) .into(holder.RvImage);

                   holder.Play_icon.setVisibility(View.GONE);
                   holder.audio_icon.setVisibility(View.VISIBLE);

                }
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.position = position;

        holder.RvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.INVISIBLE);

                recyclerListener.onItemClicked(holder.position);

                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;
                if(views_count > 99 ){
                views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" h Views");
                } else if(views_count > 999 ){

                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } else if(views_count > 9999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 2));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } if(views_count > 9999999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" m Views");
                }




                file_id_=list.getFileID();


                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();

            }
        });


        holder.Play_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.INVISIBLE);

                recyclerListener.onItemClicked(holder.position);
                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;
                if(views_count > 99 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" h Views");
                } else if(views_count > 999 ){

                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } else if(views_count > 9999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 2));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } if(views_count > 9999999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" m Views");
                }

                holder.RvMoreTag.setText(views_count+" Views");
                file_id_=list.getFileID();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();
            }
        });


        holder.RvVideoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.INVISIBLE);

                recyclerListener.onItemClicked(holder.position);
                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;
                if(views_count > 99 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" h Views");
                } else if(views_count > 999 ){

                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } else if(views_count > 9999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 2));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } if(views_count > 9999999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" m Views");
                }

                holder.RvMoreTag.setText(views_count+" Views");
                file_id_=list.getFileID();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();
            }
        });


        holder.audio_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.INVISIBLE);

                recyclerListener.onItemClicked(holder.position);

                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;
                if(views_count > 99 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" h Views");
                } else if(views_count > 999 ){

                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } else if(views_count > 9999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 2));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } if(views_count > 9999999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" m Views");
                }


                holder.RvMoreTag.setText(views_count+" Views");
                file_id_=list.getFileID();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.INVISIBLE);

                recyclerListener.onItemClicked(holder.position);

                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;
                if(views_count > 99 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" h Views");
                } else if(views_count > 999 ){

                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } else if(views_count > 9999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 2));
                    holder.RvMoreTag.setText(views_count+" k Views");
                } if(views_count > 9999999 ){
                    views_count = Integer.parseInt(Integer.toString(views_count).substring(0, 1));
                    holder.RvMoreTag.setText(views_count+" m Views");
                }


                holder.RvMoreTag.setText(views_count+" Views");
                file_id_=list.getFileID();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();
            }
        });

      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {


                    case R.id.RvImage:
                    case R.id.imageView_play_icon:
                    case R.id.imageView_audio_icon:
                    case R.id.RvVideoTitle:

                        Toast.makeText(_context, "Click", Toast.LENGTH_LONG).show();
                        holder.pbar.setVisibility(View.INVISIBLE);

                        recyclerListener.onItemClicked(holder.position);

                        int views_count;
                        views_count=Integer.parseInt(list.getViewsCount());
                        views_count++;

                        holder.RvMoreTag.setText(views_count+" Views");
                        file_id_=list.getFileID();
                        String Vtype = list.getFileMimeType();
                        if(Vtype.equalsIgnoreCase("video/mp4")){
                            video_type = "video";
                        }else {
                            video_type = "audio";
                        }

                        mtd_views_count();
                        break;
                }


                Toast.makeText(_context, "Outside Click", Toast.LENGTH_LONG).show();

            }
        });*/



    }




    private void mtd_like_count() {

        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        String Emotion="like";

        RestClient.get(_context).Liked(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Like_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Emotion,Status,file_id_),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {

                            String Post_status = arg0.getStatus();
                        //    Log.d("Mes-Like",Post_status);
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

    private void mtd_views_count() {
        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String viewed = "1";

        //"id":"57318bd6db923d57643edd59","viewed":"1","video_type":"video"}
        RestClient.get(_context).Viewd(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Viewed_Req(file_id_,viewed,video_type),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {
                        // LD.DismissTheDialog();
                        if (arg0.getCode().equals("200")) {

                            String Post_status = arg0.getStatus();
                       //     Log.d("Mes-views",Post_status);
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
    public int getItemCount() {
        return rvListitems.size();
    }

    public interface OnRecyclerListener {
        void onItemClicked(int position);

        void Subcribed(int position, String email);
        void UnSubcribed(int position, String email);


    }




}

