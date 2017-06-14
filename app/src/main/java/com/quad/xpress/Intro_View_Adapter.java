package com.quad.xpress;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ctl on 18/1/17.
 */

public class Intro_View_Adapter extends PagerAdapter {


    String[] strText1, strText2,strText3;
    int[] images;
    Context mContext;
    LayoutInflater mLayoutInflater;
    Button btn_done;



    public Intro_View_Adapter(IntroActivity introActivity,
                              int[] images, String[] text, String[] text2,String[] text3) {

        this.images = images;
        this.strText1 = text;
        this.strText2 = text2;
        this.strText3= text3;
        mContext = introActivity;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView txt1, txt2,txt3;
        ImageView image;

        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = mLayoutInflater.inflate(R.layout.intro_v_pager, container,
                false);

        btn_done = (Button) itemView.findViewById(R.id.into_vp_btn_done);
        txt1 = (TextView) itemView.findViewById(R.id.intro_text);
        txt2 = (TextView) itemView.findViewById(R.id.intro_text2);
        txt3 = (TextView) itemView.findViewById(R.id.intro_text3);
        txt1.setText(strText1[position]);
        txt2.setText(strText2[position]);
        txt3.setText(strText3[position]);

        image = (ImageView) itemView.findViewById(R.id.intro_logo);
        image.setImageResource(images[position]);
        if(position==2){
          //  btn_done.setVisibility(View.VISIBLE);
        }
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Tag_act = new Intent(mContext, DashBoard.class);
                v.getContext().startActivity(Tag_act);
            }
        });

        ((ViewPager) container).addView(itemView);


        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
