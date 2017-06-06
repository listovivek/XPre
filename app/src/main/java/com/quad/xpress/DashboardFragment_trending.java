package com.quad.xpress;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DashboardFragment_trending extends Fragment implements adapter_dashboard.OnRecyclerListener{


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
    boolean detachonce = false;
    // public final static String Title_toload = "Popular";


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
            getData();
            Index++;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
                    pb.setVisibility(View.VISIBLE);
                    Index++;
                    getData();
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
                getData();
            }
        });
       // setupRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
               // Toast.makeText(context,"++--"+Index,Toast.LENGTH_LONG).show();
                Index++;
                getData();
            }
        });


        recyclerAdapter = new adapter_dashboard(playlist,context,DashboardFragment_trending.this);
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
        try {
            File f = new File(Environment.getExternalStorageDirectory(), AppName);
            if (!f.exists()) {
                f.mkdirs();
            }
            Log.v("projDir", " " + f);
        } catch (Exception e) {
            Log.v("MTV", " createFileDirectory exception" + e);
        }
        //  Toast.makeText(getActivity(), "Item id " + selected_item_id, Toast.LENGTH_LONG).show();

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + AppName + "/", selected_file_name);

        Log.v("TMR", "file " + file);

        if (file.exists()) {
            Log.v("TMR", "file exists ");
            ShowFile(file.toString(), file);
        } else if (NCD.isConnected(context)) {
            //Live server
            if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
                Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
            } else {
                //Local server
                Selected_file_url = StaticConfig.ROOT_URL + "/" + selected_file_path;
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

    public void ShowFile(String url, File filename) {
        // MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = selected_file_mime;
        newIntent.setDataAndType(Uri.fromFile(filename), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Sorry, you have no application to open this file.", Toast.LENGTH_LONG).show();
        }
        Log.v("TMR show file", " type " + mimeType);

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String filename = "";

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
                filename = selected_file_name;
                String Filetype = conection.getContentType();
                Log.v("fileName", " " + selected_file_name + " FileLength " + lenghtOfFile + " Filetype " + Filetype);

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + AppName + "/" + filename);
                Log.v("", "" + Environment.getExternalStorageDirectory().toString() + "/" + R.string.app_name + "/" + filename);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                DownloadSucess = true;
            } catch (Exception e) {
                DownloadSucess = false;
                Log.e("Error: ", "Download Exception " + e);
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

     /*   *
         * Updating progress bar*/

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /* *
          * After completing background task
          * Dismiss the progress dialog
          **/
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Log.v("DownloadSucess", " " + DownloadSucess);
            if (DownloadSucess) {
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + AppName + "/", filename);
                ShowFile(file.toString(), file);
            } else {
                Toast.makeText(context, "Something went wrong,Try again later", Toast.LENGTH_LONG).show();

            }
        }

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
                    iii.getEmotionCount(),iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user(),iii.getMydp());
            playlist.add(playlistItems);
        }
        EndOfRecords = arg0.getData().getLast();

        //recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        pb = (ProgressBar) getActivity().findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.GONE);
        iv_no_data.setVisibility(View.INVISIBLE);
        recyclerAdapter.notifyDataSetChanged();

        if(!detachonce){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
            detachonce = true;
        }

        }

    @Override
    public void onResume() {
        super.onResume();

    }
}
