package com.quad.xpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quad.xpress.Adapters_horizontal.adapter_user_data;
import com.quad.xpress.models.Follow.FollowRep;
import com.quad.xpress.models.Follow.FollowReq;
import com.quad.xpress.models.receivedFiles.FollowerPublicListReq;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.utills.EndlessRecyclerOnScrollListener;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.StaticConfig;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Act_user_data extends Activity implements adapter_user_data.OnRecyclerListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    String RefreshTokenMethodName = "";
    public static String isfollowing,fromemail;
    Context context;
    Activity _activity;
    FloatingActionButton btn_follow;
    String AppName,usernae;

    TextView tv_tb_tilte,tv_username;
    ImageView iv_bg_blur,iv_pic,iv_tb_logo;
    int Index = 0;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    NetConnectionDetector NCD;
    private List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    ProgressBar pb;
    String EndOfRecords = "0";
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_user_lists);
        context = getApplicationContext();


        Intent inget = getIntent();

        pb = (ProgressBar) findViewById(R.id.pb_user_list);
        btn_follow = (FloatingActionButton) findViewById(R.id.fab);
        tv_username = (TextView) findViewById(R.id.tv_show_user_name);
        tv_tb_tilte = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_tilte.setVisibility(View.GONE);
        iv_tb_logo = (ImageView) findViewById(R.id.iv_tb_normal_logo);
        iv_tb_logo.setVisibility(View.VISIBLE);
        iv_bg_blur = (ImageView) findViewById(R.id.iv_show_profile_blur);
        iv_pic = (ImageView) findViewById(R.id.iv_show_user_pic);



        String Purl = null;
        try {
            Purl = inget.getStringExtra("ProfilePic");
            usernae = inget.getStringExtra("username");
            fromemail = inget.getStringExtra("user_id");
            isfollowing = inget.getStringExtra("isfollowing");
        } catch (Exception e) {
            e.printStackTrace();

        }
       Glide.with(context).load(Purl).error(R.drawable.app_logo).bitmapTransform(new BlurTransformation(context)).into(iv_bg_blur);
        Glide.with(context).load(Purl).error(R.drawable.app_logo).into(iv_pic);

        tv_username.setText(usernae);

        if(isfollowing.equals("1")){
            btn_follow.setImageResource(R.drawable.ic_unfollow);
        }else {
            btn_follow.setImageResource(R.drawable.ic_view_profile_icon);
        }





        recyclerView = (RecyclerView) findViewById(R.id.rv_user_data);
        context = getApplicationContext();
        AppName = getResources().getString(R.string.app_name);
        _activity = this;

        /*TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_title.setText("Your Uploads");*/
        ImageButton btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






        NCD = new NetConnectionDetector();
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();









        if(sharedpreferences.getString(SharedPrefUtils.SpPhone, "").equals(fromemail)){
            btn_follow.setVisibility(View.GONE);
        }


        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isfollowing.equals("1")){
                   UnSubcribed(fromemail);
                    btn_follow.setImageResource(R.drawable.ic_view_profile_icon);
                    isfollowing = "0";

                }else {
                    Subcribed(fromemail);
                    btn_follow.setImageResource(R.drawable.ic_unfollow);
                    isfollowing= "1";

                }

            }
        });

        linearLayoutManager = new LinearLayoutManager(Act_user_data.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new adapter_user_data(playlist, context, Act_user_data.this);
        recyclerView.setAdapter(adapter);


        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                pb.setVisibility(View.VISIBLE);
                Index ++;
                getData();
            }
        });







    }

    public void Subcribed(String email) {
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).FollowRequest(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),email),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Followed successfully.", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }


    public void UnSubcribed(String email) {
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).UnFollowRequest(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),email),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Unfollowed successfully.", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }


    public void getData() {

       // Toast.makeText(context, ""+Index, Toast.LENGTH_SHORT).show();

        String emotion="Like";




            RestClient.get(context).Followers_public_list(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new FollowerPublicListReq(Integer.toString(Index), "30", fromemail, emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {
                                // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                              if(arg0.getData().getRecords().length > 0){
                                  ParsePublicFiles(arg0);

                                  Index = Index + 1;
                              }else {
                                  pb.setVisibility(View.INVISIBLE);
                              }



                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";
                                pb.setVisibility(View.INVISIBLE);
                                //RefreshToken();
                            } else if (arg0.getCode().equals("202")) {
                               // adapter.notifyDataSetChanged();
                                EndOfRecords = "1";
                                pb.setVisibility(View.INVISIBLE);
                              //  Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.INVISIBLE);
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {


                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "Unable to connect.", Toast.LENGTH_LONG).show();
                        }
                    });

    }

    private void ParsePublicFiles(PlayListResp_emotion arg0) {
        int RLength = arg0.getData().getRecords().length;
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < RLength; i++) {
            Records iii = arg0.getData().getRecords()[i];

            try {
                if (iii.getFileuploadFilename().equals("")) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            playlistItems = new PlayListitems_emotion(iii.getFileuploadFilename(), iii.getTitle(), iii.getCreated_date(), iii.getFrom_email()
                    , iii.getThumbtokenizedUrl(), iii.getFilemimeType(), iii.getTokenizedUrl(), iii.getFileuploadFilename()
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getIsUserLiked(),
                    sb.toString(),iii.getEmotionCount(),iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getFrom_email(),
                    iii.getFrom_user(),iii.getMydp(),iii.getUser_id(),iii.getPhone_number());
            playlist.add(playlistItems);
            sb.setLength(0);
        }
        EndOfRecords = arg0.getData().getLast();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pb.setVisibility(View.GONE);

    }


    @Override
    public void onItemClicked(int position) {

        String selected_file_path = playlist.get(position).getFileuploadPath();
        String Selected_file_url="";
        if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
            Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
        } else {
            //Local server
            Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
        }
        //BG image
        String img_url = "";
        String img_thumb = "";
        if(playlist.get(position).getTBPath() != null){
            img_thumb = playlist.get(position).getTBPath();
            if (img_thumb.contains(StaticConfig.ROOT_URL_Media)) {
                img_url = StaticConfig.ROOT_URL + img_thumb.replace(StaticConfig.ROOT_URL_Media, "");
            } else {
                //Local server
                img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

            }
        }else{

            img_url = "audio";
        }


        Intent videoIntent = new Intent(Act_user_data.this,Actvity_video.class);
        videoIntent.putExtra("url",Selected_file_url);
        videoIntent.putExtra("type",playlist.get(position).getFileMimeType());
        videoIntent.putExtra("likes",playlist.get(position).getLikesCount());
        videoIntent.putExtra("views",playlist.get(position).getViewsCount());
        videoIntent.putExtra("file_id",playlist.get(position).getFileID());
        videoIntent.putExtra("title",playlist.get(position).getTitle());
        videoIntent.putExtra("tags",playlist.get(position).getTags());
        videoIntent.putExtra("upload_date",playlist.get(position).getCreatedDate());
        videoIntent.putExtra("isliked",playlist.get(position).getIsUserLiked());
        videoIntent.putExtra("img_url",img_url);
        videoIntent.putExtra("isPrivate","false");
        startActivity(videoIntent);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NCD.isConnected(context)) {
            playlist.clear();
            Index = 0;

            getData();
        } else {
            Toast.makeText(context,"Your are Offline",Toast.LENGTH_LONG).show();
        }
    }
/* public void RefreshToken() {

        RestClient.get(context).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {


                if (arg0.getCode().equals("200")) {
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();
                    if (RefreshTokenMethodName.equals("getData()")) {
                        getData();
                    }
                    Toast.makeText(context, "TokenRefreshed", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
            }
        });
    }

*/


  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onClick(View v) {

    }
}
