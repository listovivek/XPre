package com.quad.xpress.Adapters_horizontal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.clickResponce.Viewed_Req;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.webservice.RestClient;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class adapter_user_data extends RecyclerView.Adapter<adapter_user_data.MyViewHolder> {
    private List<PlayListitems_emotion> rvListitems;
    Context _context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public OnRecyclerListener recyclerListener;
    public String file_id_;

    String video_type;




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
        TextView  Tv_comment_1, Tv_comment_2, Tv_comment_3, Tv_comment_4,tv_follow,tv_emotion_count;

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


            Tv_comment_1 = (TextView)itemView.findViewById(R.id.textView_comment_1);
            Tv_comment_2 = (TextView)itemView.findViewById(R.id.textView_comment_2);
            Tv_comment_3 = (TextView)itemView.findViewById(R.id.textView_comment_3);
            Tv_comment_4 = (TextView)itemView.findViewById(R.id.textView_comment_4);


                  }


        @Override
        public void onClick(View v) {

        }


    }


    public adapter_user_data(List<PlayListitems_emotion> rvListitems, Context _context, OnRecyclerListener recyclerListener) {
        this.rvListitems = rvListitems;
        this._context = _context;
        this.recyclerListener = recyclerListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_user_data_list, parent, false);

                  return new MyViewHolder(itemView, _context, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PlayListitems_emotion list = rvListitems.get(position);
        holder.pbar.setVisibility(View.INVISIBLE);
        holder.RvVideoTitle.setText(list.getTitle());
        holder.RvSender.setText(list.getFromEmail());
        holder.RvTime.setText(list.getCreatedDate());
        holder.RvMoreTag.setText(""+list.getViewsCount()+" Views");

        try {
            holder.RvTags_full.setText(list.getTags().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("list data",list.getEmotion());
        String tag_txt ="";
        tag_txt = list.getEmotion().trim();
        String tag_store[] ;
        tag_store = tag_txt.split(",");
        holder.setIsRecyclable(false);

      //  Toast.makeText(_context,"tag_store"+tag_store.length,Toast.LENGTH_LONG).show();

        if(list.getIsUserLiked().matches("1")){
           // holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled_liked, 0, 0, 0);
        }else {

        }
        for(int i = 0; i < tag_store.length; i++){

            if(tag_store[i].equalsIgnoreCase("null")){
             tag_store[i]= tag_store[i].replace(tag_store[i],"Reply");

            }
            else {

            }
        }
       // Toast.makeText(_context,"tag_store"+tag_store[1],Toast.LENGTH_LONG).show();
        holder.tv_emotion_count.setText(list.getEmotion_count()+ " Rx");






        int likes_count  =  Integer.parseInt(list.getLikesCount());
         holder.Rvlikes.setText(""+likes_count+" Likes");
        if (!list.getTags().equals("")) {
            holder.RvTags.setVisibility(TextView.VISIBLE);
            holder.RvTags.setText(list.getTags());
        }else {}


        //Tb Images

        try {
            if ( list.getFileMimeType().equalsIgnoreCase("audio/mp3") && list.getTBPath().contains("icons/microphone.png")){

                String TBPath;

                if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");

                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                }
              //  Log.v("", "TBPath " + TBPath);


                Glide.with(_context).load(TBPath).bitmapTransform(new RoundedCornersTransformation(_context,10,0))
                        .fitCenter().into(holder.RvImage);

                holder.Play_icon.setVisibility(View.GONE);
                holder.audio_icon.setVisibility(View.GONE);

            }else if (list.getFileMimeType().equalsIgnoreCase("video/mp4") && list.getTBPath()!=null) {
                    String TBPath;
                    if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                        TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                    }
                 //   Log.v("", "TBPath " + TBPath);

                Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,10,0))
                        .into(holder.RvImage);

            }
            else if ( list.getFileMimeType().equalsIgnoreCase("audio/mp3") && list.getTBPath()!=null){


                    String TBPath;

                    if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                        TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                    } else {
                        TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                    }
                 //   Log.v("", "TBPath " + TBPath);


                Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,10,0))
                        .into(holder.RvImage);

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


        holder.Play_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Log.d("Mes-views",Post_status);
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



    }




}

