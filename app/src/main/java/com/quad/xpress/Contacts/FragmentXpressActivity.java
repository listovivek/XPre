package com.quad.xpress.Contacts;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.quad.xpress.AudioRecordActivity;
import com.quad.xpress.CameraRecordActivity;
import com.quad.xpress.R;

import java.util.List;

/**
 * Created by suresh on 3/4/17.
 */
public class FragmentXpressActivity extends Fragment implements
        ContactRecycleAdapter.onRecyclerListener, View.OnClickListener{


    private RecyclerView recyclerView_ixpress;
    SearchView searchView;
    View v;
    private int itemPosition, recentPosition;
    ContactRecycleAdapter mAdapter;
    ImageButton audio_Button, video_Button;
    String tempEmail, tempName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ix_contacts, container, false);

        recyclerView_ixpress = (RecyclerView) v.findViewById(R.id.rv_contact_list);
        recyclerView_ixpress.setLayoutManager(new LinearLayoutManager(ContactMainActivity.mContactMainActivity));
        recyclerView_ixpress.setItemAnimator(new DefaultItemAnimator());

        searchView = (SearchView) v.findViewById(R.id.search);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("Find and xprez");
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white_transparent));



        mAdapter = new ContactRecycleAdapter(Contact.getInstance(), FragmentXpressActivity.this,
                FragmentXpressActivity.this);
        recyclerView_ixpress.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return false;
            }
        });


            return v;

    }

    @Override
    public void setOnItemClick(View itemView, List<String> mData, List<String> mDataName) {

        itemPosition = recyclerView_ixpress.getChildLayoutPosition(itemView);

        if(itemPosition != -1){
           alert();
            tempEmail = mData.get(itemPosition);
            tempName = mDataName.get(itemPosition);
            recentPosition = 0;
          //  Toast.makeText(ContactMainActivity.mContactMainActivity, ""+itemPosition, Toast.LENGTH_LONG).show();
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
            i.putExtra("tempEmail", tempEmail);
            i.putExtra("tempName", tempName);
            // DatabaseHandler.dbEmailList.add(0, DatabaseHandler.dbEmailList.get(itemPosition));
            // String ixUserName = appendName.get(itemPosition);
            startActivity(i);
        } else if (video_Button == view) {
            Intent i = new Intent(getActivity(), CameraRecordActivity.class);
            i.putExtra("positionEmailID", itemPosition);
            i.putExtra("ixpresspositionEmailID", recentPosition);
            i.putExtra("tempEmail", tempEmail);
            i.putExtra("tempName", tempName);
            startActivity(i);
        }
    }
}
