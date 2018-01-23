package com.quad.xpress.contacts;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quad.xpress.AudioRecordActivity;
import com.quad.xpress.CameraRecordActivity;
import com.quad.xpress.R;
import com.quad.xpress.utills.StaticConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by suresh on 28/3/17.
 */
public class ContactRecentAdapter extends RecyclerView.Adapter<ContactRecentAdapter.MyViewHolder> {

    private Contact mContact;

    private FragmentRecentActivity mContext;
    private Dialog SendDiscardDialog;
    private RecentonRecyclerListener mOnRecyclerListener;


    public ContactRecentAdapter(FragmentRecentActivity recycler, FragmentRecentActivity con) {
       // this.mContact = contact;
        this.mContext = con;
        this.mOnRecyclerListener = recycler;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mLabel, mEmail;
        CircleImageView mImage;
        ImageButton mImageButton;
        private ImageButton audio_Button, video_Button;
        private ArrayList<String> contacts;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                    String item = mList.get(itemPosition);
                    Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();*/
                    mOnRecyclerListener.recentSetOnItemClick(v);

                }
            });
            mImage = (CircleImageView) itemView.findViewById(R.id.rounded_iv_profile);
            mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            mEmail = (TextView) itemView.findViewById(R.id.tv_email);
            mImageButton = (ImageButton) itemView.findViewById(R.id.contact_right_button);
           /* mImageButton = (ImageButton) itemView.findViewById(R.id.contact_right_button);

            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecyclerListener.recentSetOnItemClick(v);
                }
            });*/

            /*SendDiscardDialog = new Dialog(mContext,R.style.Theme_Transparent);
            SendDiscardDialog.setContentView(R.layout.send_discard_dialog);

            audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
            video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

            audio_Button.setOnClickListener(this);
            video_Button.setOnClickListener(this);*/

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //view.fo

                    //alert();
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (audio_Button == view) {
                Intent i = new Intent(mContext.getActivity(), AudioRecordActivity.class);
                //i.putExtra("emailID", mBoundContact.emailID);
                mContext.startActivity(i);
            } else if (video_Button == view) {
                Intent i = new Intent(mContext.getActivity(), CameraRecordActivity.class);
                // i.putExtra("emailID", mBoundContact.emailID);
                mContext.startActivity(i);
            }
        }
    }

    private void alert() {
        SendDiscardDialog.show();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);
        mOnRecyclerListener.recentSetOnItemClick(itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String imgUrl = null;
        holder.mLabel.setText(DatabaseHandler.dbNameList.get(position));
        holder.mEmail.setText(DatabaseHandler.dbEmailList.get(position));

        try {
            imgUrl = DatabaseHandler.dbPicList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            imgUrl=null;
        }

        if(imgUrl!= null){
            if (imgUrl.contains(StaticConfig.ROOT_URL_Media)) {
                imgUrl = StaticConfig.ROOT_URL + imgUrl.replace(StaticConfig.ROOT_URL_Media, "");
                Picasso.with(mContext.getActivity()).load(imgUrl)
                        .resize(300,300)
                        .centerCrop()
                        .into(holder.mImage);
            }else if(imgUrl.contains("https")){
                Picasso.with(mContext.getActivity()).load(imgUrl)
                        .resize(300,300)
                        .centerCrop()
                        .into(holder.mImage);

            } else {
                // imgUrl = StaticConfig.ROOT_URL + "/" +imgUrl;
            }

        }else {
            Picasso.with(mContext.getActivity()).load(R.mipmap.ic_user_icon)
                    .resize(300,300)
                    .centerCrop()
                    .into(holder.mImage);
        }

        /*holder.mLabel.setText(DatabaseHandler.dbNameList.get(position));
        holder.mEmail.setText(DatabaseHandler.dbEmailList.get(position));
        Picasso.with(mContext).load(DatabaseHandler.dbPicList.get(position))
                .resize(300, 300)
                .centerCrop()
                .placeholder(R.mipmap.ic_user_icon)
                .error(R.mipmap.ic_user_icon)
                .into(holder.mImage);
*/
       holder.mImageButton.setBackgroundResource(R.drawable.ic_heart_white_liked);
    }

    @Override
    public int getItemCount() {
        return DatabaseHandler.dbEmailList.size();
    }

    public interface RecentonRecyclerListener{

        void recentSetOnItemClick(View itemView);
    }
}
