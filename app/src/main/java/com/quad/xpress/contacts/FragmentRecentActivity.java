package com.quad.xpress.contacts;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.quad.xpress.AudioRecordActivity;
import com.quad.xpress.CameraRecordActivity;
import com.quad.xpress.R;

/**
 * Created by suresh on 3/4/17.
 */
public class FragmentRecentActivity extends Fragment implements
        View.OnClickListener, ContactRecentAdapter.RecentonRecyclerListener{

    private RecyclerView recyclerView_ixpress;
    ContactRecentAdapter mAdapter;
    private int itemPosition, recentPosition;
    ImageButton audio_Button, video_Button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_ix_contacts, container, false);

        recyclerView_ixpress = (RecyclerView) v.findViewById(R.id.rv_contact_list);
        recyclerView_ixpress.setLayoutManager(new LinearLayoutManager(ContactMainActivity.mContactMainActivity));
        recyclerView_ixpress.setItemAnimator(new DefaultItemAnimator());

        SearchView mSearch = (SearchView) v.findViewById(R.id.search);
        mSearch.setVisibility(View.GONE);



        mAdapter = new ContactRecentAdapter(FragmentRecentActivity.this,
                FragmentRecentActivity.this);
        recyclerView_ixpress.setAdapter(mAdapter);


        return v;

    }

    @Override
    public void recentSetOnItemClick(View itemView) {
        itemPosition = recyclerView_ixpress.getChildLayoutPosition(itemView);
        if(itemPosition != -1){
            alert();
            recentPosition = 1;

            /* String item = appendEmail.get(itemPosition);
            Toast.makeText(ContactMainActivity.this, item, Toast.LENGTH_LONG).show();*/

        }else{
        }
    }

    public void alert() {

        Dialog SendDiscardDialog = new Dialog(getActivity(),
                R.style.Theme_Transparent);
        SendDiscardDialog.setContentView(R.layout.send_discard_dialog);
        SendDiscardDialog.show();

        audio_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.audio_alert);
        video_Button = (ImageButton) SendDiscardDialog.findViewById(R.id.video_alert);

        audio_Button.setOnClickListener(this);
        video_Button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (audio_Button == view) {
            Intent i = new Intent(getActivity(), AudioRecordActivity.class);
            i.putExtra("positionEmailID", itemPosition);
            i.putExtra("ixpresspositionEmailID", recentPosition);
            // DatabaseHandler.dbEmailList.add(0, DatabaseHandler.dbEmailList.get(itemPosition));
            // String ixUserName = appendName.get(itemPosition);
            startActivity(i);
        } else if (video_Button == view) {
            Intent i = new Intent(getActivity(), CameraRecordActivity.class);
            i.putExtra("positionEmailID", itemPosition);
            i.putExtra("ixpresspositionEmailID", recentPosition);
            startActivity(i);
        }
    }
}
