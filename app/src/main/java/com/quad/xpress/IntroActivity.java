package com.quad.xpress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.quad.xpress.Utills.StatiConstants;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by ctl on 18/1/17.
 */

public class IntroActivity extends AppCompatActivity {

    int[] images;
    String[] text1, text2,text3;
    ImageButton btn_done;

    Button btn_Proceed;
    ViewPager viewPager;
    int pos;




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_viewpager);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        btn_done = (ImageButton) findViewById(R.id.intro_btn_done);
        btn_Proceed = (Button) findViewById(R.id.intro_btn_done_final);
        images = new int[]{R.drawable.intro_heart, R.drawable.intro_video, R.drawable.intro_audio};
        text1 = new String[]{"HOLA!\nWELCOME", "HOLA!\nWELCOME", "HOLA!\nWELCOME"};
        text2 = new String[]{"Connect With Your Friends.","Share Your Video & Sound Bytes With",
                "Give a Shout Out to Your Loved Ones."};
        text3 = new String[]{"Or Foes.","the World.","Literally."};


        viewPager = (ViewPager) findViewById(R.id.pager);
        Intro_View_Adapter adapter = new Intro_View_Adapter(IntroActivity.this, images, text1, text2,text3);
        viewPager.setAdapter(adapter);




        final CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                if (position < 2){


                }else {
                    btn_done.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if(pos == 2){
                    pos++;


                }else if(pos == 3){


                }

            }
        });

        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatiConstants.newSignIn = true;
                Intent intent_next=new Intent(IntroActivity.this, DashBoard.class);

                finish();
                startActivity(intent_next);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatiConstants.newSignIn = true;
                Intent intent_next=new Intent(IntroActivity.this, DashBoard.class);

                finish();
                startActivity(intent_next);
            }
        });

        if(StatiConstants.newSignIn) {


            Toast.makeText(IntroActivity.this, "cc t", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!StatiConstants.newSignIn){

        }else {
            finish();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatiConstants.newSignIn = true;
    }
}
