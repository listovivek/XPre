package com.quad.xpress.models.AlertStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.daimajia.swipe.SwipeLayout;
import com.quad.xpress.R;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.StaticConfig;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AdapterAlertStream_norecycle extends RecyclerView.Adapter<AdapterAlertStream_norecycle.MyViewHolder>{

    private List<AlertStreamModelList> ListData;
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    OnRecyclerListener recyclerListener;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, user_name, time,date;
        ImageView iv_thumb,iv_playIcon,iv_mic_top_left;
        public SwipeLayout rv_swipeLayout;
        private LinearLayout swipe_Layout;
        RelativeLayout swipe_follower,RL_item;
        ImageButton btn_unfollow;
        TextView tv_accept,tv_reject,tv_block;
        FrameLayout fl_parent;
        String file_id_;

        public MyViewHolder(View view, Context context) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.ns_adp_iv_thumb);
            iv_playIcon = (ImageView) view.findViewById(R.id.ns_adp_iv_playicon);
            title = (TextView) view.findViewById(R.id.ns_adp_tv_title);
            user_name = (TextView) view.findViewById(R.id.ns_adp_tv_username);
            date = (TextView) view.findViewById(R.id.ns_adp_tv_date);
            time = (TextView) view.findViewById(R.id.ns_adp_tv_time);
            rv_swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            swipe_Layout= (LinearLayout) view.findViewById(R.id.swipe_accept);
            swipe_follower= (RelativeLayout) view.findViewById(R.id.swipe_follower);
            btn_unfollow = (ImageButton) view.findViewById(R.id.btn_unfollow);
            tv_accept = (TextView) view.findViewById(R.id.text_accept);
            tv_reject = (TextView) view.findViewById(R.id.text_reject);
            tv_block = (TextView) view.findViewById(R.id.text_block);
            RL_item = (RelativeLayout) view.findViewById(R.id.rl_parent);
            fl_parent = (FrameLayout) view.findViewById(R.id.fl_parent);
            iv_mic_top_left = (ImageView) view.findViewById(R.id.ns_adp_iv_audio_mic);
        }
    }


    public AdapterAlertStream_norecycle(List<AlertStreamModelList> ListData, Context context, OnRecyclerListener recyclerListener) {
        this.ListData = ListData;
        this.context = context;
        this.recyclerListener =recyclerListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_stream, parent, false);

        return new MyViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AlertStreamModelList notificationStreamModelList = ListData.get(position);
        holder.title.setText(StringUtils.capitalize(notificationStreamModelList.getTitle()).trim());
        holder.user_name.setText(notificationStreamModelList.getSendername());
        String DateTime = notificationStreamModelList.getCreatedDate();
        String outTime="",OutDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MMM dd, yyyy");

        holder.setIsRecyclable(false);

        holder.rv_swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.rv_swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.rv_swipeLayout.findViewById(R.id.bottom_wrapper));

        if(notificationStreamModelList.getPrivacy().equalsIgnoreCase("Private")){
            holder.swipe_Layout.setVisibility(View.VISIBLE);
            holder.swipe_follower.setVisibility(View.GONE);
        }/*else if(notificationStreamModelList.getPrivacy().equalsIgnoreCase("Both")){
            holder.swipe_Layout.setVisibility(View.VISIBLE);
            holder.swipe_follower.setVisibility(View.GONE);
        }*/
        else{
            holder.swipe_follower.setVisibility(View.VISIBLE);
            holder.swipe_Layout.setVisibility(View.GONE);
        }

        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

        Boolean newuser = sharedpreferences.getBoolean(SharedPrefUtils.SpSlideViewusedOnce, true);
        if(newuser) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (position == 0) {
                        holder.rv_swipeLayout.open();
                    }
                }
            }, 10);
        }
        holder.RL_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(context, "cl", Toast.LENGTH_SHORT).show();

                recyclerListener.onItemClicked(position,"");
            }
        });
        holder.fl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerListener.onItemClicked(position,"");
            }
        });


        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onAccept(position, "");

            }
        });
        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onReject(position, "");

            }
        });
        holder.tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onBlock(position, "");

            }
        });
        holder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onUnfollow(position, "");

            }
        });

        holder.rv_swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.rv_swipeLayout.findViewById(R.id.bottom_wrapper));



        holder.rv_swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });






        try {
            Date date = dateFormat.parse(DateTime);

            outTime = dateFormat2.format(date);
            OutDate = dateFormat3.format(date);
        } catch (ParseException e) {
        }
        holder.date.setText(outTime);
        holder.time.setText(OutDate);

        String TBPath, tb= "";

        if(notificationStreamModelList.getFileMimeType().equalsIgnoreCase("video/mp4")){
            tb = notificationStreamModelList.getTBPath();
            if (tb.contains(StaticConfig.ROOT_URL_Media)) {
                TBPath = StaticConfig.ROOT_URL + tb.replace(StaticConfig.ROOT_URL_Media, "");

                Glide.with(context).load(TBPath).bitmapTransform( new CenterCrop(context),new RoundedCornersTransformation(context,7,0))
                        .into(holder.iv_thumb);


            } else {
                TBPath = StaticConfig.ROOT_URL + "/" + tb;
                Glide.with(context).load(TBPath).bitmapTransform( new CenterCrop(context),new RoundedCornersTransformation(context,7,0))
                        .into(holder.iv_thumb);
            }
            holder.iv_playIcon.setVisibility(View.VISIBLE);

        }else {

            String profile_pic_path = notificationStreamModelList.getProfile_pic();
            holder.iv_mic_top_left.setVisibility(View.VISIBLE);

            if (profile_pic_path.contains(StaticConfig.ROOT_URL_Media)) {
                TBPath = StaticConfig.ROOT_URL + profile_pic_path.replace(StaticConfig.ROOT_URL_Media, "");

                Glide.with(context).load(TBPath).bitmapTransform( new CenterCrop(context),new RoundedCornersTransformation(context,7,0))
                        .into(holder.iv_thumb);


            } else {
                TBPath = StaticConfig.ROOT_URL + "/" + profile_pic_path;
                Glide.with(context).load(TBPath).bitmapTransform( new CenterCrop(context),new RoundedCornersTransformation(context,7,0))
                        .into(holder.iv_thumb);
            }

         /*
            Glide.with(context).load(R.drawable.ic_mic_placeholder).bitmapTransform( new CenterCrop(context),new RoundedCornersTransformation(context,7,0))
                    .into(holder.iv_thumb);*/
          //  Glide.with(context).load(R.drawable.ic_mic_placeholder).fitCenter().into(holder.iv_thumb);

             holder.iv_playIcon.setVisibility(View.GONE);


        }

    }

    public interface OnRecyclerListener {
        void onItemClicked(int position, String data);
        void onUnfollow(int position, String data);
        void onAccept(int position, String data);
        void onReject(int position, String data);
        void onBlock(int position, String data);
    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

}

