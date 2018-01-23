package com.quad.xpress.uploadsFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.quad.xpress.Actvity_videoPlayback_Private;
import com.quad.xpress.Myuploads.DeleteReq;
import com.quad.xpress.Myuploads.MyUploadsAdapter;
import com.quad.xpress.R;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.StaticConfig;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 8/30/17.
 */

public class private_uploads_frgamnet extends Fragment implements MyUploadsAdapter.OnRecyclerListener {
    RecyclerView rv_public;
    SwipeRefreshLayout swr_rv_public;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    RecyclerView.Adapter adapter;
    List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    Context context;
    SharedPreferences sharedpreferences;
    ImageView iv_empty;
    int Index = 0;
    Boolean loading;
    String EndOfRecords = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.my_uploads_public, container, false);
        context = getActivity().getApplicationContext();
        rv_public = rootView.findViewById(R.id.myuploads_public_rv);
        swr_rv_public = rootView.findViewById(R.id.myupl_public_swr);
        iv_empty = (ImageView) rootView.findViewById(R.id.iv_nothing_to_show);
        sharedpreferences = getActivity().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

        getData();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        rv_public.setLayoutManager(staggeredGridLayoutManager);
        adapter = new MyUploadsAdapter(playlist, getActivity(), this);

        swr_rv_public.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Index = 0;
                playlist.clear();
                loading = true;
                getData();

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv_public.setAdapter(adapter);



        rv_public.addOnScrollListener(recyclerViewOnScrollListener);



    }

    public void getData() {

        String emotion="Like";



            RestClient.get(context).MyUploads_Private(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "30", sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {
                                // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();
                                ParsePublicFiles(arg0);

                            } else if (arg0.getCode().equals("601")) {

                            } else if (arg0.getCode().equals("202") && arg0.getData().getRecords().length == 1 && playlist.isEmpty()) {

                                //Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                                iv_empty.setVisibility(View.VISIBLE);
                                playlist.clear();
                                adapter.notifyDataSetChanged();

                            }
                            else if (arg0.getCode().equals("202") && arg0.getData().getLast().equals("1") ) {
                                //  adapter.notifyDataSetChanged();
                               // Toast.makeText(context, "End of list", Toast.LENGTH_LONG).show();
                                loading = false;
                                swr_rv_public.setRefreshing(false);

                            }else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();
                                swr_rv_public.setRefreshing(false);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            iv_empty.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Unable to connect.", Toast.LENGTH_LONG).show();
                            swr_rv_public.setRefreshing(false);
                        }
                    });

    }

    private void ParsePublicFiles(PlayListResp_emotion arg0) {
        loading = true;
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
                    sb.toString(),iii.getEmotionCount(),iii.getIsuerfollowing(),"private",iii.getTo_email(),
                    iii.getFrom_user(),iii.getMydp(),iii.getUser_id(),iii.getPhone_number());
            playlist.add(playlistItems);
            sb.setLength(0);
        }
        EndOfRecords = arg0.getData().getLast();
        Index++;
        adapter.notifyDataSetChanged();
        swr_rv_public.setRefreshing(false);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int  visibleItemCount = staggeredGridLayoutManager.getChildCount();
            int  totalItemCount = staggeredGridLayoutManager.getItemCount();
            int[] firstVisibleItems = null;
            int pastVisibleItems = 0;
            firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
            if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                pastVisibleItems = firstVisibleItems[1];
            }
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount && loading) {
                loading = false;
                Index ++;
                getData();
            }




        }
    };

    @Override
    public void onItemClicked(int position) {

        String selected_file_path = playlist.get(position).getFileuploadPath();
        String Selected_file_url="";
        if (selected_file_path.contains(StaticConfig.ROOT_URL_Media)) {
            Selected_file_url = StaticConfig.ROOT_URL + selected_file_path.replace(StaticConfig.ROOT_URL_Media, "");
        }
        else if  (selected_file_path.contains("https")){
            Selected_file_url = selected_file_path;
        }
        else {
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
            }
            else if  (img_thumb.contains("https")){
                img_url = img_thumb;
            }
            else {
                //Local server
                img_url = StaticConfig.ROOT_URL + "/" + img_thumb;

            }
        }else{

            img_url = "audio";
        }

        Intent videoIntent = null;



        videoIntent    = new Intent(getActivity(),Actvity_videoPlayback_Private.class);
        videoIntent.putExtra("isPrivate","true");

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
        videoIntent.putExtra("IsMyUploads",true);
        startActivity(videoIntent);



    }

    @Override
    public void DeleteVideo(final String id, final String type , final Boolean isPrivate) {
        //Toast.makeText(context, "Delete"+id, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppCompatAlertDialogStyle));


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
}
