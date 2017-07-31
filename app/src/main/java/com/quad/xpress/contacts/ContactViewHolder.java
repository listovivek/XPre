package com.quad.xpress.contacts;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quad.xpress.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Contains a Contact List Item
 */
public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private CircleImageView mImage;
    private TextView mLabel;
    private TextView mEmail;
    private Contact mBoundContact; // Can be null
    private ImageButton mImageButton;
    private Cursor mCursor;
    ArrayList<Integer> list = new ArrayList<>();
    private ImageButton audio_Button, video_Button;

    public ContactViewHolder(final View itemView, Cursor c) {
        super(itemView);
        mCursor = c;
        mImage = (CircleImageView) itemView.findViewById(R.id.rounded_iv_profile);
        mLabel = (TextView) itemView.findViewById(R.id.tv_label);
        mEmail = (TextView) itemView.findViewById(R.id.tv_email);
        mImageButton = (ImageButton) itemView.findViewById(R.id.contact_right_button);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int position = getAdapterPosition();
                list.add(position);
                mImageButton.setImageResource(R.drawable.ic_check);
                mImageButton.setTag(position);
                    if(list.get(position) == mImageButton.getTag()){
                        mImageButton.setImageResource(R.drawable.ic_plus);
                    }else{
                        mImageButton.setImageResource(R.drawable.ic_check);
                    }*/

                //alert();
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBoundContact != null) {
                }
            }
        });
    }

    private void alert() {

       /* Dialog SendDiscardDialog = new Dialog(ContactsRecycleviewActivity.mContext,R.style.Theme_Transparent);
        SendDiscardDialog.setContentView(R.layout.send_discard_dialog);

        audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
        video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

        audio_Button.setOnClickListener(this);
        video_Button.setOnClickListener(this);

        SendDiscardDialog.show();*/
    }

    public void bind(Contact contact, int pos) {

       // if(contact != null){
            mBoundContact = contact;
            mLabel.setText(contact.name);
           // mEmail.setText(contact.emailID);
            Picasso.with(itemView.getContext()) .load(contact.profilePic)
                    .resize(50, 50)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_user_icon)
                    .error(R.mipmap.ic_user_icon)
                    .into(mImage);
        /*}else {
            mBoundContact = contact;
            mLabel.setText(contact.name);
            Picasso.with(itemView.getContext()) .load(contact.profilePic)
                    .resize(50, 50)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_user_icon)
                    .error(R.mipmap.ic_user_icon)
                    .into(mImage);
        }*/
    }

    @Override
    public void onClick(View view) {

        /*if(audio_Button == view){
            Intent i = new Intent(ContactsRecycleviewActivity.mContext, AudioRecordActivity.class);
            //i.putExtra("emailID", mBoundContact.emailID);
            ContactsRecycleviewActivity.mContext.startActivity(i);
        }else if(video_Button == view){
            Intent i = new Intent(ContactsRecycleviewActivity.mContext, CameraDemoActivity.class);
           // i.putExtra("emailID", mBoundContact.emailID);
            ContactsRecycleviewActivity.mContext.startActivity(i);
        }*/
    }
}
