package com.quad.xpress;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.quad.xpress.contacts.Contact;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.helpers.StaticConfig;
import com.quad.xpress.models.receivedFiles.PlayListitems;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PrivatePlayListAdapter extends RecyclerSwipeAdapter<PrivatePlayListAdapter.MyViewHolder> {

    private List<PlayListitems> rvListitems;
    Context _context;
    public OnRecyclerListener recyclerListener;
    String file_id_;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView RvVideoTitle,tv_accept,tv_reject,tv_block;
        public TextView RvSender;
        public TextView RvTags;
        //public TextView RvTime;
        public ImageView RvImage,iv_plyIcon, iv_mic_placeholder;
        public LinearLayout rv_menu_button_ll;
        public SwipeLayout rv_swipeLayout;
        public TextView date, time;
        public int position;
        public String data = null;
        public ImageButton button = null;
        public PopupWindow popupWindow = null;
        private OnRecyclerListener recyclerListener = null;
        RelativeLayout rl_foreground;


        public MyViewHolder(View view, Context context, OnRecyclerListener recyclerListeners) {
            super(view);
            this.recyclerListener = recyclerListeners;
            rl_foreground = (RelativeLayout) itemView.findViewById(R.id.pvt_relative_foreground);
            rv_menu_button_ll = (LinearLayout) itemView.findViewById(R.id.rv_menu_button_ll);
            button = (ImageButton) itemView.findViewById(R.id.menu_button);
            RvImage = (ImageView) itemView.findViewById(R.id.RvImage);
            iv_plyIcon = (ImageView) itemView.findViewById(R.id.RvImage_overlay);
            iv_mic_placeholder = (ImageView) itemView.findViewById(R.id.RvImage_overlay_audio);
            RvVideoTitle = (TextView) itemView.findViewById(R.id.RvVideoTitle);
            RvSender = (TextView) itemView.findViewById(R.id.RvSender);
            RvTags = (TextView) itemView.findViewById(R.id.RvTags);
            //RvTime = (TextView) itemView.findViewById(R.id.RvTime);
            tv_accept = (TextView) itemView.findViewById(R.id.text_accept);

            tv_reject = (TextView) itemView.findViewById(R.id.text_reject);
            tv_block = (TextView) itemView.findViewById(R.id.text_block);
            date = (TextView) view.findViewById(R.id.ns_adp_tv_date);
            time = (TextView) view.findViewById(R.id.ns_adp_tv_time);
           rv_swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            popupWindow = new PopupWindow(context);

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
            button.setOnClickListener(this);

                rv_menu_button_ll.setVisibility(LinearLayout.GONE);






        }

        @Override
        public void onClick(View v) {
            if(PrivatePlayListActivity.RvDisabler){

            }
            else{
                if (v != null) {
                    if (v.getId() == R.id.menu_button) {
                        popupWindow.showAsDropDown(v, v.getWidth(), -v.getHeight());
                    } else {
                        recyclerListener.onMenuItemClicked(position, data, v.getId());
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }
                }
            }

        }
    }


    public PrivatePlayListAdapter(List<PlayListitems> rvListitems, Context _context, OnRecyclerListener recyclerListener) {
        this.rvListitems = rvListitems;
        this._context = _context;
        this.recyclerListener = recyclerListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receive_video_lists, parent, false);

        return new MyViewHolder(itemView, _context, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PlayListitems list = rvListitems.get(position);
        holder.RvVideoTitle.setText(StringUtils.capitalize(list.getTitle().trim()));
        holder.RvSender.setText(list.getFromEmail());
        //holder.RvTime.setText(list.getCreatedDate());

        String DateTime = list.getCreatedDate();
        String outTime="",OutDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MMM dd, yyyy");


        holder.rv_swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);





        holder.rv_swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.rv_swipeLayout.findViewById(R.id.bottom_wrapper));

        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

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


        holder.rv_swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onItemClicked(position);
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


        try {
            if (!list.getTags().equals("")) {
                holder.RvTags.setVisibility(View.INVISIBLE);
                holder.RvTags.setText(list.getTags());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {


            String TBPath;

            if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
            }
         else if  (list.getTBPath().contains("https")){
            TBPath = list.getTBPath();
        }else {
                TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
            }



            if (list.getFileMimeType().equals("audio/mp3")) {

                Glide.with(_context).load(R.drawable.ic_mic_placeholder)
                        .into(holder.RvImage);



                if(TBPath!= null){

                    Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,7,0))
                            .into(holder.RvImage);
                    holder.iv_mic_placeholder.setVisibility(View.VISIBLE);
                }


            } else if (list.getFileMimeType().equals("video/mp4")) {




                Glide.with(_context).load(TBPath).bitmapTransform( new CenterCrop(_context),new RoundedCornersTransformation(_context,7,0))
                        .into(holder.RvImage);
                holder.iv_plyIcon.setVisibility(View.VISIBLE);

            }



        } catch (Exception e) {
            Log.v("Exception ", "Exception " + e);

        }



        if(!Contact.getInstance().ixpressemail.contains(list.from_email)){
            holder.rl_foreground.setBackgroundResource(R.drawable.private_recycle_curve_spammer);
            holder.RvImage.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        }
        else if(Contact.getInstance().email_list.isEmpty()){
            holder.rl_foreground.setBackgroundResource(R.drawable.curved_dark);
            holder.RvImage.clearColorFilter();
        }
        else {
            holder.rl_foreground.setBackgroundResource(R.drawable.curved_dark);
            holder.RvImage.clearColorFilter();
        }

        holder.position = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListener.onItemClicked(position);

            }
        });

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_id_=list.getFileID();
                recyclerListener.onAccept(position, file_id_);
            }
        });
        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_id_=list.getFileID();
                recyclerListener.onReject(position, file_id_);
            }
        });
        holder.tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean IsnewContact = false;
                if(!Contact.getInstance().email_list.contains(list.from_email)){
                    IsnewContact = true;
                }
                file_id_=list.getFromEmail();
                recyclerListener.onBlock(position, file_id_,IsnewContact);

            }
        });

    }

    @Override
    public int getItemCount() {
        return rvListitems.size();
    }

    public interface OnRecyclerListener {
        void onItemClicked(int position);

        // void onItemClicked(int position, String data);
        void onMenuItemClicked(int position, String data, int menuId);

        void onAccept(int position, String data);
        void onReject(int position, String data);
        void onBlock(int position, String data, Boolean isnewContact);
    }
}

