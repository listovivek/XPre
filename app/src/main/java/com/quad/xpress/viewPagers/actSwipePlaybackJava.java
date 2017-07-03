package com.quad.xpress.viewPagers;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.quad.xpress.DashboardFragment_pop;
import com.quad.xpress.R;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.List;

/**
 * Created by kural on 29/6/17.
 */

public class actSwipePlaybackJava extends AppCompatActivity {
        AdapterSwipePlayback adapter;
        List<PlayListitems_emotion> listOfEmotions;
        Context context;
        TextView tv_tb_title;
        MediaPlayer mMediaPlayer;

    static Boolean like_clicked = false, isFromFeatured =false;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.act_swipe_playback);
            context = getApplicationContext();
            listOfEmotions = DashboardFragment_pop.playlist;
            ViewPager vp = (ViewPager) findViewById(R.id.asw_Viewpager);

            tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            vp.setOffscreenPageLimit(-1);
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                tv_tb_title.setText(listOfEmotions.get(position).getTitle());


                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }

                }
            });
            Intent getVurl = getIntent();

            try {
                isFromFeatured = getVurl.getBooleanExtra("fromfeatured",false);
            } catch (Exception e) {
                e.printStackTrace();
            }




            adapter = new AdapterSwipePlayback(listOfEmotions,context,actSwipePlaybackJava.this);
            vp.setAdapter(adapter);


        }




}
