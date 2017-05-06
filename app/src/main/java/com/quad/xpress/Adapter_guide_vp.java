package com.quad.xpress;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_guide_vp extends PagerAdapter {
	int size;
	Activity act;
	View layout;
	TextView pagenumber1,pagenumber2,pagenumber3,pagenumber4,pagenumber5,tv_title,tv_description;
	ImageView pageImage;
	VideoView videoView;
	Button click;
	Context context;
    ArrayList<String>title_gv = new ArrayList<>();
    ArrayList<String>description_gv = new ArrayList<>();
    ArrayList<String>img_url_gv = new ArrayList<>();
	ArrayList<String>type_gv = new ArrayList<>();

	public Adapter_guide_vp(DashBoard dashBoard, int noofsizeguide, ArrayList<String>title, ArrayList<String>description, ArrayList<String>img_url, ArrayList<String>type) {
		// TODO Auto-generated constructor stub
		size = noofsizeguide;
		act = dashBoard;
        title_gv = title;
        description_gv =description;
        img_url_gv=img_url;
		type_gv=type;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.guide_vp_pager, null);

        context= container.getContext();

		pagenumber1 = (TextView)layout.findViewById(R.id.pagenumber1);
		pagenumber2 = (TextView)layout.findViewById(R.id.pagenumber2);
		pagenumber3 = (TextView)layout.findViewById(R.id.pagenumber3);

        tv_title = (TextView)layout.findViewById(R.id.textView_guide_title);
        tv_description = (TextView)layout.findViewById(R.id.textView_guide_description);
		pageImage = (ImageView)layout.findViewById(R.id.imageView_guide);
		videoView = (VideoView)layout.findViewById(R.id.videoView_guide);
        if(type_gv.get(position).equals("video/mp4")){

            pageImage.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

             final MediaController mc = new MediaController(context);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            Uri video = Uri.parse(img_url_gv.get(position));

            videoView.setMediaController(mc);
            videoView.setVideoURI(video);
            videoView.requestFocus();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {

                    videoView.start();

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                            // TODO Auto-generated method stub
                            // Log.e(TAG, "Changed");


                        }
                    });



                }
            });


        }else {  Glide.with(context).load(img_url_gv.get(position)).fitCenter().into(pageImage);}

        tv_title.setText(title_gv.get(position));
        tv_description.setText(description_gv.get(position));
        ((ViewPager) container).addView(layout, 0);
        if(position==0){
            pagenumber1.setBackgroundResource(R.color.colorPrimaryDark);

        }else if(position==1){
            pagenumber2.setBackgroundResource(R.color.colorPrimaryDark);
        }
        else if(position==2){
            pagenumber3.setBackgroundResource(R.color.colorPrimaryDark);
        }


		return layout;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	//public interface on

}
