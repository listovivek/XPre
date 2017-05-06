package com.quad.xpress.models.NotificationStream;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.StaticConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterNotificationStream extends RecyclerView.Adapter<AdapterNotificationStream.MyViewHolder> {

    private List<NotificationStreamModelList> ListData;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, user_name, time,date;
        ImageView iv_thumb;
        public SwipeLayout rv_swipeLayout;
        private LinearLayout swipe_Layout;
        RelativeLayout swipe_follower;

        public MyViewHolder(View view, Context context) {
            super(view);
            iv_thumb = (ImageView) view.findViewById(R.id.ns_adp_iv_thumb);
            title = (TextView) view.findViewById(R.id.ns_adp_tv_title);
            user_name = (TextView) view.findViewById(R.id.ns_adp_tv_username);
            date = (TextView) view.findViewById(R.id.ns_adp_tv_date);
            time = (TextView) view.findViewById(R.id.ns_adp_tv_time);
            rv_swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            swipe_Layout= (LinearLayout) view.findViewById(R.id.swipe_accept);
            swipe_follower= (RelativeLayout) view.findViewById(R.id.swipe_follower);
        }
    }


    public AdapterNotificationStream(List<NotificationStreamModelList> ListData, Context context) {
        this.ListData = ListData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_stream, parent, false);

        return new MyViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NotificationStreamModelList notificationStreamModelList = ListData.get(position);
        holder.title.setText(notificationStreamModelList.getTitle());
        holder.user_name.setText(notificationStreamModelList.getSendername());
        String DateTime = notificationStreamModelList.getCreatedDate();
        String outTime="",OutDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MMM dd, yyyy");


        holder.rv_swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.rv_swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.rv_swipeLayout.findViewById(R.id.bottom_wrapper));

        if(position == 0){
            holder.swipe_Layout.setVisibility(View.GONE);
            holder.swipe_follower.setVisibility(View.VISIBLE);
        }else if(position == 1){
            holder.swipe_Layout.setVisibility(View.VISIBLE);
            holder.swipe_follower.setVisibility(View.GONE);
        }else if(position == 2){
            holder.swipe_Layout.setVisibility(View.VISIBLE);
            holder.swipe_follower.setVisibility(View.GONE);
        }else if(position == 3){
            holder.swipe_Layout.setVisibility(View.GONE);
            holder.swipe_follower.setVisibility(View.VISIBLE);
        }


        holder.rv_swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.rv_swipeLayout.findViewById(R.id.bottom_wrapper));



        holder.rv_swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerListener.onItemClicked(position);
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

        String TBPath, tb = notificationStreamModelList.getTBPath();

      //  Toast.makeText(context, ""+out, Toast.LENGTH_SHORT).show();

        if (tb.contains(StaticConfig.ROOT_URL_Media)) {
            TBPath = StaticConfig.ROOT_URL + tb.replace(StaticConfig.ROOT_URL_Media, "");
        } else {
            TBPath = StaticConfig.ROOT_URL + "/" + tb;
        }
        Glide.with(context).load(TBPath).placeholder(R.drawable.ic_user_icon).fitCenter().into(holder.iv_thumb);

    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

}

