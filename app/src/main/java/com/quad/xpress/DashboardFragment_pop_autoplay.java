package com.quad.xpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.quad.xpress.Adapters_horizontal.adapter_autoplay;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.helpers.StaticConfig;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DashboardFragment_pop_autoplay extends Fragment implements adapter_autoplay.OnRecyclerListener{


    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int Index = 0;
    String RefreshTokenMethodName;
    public static List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    String EndOfRecords = "0";
    static int previousposition;
    String Selected_file_url = "";
    String selected_file_path;
    String selected_file_name;
    String selected_file_mime;
    ImageView iv_no_data;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    adapter_autoplay recyclerAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    // public final static String Title_toload = "Popular";

/*    public static DashboardFragment_pop createInstance(String Title) {

        DashboardFragment_pop partThreeFragment = new DashboardFragment_pop();
        Bundle bundle = new Bundle();
        bundle.putString("Title_toload",Title);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_pop_auto);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_frag_pop_auto);
        recyclerAdapter = new adapter_autoplay(playlist,context,DashboardFragment_pop_autoplay.this);


        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
       // recyclerView.setOverScrollMode();
        recyclerView.setAdapter(recyclerAdapter);

        getData();



        //recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                playlist.clear();
                Index = 0;
                getData();

            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

            onResume();

    }



    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();



                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount-15  && !swipeRefreshLayout.isRefreshing() ) {

                   // Toast.makeText(context, "exe", Toast.LENGTH_SHORT).show();
                    Index ++;
                    getData();
                }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iv_no_data = (ImageView) getActivity().findViewById(R.id.ll_rv_bg);
        context = getActivity();

        return inflater.inflate(R.layout.dashboard_fragment_auto_pop, container, false);
    }


    @Override
    public void onItemClicked(int position) {
        previousposition = position;
        //    Toast.makeText(DashBoard.this, "ex", Toast.LENGTH_SHORT).show();
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

                // Toast.makeText(DashBoard.this, "url --"+Selected_file_url, Toast.LENGTH_SHORT).show();
            } else {
                //Local server
                //  Toast.makeText(DashBoard.this, "else --"+Selected_file_url, Toast.LENGTH_SHORT).show();
                Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;

            }

            // TbImage
            String TBPath = "audio";

            if (playlistItems.getFileMimeType().equalsIgnoreCase("video/mp4")) {

                if (playlistItems.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + playlistItems.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");

                    // Toast.makeText(DashBoard.this, "url"+Selected_file_url, Toast.LENGTH_SHORT).show();

                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + playlistItems.getTBPath();
                }


            }
            else if ( playlistItems.getFileMimeType().equalsIgnoreCase("audio/mp3") ){



                if (playlistItems.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + playlistItems.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + playlistItems.getTBPath();
                }

            }else {
                TBPath = "audio";

            }


            Intent videoIntent = new Intent(getActivity(),Actvity_video.class);
            videoIntent.putExtra("url",Selected_file_url);
            videoIntent.putExtra("type",selected_file_mime);
            videoIntent.putExtra("likes",playlistItems.getLikesCount());
            int vCout = Integer.parseInt(playlistItems.getViewsCount());
            vCout++;
            videoIntent.putExtra("views",""+vCout);
            videoIntent.putExtra("file_id",playlistItems.getFileID());
            videoIntent.putExtra("title",playlistItems.getTitle());
            videoIntent.putExtra("tags",playlistItems.getTags());
            videoIntent.putExtra("upload_date",playlistItems.getCreatedDate());
            videoIntent.putExtra("isliked",playlistItems.getIsUserLiked());
            videoIntent.putExtra("img_url",TBPath);
            videoIntent.putExtra("isPrivate","false");
            startActivity(videoIntent);

            //  new DownloadFileFromURL().execute(Selected_file_url);
            // Log.v("serverplusUrl", "" + Selected_file_url);
        } else if (!NCD.isConnected(context)) {
            Toast.makeText(context, "Check your connectivity.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Subcribed(int position, String email) {

    }

    @Override
    public void UnSubcribed(int position, String email) {

    }


    private void getData() {

       // Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(true);
        String emotion = "Like";
            RestClient.get(context).MyPublicLists_Popular(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "30",sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {

                                ParsePublicFiles(arg0);

                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";

                            } else if (arg0.getCode().equals("202")) {
                                swipeRefreshLayout.setRefreshing(false);

                            }else if(arg0.getData().getLast().equals("1")||arg0.getData().getRecords().length == 0){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            else {


                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }


                        @Override
                        public void failure(RetrofitError error) {
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    });

        }

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
                    iii.getEmotionCount(),iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user(),
                    iii.getMydp(),iii.getUser_id(),iii.getPhone_number());
            playlist.add(playlistItems);
            recyclerAdapter.notifyDataSetChanged();
        }


        EndOfRecords = arg0.getData().getLast();
        swipeRefreshLayout.setRefreshing(false);
        iv_no_data.setVisibility(View.INVISIBLE);








        }


    @Override
    public void onResume() {
        super.onResume();
        if(playlist!=null || playlist.size() != 0) {

            if (Actvity_video.like_clicked && Actvity_video.LikeChangedValue == 0 && !Actvity_video.isFromFeatured) {


                playlist.get(previousposition).setLikesCount("" + 0);
                playlist.get(previousposition).setIsUserLiked("0");
                recyclerAdapter.notifyDataSetChanged();

            }

            if (Actvity_video.LikeChangedValue != 0 && !Actvity_video.isFromFeatured) {


                PlayListitems_emotion playlistItemstemp = null;
                try {
                    if(playlist.size() >= previousposition ){
                        playlistItemstemp = playlist.get(previousposition);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // is userlike derevative

                int lkin = Integer.parseInt(playlistItemstemp.getLikesCount());

                if (lkin < Actvity_video.LikeChangedValue) {

                    playlistItemstemp.setIsUserLiked("1");

                } else {

                    playlistItemstemp.setIsUserLiked("0");

                }

                int vC = Integer.parseInt(playlistItemstemp.getViewsCount());
                vC++;
                playlistItemstemp.setViewsCount("" + vC);
                playlistItemstemp.setLikesCount("" + Actvity_video.LikeChangedValue);
                playlist.remove(previousposition);
                playlist.add(previousposition, playlistItemstemp);

                recyclerAdapter.notifyDataSetChanged();
            }

            // upating the follow list locally
            try {
                for (int i = 0; i < playlist.size(); i++) {

                    if (Act_user_data.fromemail.equals(playlist.get(i).getFromEmail())) {
                        playlist.get(i).setIsUserFollowing(Act_user_data.isfollowing);
                    }

                    recyclerAdapter.notifyDataSetChanged();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
       // Toast.makeText(context, "new resume", Toast.LENGTH_SHORT).show();
    }

}
