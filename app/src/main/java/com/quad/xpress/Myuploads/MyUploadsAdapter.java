package com.quad.xpress.Myuploads;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    Boolean isPanelVisible = false,isPrivate ;
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
        holder.RvVideoTitle.setText(StringUtils.capitalize(list.getTitle().trim()));
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
            holder.Tv_delete.setVisibility(View.VISIBLE);

            holder.tv_info_likes_lable.setVisibility(View.VISIBLE);
            holder.tv_info_likes_lable.setText("To");

            holder.tv_info_likes.setVisibility(View.VISIBLE);
            holder.tv_info_likes.setText(list.getTo_email());

            holder.tv_info_views.setVisibility(View.GONE);
            holder.tv_info_views_lable.setVisibility(View.GONE);


        }else {
            holder.tv_info_privilege.setText("Public");
            holder.tv_info_likes.setText(list.getLikesCount());
            holder.tv_info_views.setText(list.getViewsCount());
            int likes_count  =  Integer.parseInt(list.getLikesCount());
            holder.Rvlikes.setText(likes_count+" Likes");

        }



        holder.tv_info_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ll_full_info_panel.setVisibility(View.VISIBLE);
            }
        });

        //create up down view
        if ((position % 2)==0){
            final float scale = _context.getResources().getDisplayMetrics().density;
            int Dp2pixels = (int) (240 * scale + 0.5f);
            Rl_parent.getLayoutParams().height = Dp2pixels;
        }
        else{
            final float scale = _context.getResources().getDisplayMetrics().density;
            int Dp2pixels = (int) (265 * scale + 0.5f);
            Rl_parent.getLayoutParams().height = Dp2pixels;  // replace 100 with your dimensions
        }









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
                if(list.getPrivacy().equalsIgnoreCase("private")){
                    isPrivate = true;
                }else {
                    isPrivate =false;
                }
                recyclerListener.DeleteVideo(file_id_,video_type,isPrivate);


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


               // Toast.makeText(_context, "Vid--"+video_type, Toast.LENGTH_LONG).show();

            }
        });



    }








    @Override
    public int getItemCount() {
        return rvListitems.size();
    }

    public interface OnRecyclerListener {
        void onItemClicked(int position);

        void DeleteVideo(String id, String type, Boolean isPrivate);


        void MenuOnItem(int position);



    }




}

