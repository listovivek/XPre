package com.quad.xpress.Myuploads;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quad.xpress.R;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.clickResponce.Viewed_Req;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.tagList.Tag_cloud_activity_new;
import com.quad.xpress.webservice.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyUploadsAdapter extends RecyclerView.Adapter<MyUploadsAdapter.MyViewHolder> {

    private List<PlayListitems_emotion> rvListitems;
    Context _context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public OnRecyclerListener recyclerListener;
    public String file_id_;
    String video_type;
    String Status = "";
    private View.OnClickListener Reply_click;
    Boolean isPanelVisible = false;
    LinearLayout Rl_parent;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView RvVideoTitle;
        public TextView RvSender,tv_info_likes_lable,tv_info_views_lable;
        public TextView RvTags,RvTags_full;
        public TextView RvTime, RvMoreTag, Rvlikes;
        TextView tv_info_date_created,tv_info_privilege,tv_info_likes,tv_info_views,tv_info_first;
        public ImageView RvImage, Play_icon;
        public LinearLayout rv_menu_button_ll;
        public int position;

        public String data = null;
        public ImageButton menu_button = null;
        public PopupWindow popupWindow = null;
        private OnRecyclerListener recyclerListener = null;
        private TextView Tv_tag1,Tv_tag2,Tv_tag3,Tv_tag4;
        public ProgressBar pbar;
        TextView  Tv_comment_1, Tv_comment_2, Tv_comment_3, Tv_comment_4,Tv_delete;
        LinearLayout ll_button_panel,ll_full_info_panel;

        public MyViewHolder(View view, final Context context, OnRecyclerListener recyclerListeners) {
            super(view);
            this.recyclerListener = recyclerListeners;
            Rvlikes = (TextView) itemView.findViewById(R.id.textView_likes);
            Rl_parent = (LinearLayout) itemView.findViewById(R.id.rl_parent_full);
            rv_menu_button_ll = (LinearLayout) itemView.findViewById(R.id.rv_menu_button_ll);
            menu_button = (ImageButton) itemView.findViewById(R.id.menu_button);
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
            Tv_delete = (TextView) itemView.findViewById(R.id.textView_delete);
            Tv_tag1 = (TextView)itemView.findViewById(R.id.textView_tag_1);
            Tv_tag2 = (TextView)itemView.findViewById(R.id.textView_tag_2);
            Tv_tag3 = (TextView)itemView.findViewById(R.id.textView_tag_3);
            Tv_tag4 = (TextView)itemView.findViewById(R.id.textView_tag_4);
            ll_button_panel = (LinearLayout) itemView.findViewById(R.id.ll_action_panel);
            ll_full_info_panel = (LinearLayout) itemView.findViewById(R.id.ll_action_panel_full);

            tv_info_date_created = (TextView) itemView.findViewById(R.id.amu_tv_info_date_created_data);
            tv_info_privilege = (TextView) itemView.findViewById(R.id.amu_tv_info_privelage_data);
            tv_info_likes = (TextView) itemView.findViewById(R.id.amu_tv_info_likes_data);
            tv_info_views = (TextView) itemView.findViewById(R.id.amu_tv_info_viewes_data);
            tv_info_first = (TextView) itemView.findViewById(R.id.amu_tv_info_show_details);
            tv_info_likes_lable = (TextView) itemView.findViewById(R.id.amu_tv_info_likes);
            tv_info_views_lable = (TextView) itemView.findViewById(R.id.amu_tv_info_viewes);





            Tv_comment_1 = (TextView)itemView.findViewById(R.id.textView_comment_1);
            Tv_comment_2 = (TextView)itemView.findViewById(R.id.textView_comment_2);
            Tv_comment_3 = (TextView)itemView.findViewById(R.id.textView_comment_3);
            Tv_comment_4 = (TextView)itemView.findViewById(R.id.textView_comment_4);

            View contentView = LayoutInflater.from(context).inflate(R.layout.rv_options_layout, null);
            contentView.findViewById(R.id.rv_opt_accept).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_reject).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_block).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_later).setOnClickListener(this);


            popupWindow.setContentView(contentView);

            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, context.getResources().getDisplayMetrics());
            popupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setWidth((int) width);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            menu_button.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
           //ll_button_panel.setVisibility(View.VISIBLE);


        }


    }


    public MyUploadsAdapter(List<PlayListitems_emotion> rvListitems, Context _context, OnRecyclerListener recyclerListener) {
        this.rvListitems = rvListitems;
        this._context = _context;
        this.recyclerListener = recyclerListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_myuploads_grid, parent, false);

      /*  int height = parent.getMeasuredHeight() / 4;
        itemView.setMinimumHeight(height);*/

       return new MyViewHolder(itemView, _context, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        final PlayListitems_emotion list = rvListitems.get(position);
        holder.pbar.setVisibility(View.INVISIBLE);
        holder.RvVideoTitle.setText(list.getTitle());
        holder.RvSender.setText(list.getFromEmail());
        holder.RvTime.setText(list.getCreatedDate());
        holder.RvMoreTag.setText(list.getViewsCount()+" Views");
        holder.RvTags_full.setText(list.getTags().trim());
        String tag_txt = list.getEmotion().trim();
        String tag_store[] = tag_txt.split(",");
        holder.setIsRecyclable(false);

        //set data infoview
        holder.tv_info_date_created.setText(list.getCreatedDate());

      //  Toast.makeText(_context, ""+list.getPrivacy(), Toast.LENGTH_SHORT).show();

        if(list.getPrivacy().equalsIgnoreCase("private")){

            holder.tv_info_privilege.setText("Private");
            holder.Tv_delete.setVisibility(View.GONE);
            holder.tv_info_likes.setVisibility(View.GONE);
            holder.tv_info_views.setVisibility(View.GONE);
            holder.tv_info_likes_lable.setVisibility(View.GONE);
            holder.tv_info_views_lable.setVisibility(View.GONE);


        }else {
            holder.tv_info_privilege.setText("Public");
        }


        holder.tv_info_likes.setText(list.getLikesCount());
        holder.tv_info_views.setText(list.getViewsCount());
        holder.tv_info_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ll_full_info_panel.setVisibility(View.VISIBLE);
            }
        });

        //create up down view
        if ((position % 2)==0){
            final float scale = _context.getResources().getDisplayMetrics().density;
            int Dp2pixels = (int) (220 * scale + 0.5f);
            Rl_parent.getLayoutParams().height = Dp2pixels;
        }
        else{
            final float scale = _context.getResources().getDisplayMetrics().density;
            int Dp2pixels = (int) (245 * scale + 0.5f);
            Rl_parent.getLayoutParams().height = Dp2pixels;  // replace 100 with your dimensions
        }




        if(list.getIsUserLiked().matches("1")){
            holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled_liked, 0, 0, 0);
        }else {

        }
        for(int i =0; i < tag_store.length;i++){

            if(tag_store[i].equalsIgnoreCase("null")){
             tag_store[i]= tag_store[i].replace(tag_store[i],"Reply");

            }
            else {

            }

        }
       // Toast.makeText(_context,"tag_store"+tag_store[1],Toast.LENGTH_LONG).show();

        Reply_click  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }
                file_id_=list.getFileID();
                Intent Tag_act = new Intent(_context, Tag_cloud_activity_new.class);
                Tag_act.putExtra("pos",holder.position);
                Tag_act.putExtra("file_id",file_id_);
                Tag_act.putExtra("file_type",video_type);
                v.getContext().startActivity(Tag_act);
            }
        };


        int likes_count  =  Integer.parseInt(list.getLikesCount());
         holder.Rvlikes.setText(likes_count+" Likes");



        if (!list.getTags().equals("")) {
            holder.RvTags.setVisibility(TextView.VISIBLE);
            holder.RvTags.setText(list.getTags());
        }
        try {
            if (list.getFileMimeType().equals("audio/mp3")) {

                Glide.with(_context).load(R.drawable.ic_mic_placeholder_large).fitCenter()
                        .into(holder.RvImage);

                holder.Play_icon.setVisibility(View.INVISIBLE);

            } else if (list.getFileMimeType().equals("video/mp4")) {
                String TBPath;
                if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                }
            //    Log.v("", "TBPath " + TBPath);
                Glide.with(_context).load(TBPath).centerCrop().into(holder.RvImage);


            }
        } catch (Exception e) {
            Log.v("Exception ", "Exception " + e);
        }

        holder.position = position;


        holder.menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPanelVisible){
                    holder.ll_button_panel.setVisibility(View.VISIBLE);
                    isPanelVisible =true;
                }else {
                    holder.ll_button_panel.setVisibility(View.GONE);
                    holder.ll_full_info_panel.setVisibility(View.GONE);
                    isPanelVisible=false;
                }


               // Toast.makeText(_context, "itemclick", Toast.LENGTH_SHORT).show();

               recyclerListener.MenuOnItem(holder.position);
            }
        });

        holder.Tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }
                file_id_=list.getFileID();


                // interface listner
                recyclerListener.DeleteVideo(file_id_,video_type);


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  holder.pbar.setVisibility(View.VISIBLE);

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
               // Toast.makeText(_context, "Vid--"+video_type, Toast.LENGTH_LONG).show();

            }
        });
        holder.RvMoreTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             /*   String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }
                file_id_=list.getFileID();


                // interface listner
                recyclerListener.DeleteVideo(file_id_,video_type);
*/


            }
        });
        holder.Rvlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(_context, "+1", Toast.LENGTH_LONG).show();
                file_id_=list.getFileID();
                int likes_count  =  Integer.parseInt(list.getLikesCount());
                //int drawableResourceId = _context.getResources().getIdentifier("nameOfDrawable", "drawable", _context.getPackageName());
               // Toast.makeText(_context,""+drawableResourceId,Toast.LENGTH_LONG).show();


                if (list.getIsUserLiked().equals("1")  ){
                    holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled, 0, 0, 0);
                    likes_count--;
                    list.setIsUserLiked("0");
                    list.setLikesCount((""+likes_count));
                    holder.Rvlikes.setText(likes_count+" Likes");
                    Status = "0";
                    mtd_like_count();



                }else  {
                    holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled_liked, 0, 0, 0);
                    Status = "1";
                    likes_count++;
                    list.setIsUserLiked("1");
                    list.setLikesCount((""+likes_count));
                    holder.Rvlikes.setText(likes_count+" Likes");
                    mtd_like_count();

                }



            }
        });



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

        void DeleteVideo(String id, String type);


        void MenuOnItem(int position);



    }




}

