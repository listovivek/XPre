package com.quad.xpress.uploadsFragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quad.xpress.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kural on 8/30/17.
 */

public class act_my_uploads extends FragmentActivity {


    TabLayout tableLayout;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ViewPagerAdapter vp_frga_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuploads_act);

        TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_title.setText("My uploads");
        ImageButton btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.myupl_viewpager);
        tableLayout = findViewById(R.id.act_my_upl_tabLayout);

        vp_frga_adapter = new ViewPagerAdapter(getSupportFragmentManager());

        vp_frga_adapter.addFragment(new public_uploads_frgamnet(), "Public");
        vp_frga_adapter.addFragment(new private_uploads_frgamnet(), "Private");

        mPager.setAdapter(vp_frga_adapter);
        tableLayout.setupWithViewPager(mPager);





    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {

            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
