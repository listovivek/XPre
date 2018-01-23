package com.quad.xpress.contacts;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.TreeMap;

/**
 * Created by suresh on 3/4/17.
 */
public class FragmentXpressActivity extends Fragment implements
         View.OnClickListener, ContactRecycleAdapter.onRecyclerListener {

    String fromscr = "new";
    private RecyclerView recyclerView_ixpress;
    SearchView searchView;
    View v;
    private int itemPosition, recentPosition;
//    ContactRecycleSearchAdapter mAdapter;
    ContactRecycleAdapter  mAdapter;
    ImageButton audio_Button, video_Button;
    String tempEmail, tempName;
    SwipeRefreshLayout Swr;
    Dialog SendDiscardDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Intent in = getActivity().getIntent();
        try {
            fromscr = in.getStringExtra("from");
        } catch (Exception e) {
            fromscr = "new";
        }


        v = inflater.inflate(R.layout.fragment_ix_contacts, container, false);


        recyclerView_ixpress = (RecyclerView) v.findViewById(R.id.rv_contact_list);
        recyclerView_ixpress.setLayoutManager(new LinearLayoutManager(ContactMainActivity.mContactMainActivity));
        recyclerView_ixpress.setItemAnimator(new DefaultItemAnimator());

        searchView = (SearchView) v.findViewById(R.id.search);
        Swr = v.findViewById(R.id.srl_contats);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("Find and xprez");
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white_transparent));








        Contact contactData = new Contact();
        FullContactsDBhandler fDB = new FullContactsDBhandler(getActivity());
        List<FullContactDBOBJ> Dbcontacts = fDB.getAllContacts();


        contactData.getInstance().ixpressemail.clear();
        contactData.getInstance().ixpressname.clear();
        contactData.getInstance().ixpress_user_pic.clear();
        contactData.getInstance().is_ixpress_user.clear();



        for (FullContactDBOBJ cn : Dbcontacts) {

            if(cn.get_ixprezuser().equalsIgnoreCase("true")){

                contactData.getInstance().ixpressemail.add(cn.getPhoneNumber());
                contactData.getInstance().ixpressname.add(cn.getName());
                contactData.getInstance().ixpress_user_pic.add(cn.get_profile_pic());
                contactData.getInstance().is_ixpress_user.add(true);
            }

        }


        TreeMap<String,String> map = new TreeMap<>();

        for (FullContactDBOBJ cn : Dbcontacts) {
            if(cn.get_diplayed().equalsIgnoreCase("yes")){

                map.put(cn.getName(),cn.getPhoneNumber());

            }


        }

   /*     List<FullContactDBOBJ> Dbcontactsx = fDB.getAllContactsToDisplay();
        for (FullContactDBOBJ cn : Dbcontactsx) {

                map.put(cn.getName(),cn.getPhoneNumber());


            Toast.makeText(getActivity(), ""+cn.get_diplayed()+cn.getPhoneNumber(), Toast.LENGTH_SHORT).show();


        }*/


        contactData.getInstance().ixpressemail.addAll(map.values());
        contactData.getInstance().ixpressname.addAll(map.keySet());

        for (int i = 0; i < map.size(); i++) {
            contactData.getInstance().ixpress_user_pic.add(String.valueOf(R.drawable.ic_user_icon));
            contactData.getInstance().is_ixpress_user.add(false);
        }
      /*  mAdapter = new ContactRecycleSearchAdapter(contactData.getInstance(), FragmentXpressActivity.this,
                FragmentXpressActivity.this);
*/
        mAdapter = new ContactRecycleAdapter(contactData.getInstance(),FragmentXpressActivity.this,FragmentXpressActivity.this);

        recyclerView_ixpress.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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
        Swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                Swr.setRefreshing(false);
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


            try {
                if(fromscr.equalsIgnoreCase("camera")){
                    SendDiscardDialog.dismiss();
                    Intent i = new Intent(getActivity(), CameraRecordActivity.class);
                    i.putExtra("positionEmailID", itemPosition);
                    i.putExtra("ixpresspositionEmailID", recentPosition);
                    i.putExtra("tempEmail", tempEmail);
                    i.putExtra("tempName", tempName);
                    startActivity(i);
                }
                else if(fromscr.equalsIgnoreCase("audio"))
                {
                    SendDiscardDialog.dismiss();
                    Intent i = new Intent(getActivity(), AudioRecordActivity.class);
                    i.putExtra("positionEmailID", itemPosition);
                    i.putExtra("ixpresspositionEmailID", recentPosition);
                    i.putExtra("tempEmail", tempEmail);
                    i.putExtra("tempName", tempName);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
        }
    }

    public void alert() {

         SendDiscardDialog = new Dialog(getActivity(),
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
