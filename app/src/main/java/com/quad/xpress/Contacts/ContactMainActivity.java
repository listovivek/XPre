package com.quad.xpress.Contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quad.xpress.AudioRecordActivity;
import com.quad.xpress.CameraRecordActivity;
import com.quad.xpress.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctl on 13/3/17.
 */

public class ContactMainActivity extends AppCompatActivity implements View.OnClickListener
        {

    private RecyclerView recyclerView_ixpress;

    private int mNameColIdx, mIdColIdx;
    private TextView tv_tb_title;
    private ImageButton btn_back_tb, audio_Button, video_Button;
    private Cursor cur;

    public static String finalEmail;
    private int itemPosition, recentPosition;

    TabLayout tabLayout;
    SearchView searchView;
    ContactRecycleAdapter mAdapter;
    ViewPager mViewPager;

    public static ContactMainActivity mContactMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);


        mContactMainActivity = ContactMainActivity.this;


        /*recyclerView_ixpress = (RecyclerView) findViewById(R.id.rv_contact_list);
        recyclerView_ixpress.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_ixpress.setItemAnimator(new DefaultItemAnimator());

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("Search your favorite user");*/


        /*tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        btn_back_tb = (ImageButton) findViewById(R.id.tb_normal_back);
        tv_tb_title.setText("Contacts");


        btn_back_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        btn_back_tb = (ImageButton) findViewById(R.id.btn_back_contacts);
        btn_back_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mViewPager = (ViewPager) findViewById(R.id.contact_viewpager);
        setUpViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_private);
        tabLayout.setupWithViewPager(mViewPager);
        /*tabLayout.addTab(tabLayout.newTab().setText("Ixpress"));
        tabLayout.addTab(tabLayout.newTab().setText("Recent"));*/



        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){
                    searchView.setVisibility(View.VISIBLE);
                    recyclerAdpater();

                }else{
                    searchView.setVisibility(View.GONE);
                    if(DatabaseHandler.dbEmailList!=null &&
                            DatabaseHandler.dbEmailList.size()>0) {
                        *//*recyclerview_ix_recent.setVisibility(View.VISIBLE);
                        TextView recentView = (TextView) findViewById(R.id.textview_recent);
                        recentView.setVisibility(View.VISIBLE);*//*

                        ContactRecentAdapter adapter = new ContactRecentAdapter(ContactMainActivity.this,
                                ContactMainActivity.this);
                        recyclerView_ixpress.setAdapter(adapter);
                    }else{
                        recyclerView_ixpress.setAdapter(null);
                        Toast.makeText(ContactMainActivity.this, "No recent record found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/


        /*recyclerView_ixpress.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());
                int p = rv.getChildAdapterPosition(ChildView);

                // finalEmail = appendEmail.get(p);
                Log.d("", finalEmail);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
        //recyclerAdpater();

    }

    private void setUpViewPager(ViewPager mViewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentXpressActivity(), "Ixprez");
        adapter.addFragment(new FragmentRecentActivity(), "Recent");
        mViewPager.setAdapter(adapter);

    }

   /* private void recyclerAdpater() {

        *//*mAdapter = new
                ContactRecycleAdapter(Contact.getInstance(), ContactMainActivity.this,
                ContactMainActivity.this);*//*
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
    }
*/
    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }


    /*@Override
    public void setOnItemClick(View itemView) {
        itemPosition = FragmentXpressActivity.recyclerView_ixpress.getChildLayoutPosition(itemView);
        if(itemPosition != -1){
            alert();
            recentPosition = 0;
           *//* String item = appendEmail.get(itemPosition);
            Toast.makeText(ContactMainActivity.this, item, Toast.LENGTH_LONG).show();*//*
        }else{
        }
    }*/


    /*@Override
    public void recentSetOnItemClick(View itemView) {
        itemPosition = recyclerView_ixpress.getChildLayoutPosition(itemView);
        if(itemPosition != -1){
            alert();
            recentPosition = 1;

            *//* String item = appendEmail.get(itemPosition);
            Toast.makeText(ContactMainActivity.this, item, Toast.LENGTH_LONG).show();*//*

        }else{
        }
    }*/



    @Override
    public void onClick(View view) {
        if (audio_Button == view) {
            Intent i = new Intent(ContactMainActivity.this, AudioRecordActivity.class);
            i.putExtra("positionEmailID", itemPosition);
            i.putExtra("ixpresspositionEmailID", recentPosition);
           // DatabaseHandler.dbEmailList.add(0, DatabaseHandler.dbEmailList.get(itemPosition));
           // String ixUserName = appendName.get(itemPosition);
            startActivity(i);
        } else if (video_Button == view) {
            Intent i = new Intent(ContactMainActivity.this, CameraRecordActivity.class);
            i.putExtra("positionEmailID", itemPosition);
            i.putExtra("ixpresspositionEmailID", recentPosition);
            startActivity(i);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}