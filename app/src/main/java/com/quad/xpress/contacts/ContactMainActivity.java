package com.quad.xpress.contacts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.quad.xpress.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctl on 13/3/17.
 */

public class ContactMainActivity extends AppCompatActivity   {


    private ImageButton btn_back_tb;

    TabLayout tabLayout;

    ViewPager mViewPager;

    public static ContactMainActivity mContactMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);


        mContactMainActivity = ContactMainActivity.this;


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


    }

    private void setUpViewPager(ViewPager mViewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentXpressActivity(), "iXprez");
        adapter.addFragment(new FragmentRecentActivity(), "Recent");
        mViewPager.setAdapter(adapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
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