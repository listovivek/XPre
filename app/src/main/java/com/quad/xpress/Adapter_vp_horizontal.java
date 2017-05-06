package com.quad.xpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quad.xpress.Utills.helpers.StaticConfig;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_vp_horizontal extends PagerAdapter {
	int size;
	Activity act;
    View layout;
    TextView tv_uname,tv_time,tv_title,tv_likes,tv_views,tv_reactions;
    ImageView iv_thumb;
	CircleImageView iv_uimg;
	Context context;


    ArrayList<String>user_img_vpdp = new ArrayList<>();
	ArrayList<String>user_name_vpdp = new ArrayList<>();
	ArrayList<String>time_vpdp = new ArrayList<>();
	ArrayList<String>likes_vpdp = new ArrayList<>();
	ArrayList<String>views_vpdp = new ArrayList<>();
	ArrayList<String>thumb_img_vpdp = new ArrayList<>();
	ArrayList<String>media_url_vpdp = new ArrayList<>();
	ArrayList<String>reactions_vpdp = new ArrayList<>();
    ArrayList<String>title_vpdp = new ArrayList<>();
    ArrayList<String>file_id_vpdp = new ArrayList<>();
    ArrayList<String>tags_vpdp = new ArrayList<>();
	//user_name,user_img,thumb_url,time,likes,views,media,reactions,title

	public Adapter_vp_horizontal(DashBoard dashBoard, int noofsizeguide, ArrayList<String>user_name, ArrayList<String>user_img,
								 ArrayList<String>thumb_url, ArrayList<String>time, ArrayList<String>likes, ArrayList<String>views,
								 ArrayList<String>media, ArrayList<String>reactions, ArrayList<String>title, ArrayList<String>file_id, ArrayList<String>tags) {
		// TODO Auto-generated constructor stub
		size = noofsizeguide;
		act = dashBoard;
        user_name_vpdp = user_name;
		user_img_vpdp =user_img;
		time_vpdp= time;
		likes_vpdp = likes;
		views_vpdp=views;
		thumb_img_vpdp =thumb_url;
		media_url_vpdp = media;
		reactions_vpdp =reactions;
        title_vpdp = title;
        file_id_vpdp=file_id;
        tags_vpdp=tags;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}



	@Override
	public Object instantiateItem(View container, final int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.dash_vp_pager, null);
        context= container.getContext();
		String TBPath = "";


        tv_uname = (TextView) layout.findViewById(R.id.dvp_adp_txt_username);
        tv_time= (TextView) layout.findViewById(R.id.dvp_adp_txt_time);
        tv_title = (TextView) layout.findViewById(R.id.dvp_tv_title);
        tv_likes = (TextView) layout.findViewById(R.id.dvp_tv_likes);
        tv_views = (TextView) layout.findViewById(R.id.dvp_tv_views);
        tv_reactions = (TextView) layout.findViewById(R.id.dvp_tv_shared);
        iv_thumb = (ImageView) layout.findViewById(R.id.dvp_iv_tmb);

        iv_uimg = (CircleImageView) layout.findViewById(R.id.circleImageView_dvp);

        tv_uname.setText(user_name_vpdp.get(position).toString());

        if (thumb_img_vpdp.get(position).contains(StaticConfig.ROOT_URL_Media)) {
            TBPath = StaticConfig.ROOT_URL + thumb_img_vpdp.get(position).replace(StaticConfig.ROOT_URL_Media, "");
        } else {
            TBPath = StaticConfig.ROOT_URL + "/" + thumb_img_vpdp.get(position);
        }
		Glide.with(context).load(TBPath).placeholder(R.mipmap.ic_launcher).centerCrop().into(iv_thumb);

		//user pic
		String TBPathu = "";
		if (user_img_vpdp.get(position).contains(StaticConfig.ROOT_URL_Media)) {
			TBPathu = StaticConfig.ROOT_URL + user_img_vpdp.get(position).replace(StaticConfig.ROOT_URL_Media, "");
		} else {
			TBPathu = StaticConfig.ROOT_URL + "/" + user_img_vpdp.get(position);
		}
		Glide.with(context).load(TBPathu).placeholder(R.mipmap.ic_user_icon).dontAnimate().fitCenter().into(iv_uimg);
		//iv_uimg.setImageURI(Uri.parse(TBPathu));

        tv_title.setText(title_vpdp.get(position));
        tv_likes.setText(" "+likes_vpdp.get(position));
        tv_views.setText(views_vpdp.get(position));
        tv_reactions.setText(reactions_vpdp.get(position));

		Double Totakseconds = Double.parseDouble(time_vpdp.get(position));
        Double hours = Totakseconds / 3600;
        if(hours>24){
        Double Days = (hours/24);
        String[] splitter = Days.toString().split("\\.");
        int D = Integer.parseInt(splitter[0]);
        tv_time.setText(D+ " d  ");
        }
        else {
            tv_time.setText(hours+ " h  ");
        }
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(context, "p"+position, Toast.LENGTH_SHORT).show();
				Intent video_act = new Intent(context, Actvity_video.class);
				String MediaPath,TBPath;
				if (media_url_vpdp.get(position).contains(StaticConfig.ROOT_URL_Media)) {
					MediaPath = StaticConfig.ROOT_URL + media_url_vpdp.get(position).replace(StaticConfig.ROOT_URL_Media, "");
				} else {
					MediaPath = StaticConfig.ROOT_URL + "/" + media_url_vpdp.get(position);
				}
				if (thumb_img_vpdp.get(position).contains(StaticConfig.ROOT_URL_Media)) {
					TBPath = StaticConfig.ROOT_URL + thumb_img_vpdp.get(position).replace(StaticConfig.ROOT_URL_Media, "");
				} else {
					TBPath = StaticConfig.ROOT_URL + "/" + thumb_img_vpdp.get(position);
				}

				video_act.putExtra("url",MediaPath);
				video_act.putExtra("thumbImg",TBPath);
				video_act.putExtra("type","video");
				video_act.putExtra("likes",likes_vpdp.get(position));
				video_act.putExtra("views",views_vpdp.get(position));
				video_act.putExtra("file_id",file_id_vpdp.get(position));
				video_act.putExtra("title",title_vpdp.get(position));
				video_act.putExtra("tags",tags_vpdp.get(position));
				video_act.putExtra("upload_date",time_vpdp);
				video_act.putExtra("isliked","0");
				video_act.putExtra("img_url",TBPath);
				video_act.putExtra("isPrivate","false");
				v.getContext().startActivity(video_act);


			}
		});




		((ViewPager) container).addView(layout, 0);
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

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
