package com.quad.xpress.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quad.xpress.AudioRecordActivity;
import com.quad.xpress.CameraRecordActivity;
import com.quad.xpress.DashBoard;
import com.quad.xpress.R;
import com.quad.xpress.utills.StaticConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ctl on 13/3/17.
 */

public class ContactRecycleAdapter extends RecyclerView.Adapter<ContactRecycleAdapter.MyViewHolder>
        implements Filterable {

    private Contact mContact;
    private FragmentXpressActivity mContext;
    private onRecyclerListener mOnRecyclerListener;
    ValueFilter valueFilter;

    List<String> mFilterEmail;
    List<String> mFilterName;
    List<String> mFilterPic;
    Context context;
    List<String> mStringFilterList;
    List<Boolean>isiXprezuser;
    /*public ContactRecycleAdapter(Contact contact, onRecyclerListener recycler, Context con) {
        this.mData = contact.ixpressemail;
        this.mContact = contact;
        this.mStringFilterList = contact.ixpressemail;
        this.mContext = con;
        this.mOnRecyclerListener = recycler;
    }*/

    public ContactRecycleAdapter(Contact contact, FragmentXpressActivity
            recycler, FragmentXpressActivity con) {

        this.mFilterEmail = contact.ixpressemail;
        this.mContact = contact;
        this.mStringFilterList = contact.ixpressemail;
        this.mContext = con;
        this.mOnRecyclerListener = recycler;
        this.mFilterName = contact.ixpressname;
        this.mFilterPic = contact.ixpress_user_pic;
        this.isiXprezuser = contact.is_ixpress_user;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView mLabel, mEmail;
        CircleImageView mImage;
        ImageButton mImageButton;
        private ImageButton audio_Button, video_Button;
        private ArrayList<String> contacts;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = ContactMainActivity.mContactMainActivity;

            mImage = (CircleImageView) itemView.findViewById(R.id.rounded_iv_profile);
            mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            mEmail = (TextView) itemView.findViewById(R.id.tv_email);
            mImageButton = (ImageButton) itemView.findViewById(R.id.contact_right_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                    String item = mList.get(itemPosition);
                    Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();*/

                   // final int position = getListView().getPositionForView((LinearLayout)v.getParent());

                    mOnRecyclerListener.setOnItemClick(v, mFilterEmail, mFilterName);

                }
            });

         /*   mEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // mOnRecyclerListener.setOnItemClick(v, mFilterEmail, mFilterName);
                }
            });

            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            });*/

            /*SendDiscardDialog = new Dialog(mContext,R.style.Theme_Transparent);
            SendDiscardDialog.setContentView(R.layout.send_discard_dialog);

            audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
            video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

            audio_Button.setOnClickListener(this);
            video_Button.setOnClickListener(this);*/

            /*mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext.getActivity(), "click", Toast.LENGTH_SHORT).show();
                    mOnRecyclerListener.setOnItemClick(view, mData);
                    //view.fo

                    //alert();
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            if(audio_Button == view){
                Intent i = new Intent(mContext.getActivity(), AudioRecordActivity.class);
                //i.putExtra("emailID", mBoundContact.emailID);
                mContext.startActivity(i);
            }else if(video_Button == view){
                Intent i = new Intent(mContext.getActivity(), CameraRecordActivity.class);
                // i.putExtra("emailID", mBoundContact.emailID);
                mContext.startActivity(i);
            }
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);
        //mOnRecyclerListener.setOnItemClick(itemView, mData);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int count = DashBoard.ixprez_email.size();
        if(count > position){
            if(DashBoard.ixprez_email.get(position).equalsIgnoreCase(mFilterEmail.get(position))){

                holder.mLabel.setText(mFilterName.get(position));
                holder.mEmail.setText(mFilterEmail.get(position));
                String imgUrl = null;

                Picasso.with(mContext.getActivity()).load(R.mipmap.ic_user_icon)
                        .resize(300, 300)
                        .centerCrop()
                        .into(holder.mImage);

                try {
                    imgUrl = mFilterPic.get(position);
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

                    } else {
                        // imgUrl = StaticConfig.ROOT_URL + "/" +imgUrl;
                    }

                }else {

                    Picasso.with(mContext.getActivity()).load(R.mipmap.ic_user_icon)
                            .resize(300,300)
                            .centerCrop()
                            .into(holder.mImage);

                }

                holder.mImageButton.setBackgroundResource(R.drawable.ic_heart_white);
                Log.d("Pic URL", ""+mFilterPic.get(position));

            }else{
                holder.mLabel.setText(mFilterName.get(position));
                holder.mEmail.setText(mFilterEmail.get(position));

                Picasso.with(mContext.getActivity()).load(mFilterPic.get(position))
                        .resize(300, 300)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_user_icon)
                        .error(R.mipmap.ic_user_icon)
                        .into(holder.mImage);

                holder.mImageButton.setBackgroundResource(android.R.drawable.ic_dialog_email);
                //Log.d("call 1", "call 1");
            }
        }else{

           // String imgUrl = mFilterPic.get(position);

            holder.mLabel.setText(mFilterName.get(position));
            holder.mEmail.setText(mFilterEmail.get(position));

            Picasso.with(mContext.getActivity()).load(mFilterPic.get(position))
                    .resize(300, 300)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_user_icon)
                    .error(R.mipmap.ic_user_icon)
                    .into(holder.mImage);

            holder.mImageButton.setBackgroundResource(android.R.drawable.ic_dialog_email);

        }


        if(isiXprezuser.get(position)){
            holder.mImageButton.setBackgroundResource(R.drawable.ic_heart_white);
        }

    }

    @Override
    public int getItemCount() {
        return mFilterEmail.size();
    }

            public interface onRecyclerListener {

                void setOnItemClick(View itemView, List<String> mData, List<String> mDataName);
            }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }else{}
        return valueFilter;

    }

    private class ValueFilter extends Filter{
        ArrayList<String> pic = new ArrayList<>();
        ArrayList<Boolean> picx = new ArrayList<>();
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            HashMap<String, String> nn = new HashMap<>();

            if (constraint != null && constraint.length() > 0) {


                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        nn.put(mStringFilterList.get(i), mContact.ixpressname.get(i));
                        pic.add(mFilterPic.get(i));
                        picx.add(isiXprezuser.get(i));
                    }
                }

                results.count = nn.size();
                results.values = nn;
            } else {

                /*HashMap<String, String> nn = new HashMap<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        nn.put(mStringFilterList.get(i), mContact.ixpressname.get(i));
                    }
                }*/

                if(nn.size() == 0){

                }else{
                    results.count = nn.size();
                    results.values = nn;
                }
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.count > 0){

                HashMap<String, String> fill = (HashMap<String, String>) results.values;

                ArrayList<String> email = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();


                int i =0;
                for (Map.Entry<String,String> entry : fill.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue();
                    // do stuff
                    email.add(key);
                    name.add(value);

                    i++;
                }

                mFilterEmail = email;
                mFilterName = name;
                mFilterPic = pic;

                if(email.size()==0){

                    mFilterEmail.clear();
                    mFilterName .clear();
                    mFilterPic .clear();
                }



            }else{

                mFilterEmail = mStringFilterList;
                mFilterName = mContact.ixpressname;
                mFilterPic = mContact.ixpress_user_pic;




            }
            notifyDataSetChanged();

        }
    }

}
