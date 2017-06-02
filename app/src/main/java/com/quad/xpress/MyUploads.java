package com.quad.xpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.Myuploads.DeleteReq;
import com.quad.xpress.Myuploads.MyUploadsAdapter;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.SaveResponseForOffline;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.receivedFiles.EndlessRecyclerOnScrollListener;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyUploads extends AppCompatActivity implements MyUploadsAdapter.OnRecyclerListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    LinearLayout rv_ll;
    String RefreshTokenMethodName = "";
    ProgressDialog pDialog;
    Context context;
    Activity _activity;
    String AppName;
    ProgressBar pb;
    ImageView iv_empty;
    int Index = 0;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    NetConnectionDetector NCD;
    private List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    boolean loading;
    String EndOfRecords = "0";
    StaggeredGridLayoutManager  staggeredGridLayoutManager;
    TabLayout tabLayout;
    LinearLayoutManager layoutManager;
    Boolean Api_private_uploads = false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_videos);
        pb = (ProgressBar) findViewById(R.id.progressBar_private);
        iv_empty = (ImageView) findViewById(R.id.iv_nothing_to_show);
        recyclerView = (RecyclerView) findViewById(R.id.RvRcvVideo);
        context = getApplicationContext();
        AppName = getResources().getString(R.string.app_name);
        _activity = this;
        StaticConfig.IsPublicActivity = true;
        TextView tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb_title.setText("Your Uploads");
        ImageButton  btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NCD = new NetConnectionDetector();
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        rv_ll = (LinearLayout) findViewById(R.id.rv_ll);


       tabLayout = (TabLayout) findViewById(R.id.tabLayout_private);
        tabLayout.addTab(tabLayout.newTab().setText("Public Uploads"));
        tabLayout.addTab(tabLayout.newTab().setText("Private Uploads"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();


                if(position == 0){
                    Api_private_uploads = false;
                    Index = 0;
                }else {
                    Api_private_uploads = true;
                    Index = 0;
                }

                getData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      /*  tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });*/

       recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        if (NCD.isConnected(context)) {
            getData();
        } else {
           Toast.makeText(context,"Your are Offline",Toast.LENGTH_LONG).show();
        }
        pDialog = new ProgressDialog(_activity);
       // errorReporting = new ErrorReporting(_activity);
       /* recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) staggeredGridLayoutManager) {
            @Override
            public void onLoadMore() {
                if (!loading && EndOfRecords.equals("0")) {
                    Log.v("publicactivity", "EndOfRecords " + EndOfRecords);
                    loading = true;
                    getData();
                }
                loading = false;
            }
        });*/

      /*  layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Toast.makeText(MyUploads.this, "dd", Toast.LENGTH_SHORT).show();
                if (EndOfRecords.equals("0")) {
                    loading = true;
                    Index++;
                    getData();
                }else{loading = false;
                    Index = 0;
                }
            }


        });*/

        adapter = new MyUploadsAdapter(playlist, context, MyUploads.this);
        recyclerView.setAdapter(adapter);
    }




    public void getData() {
       pb.setVisibility(View.VISIBLE);
        String emotion="Like";

        playlist.clear();

        if(Api_private_uploads){
            RestClient.get(context).MyUploads_Private(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "100",sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {
                            pb.setVisibility(View.GONE);
                            if (arg0.getCode().equals("200")) {
                                // Toast.makeText(context, "" + arg0.getData().getRecords()[0].getTo_email(), Toast.LENGTH_LONG).show();
                                ParsePublicFiles(arg0);
                                if (Index == 0) {
                                    SaveResponseForOffline srfo = new SaveResponseForOffline(context);
                                    srfo.save(arg1, SharedPrefUtils.SpPublicFiles);
                                }
                                Index = Index + 1;
                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";
                                RefreshToken();
                            } else if (arg0.getCode().equals("202")) {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                            }
                            Index = Index + 1;
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            pb.setVisibility(View.GONE);
                            iv_empty.setVisibility(View.VISIBLE);

                            Toast.makeText(context, "Unable to connect.", Toast.LENGTH_LONG).show();
                        }
                    });
        }else {

            RestClient.get(context).MyUploads_API(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "100", sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {
                            pb.setVisibility(View.GONE);
                            if (arg0.getCode().equals("200")) {
                                // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();
                                ParsePublicFiles(arg0);
                                if (Index == 0) {
                                    SaveResponseForOffline srfo = new SaveResponseForOffline(context);
                                    srfo.save(arg1, SharedPrefUtils.SpPublicFiles);
                                }
                                Index = Index + 1;
                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";
                                RefreshToken();
                            } else if (arg0.getCode().equals("202")) {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                            }
                            Index = Index + 1;
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            pb.setVisibility(View.GONE);
                            iv_empty.setVisibility(View.VISIBLE);

                            Toast.makeText(context, "Unable to connect.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
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
                    , iii.getThumbnailPath(), iii.getFilemimeType(), iii.getFileuploadPath(), iii.getFileuploadFilename()
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getIsUserLiked(),
                    sb.toString(),iii.getEmotionCount(),iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user());
            playlist.add(playlistItems);
            sb.setLength(0);
        }
        EndOfRecords = arg0.getData().getLast();
        adapter.notifyDataSetChanged();

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


        Intent videoIntent = new Intent(MyUploads.this,Actvity_video.class);
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
    public void DeleteVideo(final String id, final String type , final Boolean isPrivate) {
        //Toast.makeText(context, "Delete"+id, Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppCompatAlertDialogStyle));
        // Setting Dialog Title
        alertDialog.setTitle("Are you sure you want to delete this Xpression?");

        // Setting Dialog Message
        alertDialog.setMessage("This cannot be undone !");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
                if(isPrivate){

                    RestClient.get(context).DeletePrivate(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),
                            new DeleteReq(id, type),
                            new Callback<Like_Resp>() {
                                @Override
                                public void success(Like_Resp like_resp, Response response) {

                                    Toast.makeText(context,"Xpression deleted from your list.",Toast.LENGTH_LONG).show();
                                    Index = 0;
                                    playlist.clear();
                                    getData();


                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });

                }else {

                RestClient.get(context).Delete(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),
                        new DeleteReq(id, type),
                        new Callback<Like_Resp>() {
                            @Override
                            public void success(Like_Resp like_resp, Response response) {

                                Toast.makeText(context,"Xpression Deleted.",Toast.LENGTH_LONG).show();
                                Index = 0;
                                playlist.clear();
                                getData();


                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


            alertDialog.show();



    }

    @Override
    public void MenuOnItem(int position) {

    }


    public void RefreshToken() {

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




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


}
