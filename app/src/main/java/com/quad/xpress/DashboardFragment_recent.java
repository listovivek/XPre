package com.quad.xpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quad.xpress.Utills.EndlessRecyclerOnScrollListener;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.adapter_dashboard;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DashboardFragment_recent extends Fragment implements adapter_dashboard.OnRecyclerListener{


    Context context;
    Boolean Api_Popular = false ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int Index;
    String RefreshTokenMethodName;
    private List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    String EndOfRecords = "0";
    ProgressBar pb;
    Boolean last = false;
    ProgressDialog pDialog;
    String AppName ="Xpress";
    String Selected_file_url = "";
    String selected_file_path;
    String selected_file_name;
    String selected_file_mime;
    boolean DownloadSucess = true;
    ImageView iv_no_data;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    adapter_dashboard recyclerAdapter;
    // public final static String Title_toload = "Popular";

    public static DashboardFragment_recent createInstance(String Title) {
        DashboardFragment_recent partThreeFragment = new DashboardFragment_recent();
        Bundle bundle = new Bundle();
        bundle.putString("Title_toload",Title);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);



    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Toast.makeText(getActivity(),"refresh",Toast.LENGTH_LONG).show();
            pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
            pb.setVisibility(View.VISIBLE);
            playlist.clear();
            Index = 0;
           // getData();
            Index++;

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                    pb.setVisibility(View.VISIBLE);
                    Index++;
                   // getData();
                }
            });




        }
        else {
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                playlist.clear();
                Index = 0;
              //  getData();
            }
        });
       // setupRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
               // Toast.makeText(context,"++--"+Index,Toast.LENGTH_LONG).show();
                Index++;
             //   getData();
            }
        });


        recyclerAdapter = new adapter_dashboard(playlist,context,DashboardFragment_recent.this);
        recyclerView.setAdapter(recyclerAdapter);




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
        iv_no_data = (ImageView) getActivity().findViewById(R.id.ll_rv_bg);
        context = getActivity();
        Api_Popular = false;
        return inflater.inflate(R.layout.dashboard_fragment_refresh, container, false);
    }





    @Override
    public void onItemClicked(int position) {
        //  popp = position;
        // test_data();

        playlistItems = playlist.get(position);
        selected_file_path = playlistItems.getFileuploadPath(); //getfileuploadPath[position];
        selected_file_name = playlistItems.getFileName();
        selected_file_mime = playlistItems.getFileMimeType();
        NetConnectionDetector NCD;
        NCD = new NetConnectionDetector();
         if (NCD.isConnected(context)) {
            //Live server
            if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
                Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
            } else {
                //Local server
                Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
            }

             String img_url;
             String img_thumb = playlistItems.getTBPath();
             if (playlistItems.getFileMimeType().equals("audio/mp3")){
                 img_url = "drawable://" + R.drawable.ic_mic_placeholder;
             }else {

                 if (img_thumb.contains(StaticConfig.ROOT_URL_Media)) {
                     img_url = StaticConfig.ROOT_URL + img_thumb.replace(StaticConfig.ROOT_URL_Media, "");
                 } else {
                     //Local server
                     img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

                 }
            }

            Intent videoIntent = new Intent(getActivity(),Actvity_video.class);
            videoIntent.putExtra("url",Selected_file_url);
            videoIntent.putExtra("type",selected_file_mime);
            videoIntent.putExtra("likes",playlistItems.getLikesCount());
            videoIntent.putExtra("views",playlistItems.getViewsCount());
            videoIntent.putExtra("file_id",playlistItems.getFileID());
            videoIntent.putExtra("title",playlistItems.getTitle());
            videoIntent.putExtra("tags",playlistItems.getTags());
            videoIntent.putExtra("upload_date",playlistItems.getCreatedDate());
            videoIntent.putExtra("isliked",playlistItems.getIsUserLiked());
            videoIntent.putExtra("img_url",img_url);
            videoIntent.putExtra("isPrivate","false");
            startActivity(videoIntent);

                    //  new DownloadFileFromURL().execute(Selected_file_url);
            Log.v("serverplusUrl", "" + Selected_file_url);
        } else if (!NCD.isConnected(context)) {
            Toast.makeText(context, "No Internet to download", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Subcribed(int position, String email) {

    }

    @Override
    public void UnSubcribed(int position, String email) {

    }


    private void getData() {

        pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.VISIBLE);
        String emotion = "Like";

        if(Api_Popular){
            RestClient.get(context).MyPublicLists_Popular(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "10",sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {

                                ParsePublicFiles(arg0);

                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";
                                RefreshToken_publicList();
                            } else if (arg0.getCode().equals("202")) {
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                            }else if(arg0.getData().getLast().equals("1")||arg0.getData().getRecords().length == 0){
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, "End Of List.. ", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                           Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();
                            pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                            pb.setVisibility(View.GONE);
                        }
                    });
        }else {
            RestClient.get(context).Recent(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "10",sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {

                                ParsePublicFiles(arg0);

                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);
                                RefreshToken_publicList();
                            } else if (arg0.getCode().equals("202")) {
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);
                            }else {
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                            }

                            if(arg0.getData().getLast().equalsIgnoreCase("1")){
                                pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                                if(!last){
                                    last = true;
                                    Toast.makeText(context, "End Of List.. ", Toast.LENGTH_LONG).show();
                                }

                            }
                            //  Index = Index + 1;
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();
                            pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                            pb.setVisibility(View.GONE);
                        }
                    });

        }}

    public void RefreshToken_publicList() {
        // LD.ShowTheDialog("Please Wait", "Loading..", true);
        RestClient.get(context).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {

                // LD.DismissTheDialog();
                if (arg0.getCode().equals("200")) {
                  //  pb.setVisibility(View.GONE);
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
                Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void ParsePublicFiles(PlayListResp_emotion arg0) {
        int RLength = arg0.getData().getRecords().length;
        if(RLength == 0){
            pb.setVisibility(View.INVISIBLE);
        }
        StringBuilder sb=new StringBuilder();
      //  pb.setVisibility(View.GONE);
        for (int i = 0; i < RLength; i++) {
            Records iii = arg0.getData().getRecords()[i];
            try {
                if (iii.getFileuploadFilename().equals("")) {
                    return;
                }
                int eLength = arg0.getData().getRecords()[i].getEmotion().length;
                String emotions;
                for(int e=0; e < eLength;e++){
                    Emotion emo = arg0.getData().getRecords()[i].getEmotion()[e];
                    // Toast.makeText(_activity, " "+e +"  "+ emo.getEmotion(), Toast.LENGTH_LONG).show();
                    emotions = emo.getEmotion();
                    sb.append(emotions).append(",");
                }


            } catch (Exception e) {
                return;
            }
            playlistItems = new PlayListitems_emotion(iii.getFileuploadFilename(), iii.getTitle(), iii.getCreated_date(), iii.getFrom_email()
                    , iii.getThumbnailPath(), iii.getFilemimeType(), iii.getFileuploadPath(), iii.getFileuploadFilename()
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getIsUserLiked(),sb.toString(),
                    iii.getEmotionCount(),iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user());
            playlist.add(playlistItems);
        }
        EndOfRecords = arg0.getData().getLast();

        //recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.GONE);
        iv_no_data.setVisibility(View.INVISIBLE);
        recyclerAdapter.notifyDataSetChanged();
        }

    @Override
    public void onResume() {
        super.onResume();

    }
}
