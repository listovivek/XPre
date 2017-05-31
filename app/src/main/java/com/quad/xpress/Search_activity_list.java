package com.quad.xpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.SaveResponseForOffline;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.Follow.FollowRep;
import com.quad.xpress.models.Follow.FollowReq;
import com.quad.xpress.models.TrendingSearch.TsReq;
import com.quad.xpress.models.TrendingSearch.Tsresp;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.receivedFiles.EndlessRecyclerOnScrollListener;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.adapter_dashboard;
import com.quad.xpress.models.receivedFiles.search.SearchFilesReq;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import me.crosswall.lib.coverflow.CoverFlow;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Search_activity_list extends AppCompatActivity implements adapter_dashboard.OnRecyclerListener {

    Context context;
    Activity activity;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    NetConnectionDetector NCD;
    int previousposition;
    String Search_query = "";
    String RefreshTokenMethodName;
    PlayListitems_emotion playlistItems;
    int Index = 0;
    boolean loading;
    String EndOfRecords = "0";
    private List<PlayListitems_emotion> playlist = new ArrayList<>();
    ProgressDialog pDialog;
    ProgressBar pb_loading;
    String AppName,SortType = "like";
    SearchView searchView,searchView_SA;
    ViewPager Vp_search;
    MenuItem title_item;
    TextView tv_search_title;
    RelativeLayout rl_heading;
    FrameLayout fl_vp_parent;
    TextView tv_tb;
    ImageView ivNTS;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = getApplicationContext();
        activity = this;
        pb_loading = (ProgressBar) findViewById(R.id.pb_search_act);
        Vp_search = (ViewPager) findViewById(R.id.viewPager_search);
        recyclerView = (RecyclerView) findViewById(R.id.RvRcvVideo);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tv_search_title = (TextView) findViewById(R.id.textView_search);
        rl_heading = (RelativeLayout) findViewById(R.id.sv_heading);
        fl_vp_parent = (FrameLayout) findViewById(R.id.fl_trending_seach);
        searchView_SA = (SearchView) findViewById(R.id.searcvViewSA);
        EditText searchEditText = (EditText)searchView_SA.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        NCD = new NetConnectionDetector();
        tv_tb = (TextView) findViewById(R.id.tb_normal_title);
        tv_tb.setText("Search");
        ImageButton btn_tb_back = (ImageButton) findViewById(R.id.tb_normal_back);
         ivNTS = (ImageView) findViewById(R.id.iv_nothing_to_show);
        btn_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView_SA.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView_SA.setIconifiedByDefault(false);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!Search_query.equals(query)) {
                    Search_query = query;
                    playlist.clear();
                    Index = 0;
                    if (NCD.isConnected(context)) {
                        if (Search_query.length() >= 3) {

                            searchView_SA.clearFocus();
                            getSearchData();
                        } else {
                            Toast.makeText(context, "Minimum 3 character length Required", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }


        };

        searchView_SA.setOnQueryTextListener(queryTextListener);



        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!loading && EndOfRecords.equals("0")) {
                    loading = true;
                    getSearchData();
                }
                loading = false;
            }


        });
        adapter = new adapter_dashboard(playlist, context, Search_activity_list.this);
        recyclerView.setAdapter(adapter);

        pDialog = new ProgressDialog(activity);
        AppName = getResources().getString(R.string.app_name);
        StaticConfig.IsPublicActivity = true;

        //top view
        final ArrayList<String>user_name = new ArrayList<>();
        final ArrayList<String>user_img= new ArrayList<>();
        final ArrayList<String>thumb_url= new ArrayList<>();
        final ArrayList<String>time= new ArrayList<>();
        final ArrayList<String>likes= new ArrayList<>();
        final ArrayList<String>views = new ArrayList<>();
        final ArrayList<String>media= new ArrayList<>();
        final ArrayList<String>reactions= new ArrayList<>();
        final ArrayList<String>title= new ArrayList<>();
        final ArrayList<String>file_id= new ArrayList<>();
        final ArrayList<String>tags= new ArrayList<>();


        RestClient.get(context).GetTrendingSearch(new TsReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<Tsresp>() {
                    @Override
                    public void success(Tsresp tsresp, Response response) {
                      int RSize =   tsresp.getData().getRecords().length ;
                        if(tsresp.getCode().equals("200")){
                            for (int i=0 ; i < tsresp.getData().getRecords().length;i++){
                                user_name.add(tsresp.getData().getRecords()[i].getFrom_email());
                                user_img.add(tsresp.getData().getRecords()[i].getThumbnailPath());
                                thumb_url.add(tsresp.getData().getRecords()[i].getThumbnailPath());
                                time.add(tsresp.getData().getRecords()[i].getCreated_date());
                                likes.add(tsresp.getData().getRecords()[i].getLikeCount());
                                views.add(tsresp.getData().getRecords()[i].getView_count());
                                media.add(tsresp.getData().getRecords()[i].getFileuploadPath());
                                reactions.add(tsresp.getData().getRecords()[i].getLikeCount());
                                title.add(tsresp.getData().getRecords()[i].getTitle());
                                file_id.add(tsresp.getData().getRecords()[i].get_id());
                                tags.add(tsresp.getData().getRecords()[i].getTags());

                                // Toast.makeText(_activity, "rved"+user_name.get(0), Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Featured Videos are not Available", Toast.LENGTH_LONG).show();

                        }
                        int noofsize= RSize;
                        Adapter_vp_search adapter_vp_db = new Adapter_vp_search(Search_activity_list.this,noofsize,user_name,user_img,thumb_url,time,likes,views,media,reactions,title,file_id,tags);
                        Vp_search.setOffscreenPageLimit(adapter_vp_db.getCount());
                        Vp_search.setAdapter(adapter_vp_db);
                        adapter_vp_db.notifyDataSetChanged();
                        pb_loading.setVisibility(View.GONE);

                        new CoverFlow.Builder()
                                .with(Vp_search)
                                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.db_vp_margin))
                                .scale(0)
                                .spaceSize(2f)
                                .rotationY(0f)
                                .build();


                        Intent getSearch =  getIntent();

                        try {
                            if(getSearch.getStringExtra("searchstring") != null){
                                Search_query =  getSearch.getStringExtra("searchstring");
                                searchView_SA.setQuery(Search_query,false);
                                getSearchData();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pb_loading.setVisibility(View.GONE);
                    }
                }

        );


      /*  RestClient.get(context).GetFeatured(new featuredReq("video"),
                new Callback<featuredResp>() {

                    @Override
                    public void success(featuredResp featuredResp, Response response) {
                        if(featuredResp.getCode().equals("200")){
                            for (int i=0 ; i < featuredResp.getData().length;i++){
                                user_name.add(featuredResp.getData()[i].getUsername());
                                user_img.add(featuredResp.getData()[i].getProfilePicture());
                                thumb_url.add(featuredResp.getData()[i].getThumbnailPath());
                                time.add(featuredResp.getData()[i].getTimePost());
                                likes.add(featuredResp.getData()[i].getLikeCount());
                                views.add(featuredResp.getData()[i].getView_count());
                                media.add(featuredResp.getData()[i].getFileuploadPath());
                                reactions.add(featuredResp.getData()[i].getSmailyCount());
                                title.add(featuredResp.getData()[i].getTitle());
                                file_id.add(featuredResp.getData()[i].get_id());
                                tags.add(featuredResp.getData()[i].getTags());

                                // Toast.makeText(_activity, "rved"+user_name.get(0), Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Featured Videos are not Available", Toast.LENGTH_LONG).show();

                        }
                        int noofsize= featuredResp.getData().length;
                        Adapter_vp_search adapter_vp_db = new Adapter_vp_search(Search_activity_list.this,noofsize,user_name,user_img,thumb_url,time,likes,views,media,reactions,title,file_id,tags);
                        Vp_search.setOffscreenPageLimit(adapter_vp_db.getCount());
                        Vp_search.setAdapter(adapter_vp_db);
                        adapter_vp_db.notifyDataSetChanged();
                        pb_loading.setVisibility(View.GONE);

                        new CoverFlow.Builder()
                                .with(Vp_search)
                                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.db_vp_margin))
                                .scale(0)
                                .spaceSize(2f)
                                .rotationY(0f)
                                .build();

                       // mPagerContainer.setPadding(0,0,10,0);
                       // mtd_resize();
                                                              }

                    @Override
                    public void failure(RetrofitError error) {
                        pb_loading.setVisibility(View.GONE);

                    }});
*/




    }

    private void mtd_resize() {
        ViewTreeObserver observer = fl_vp_parent.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
             int   flW =  fl_vp_parent.getWidth();
                fl_vp_parent.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
               // Vp_search.setMinimumWidth(flW/2);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) Vp_search.getLayoutParams();
                lp.width = (flW/2)-10;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( Actvity_video.LikeChangedValue != 0){

            PlayListitems_emotion  playlistItemstemp = playlist.get(previousposition);

            // is userlike derevative

            int lkin = Integer.parseInt(playlistItemstemp.getLikesCount());

            if(lkin > Actvity_video.LikeChangedValue){

                playlistItemstemp.setIsUserLiked("1");

            }else {

                playlistItemstemp.setIsUserLiked("0");

            }
            playlistItemstemp.setIsUserLiked("1");
            int vC = Integer.parseInt(playlistItemstemp.getViewsCount());
            vC++;
            playlistItemstemp.setViewsCount(""+vC);
            playlistItemstemp.setLikesCount(""+Actvity_video.LikeChangedValue);
            playlist.remove(previousposition);
            playlist.add(previousposition,playlistItemstemp);

            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_activity_menu_item).getActionView();
        title_item = menu.findItem(R.id.spinner_sea_title);
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        searchView.setFocusable(true);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!Search_query.equals(query)) {
                    Search_query = query;
                    playlist.clear();
                    Index = 0;
                    if (NCD.isConnected(context)) {
                        if (Search_query.length() >= 3) {
                           /* InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);*/
                            searchView.clearFocus();
                            getSearchData();
                        } else {
                            Toast.makeText(context, "Minimum 3 character length Required", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }


        };

        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        switch(item.getItemId()){
            case R.id.item_likes:{
                if(Search_query.length() >= 3 ){

                    title_item.setTitle("Likes");
                    Toast.makeText(getApplicationContext(),"Sorting by Likes...",Toast.LENGTH_LONG).show();
                    SortType = "like";
                    Index = 0;
                    playlist.clear();
                    getSearchData();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter your Query minimum 3 letters...", Toast.LENGTH_LONG).show();
                }

                return true;
            }
            case R.id.item_views:{
                if(Search_query.length() >= 3 ){
                    title_item.setTitle("Views");
                SortType = "view";
                Index = 0;
                playlist.clear();
                Toast.makeText(getApplicationContext(),"Sorting by most Views...",Toast.LENGTH_LONG).show();
                getSearchData();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter your Query minimum 3 letters...", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            case R.id.item_recent:{
                if(Search_query.length() >= 3 ) {
                    title_item.setTitle("Recent");
                    SortType = "recent";
                    Index = 0;
                    playlist.clear();
                    Toast.makeText(getApplicationContext(), "Sorting by most recent...", Toast.LENGTH_LONG).show();
                    getSearchData();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter your Query minimum 3 letters...", Toast.LENGTH_LONG).show();
                }
                return true;
            }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();

    }

    @Override
    public void onItemClicked(int position) {

        previousposition = position;
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


        Intent videoIntent = new Intent(Search_activity_list.this,Actvity_video.class);
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
    public void Subcribed(int position, final String email) {
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).FollowRequest(new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Subscribed to "+email+" Posts.", Toast.LENGTH_SHORT).show();
                        playlist.clear();
                        Index = 0;
                        getSearchData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    @Override
    public void UnSubcribed(int position, final String email) {
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).UnFollowRequest(new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Unsubscribed from  "+email+" Posts.", Toast.LENGTH_SHORT).show();
                        playlist.clear();
                        Index = 0;
                        getSearchData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }



    public void RefreshToken() {

        RestClient.get(context).RefreshTokenWS(new AuthTokenReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""), sharedpreferences.getString(SharedPrefUtils.SpDeviceId, "")), new Callback<AuthTokenResp>() {
            @Override
            public void success(final AuthTokenResp arg0, Response arg1) {

                if (arg0.getCode().equals("200")) {
                    editor.putString(SharedPrefUtils.SpToken, arg0.getData()[0].getToken());
                    editor.commit();
                    if (RefreshTokenMethodName.equals("getSearchData")) {
                        getSearchData();
                    }
                    Toast.makeText(context, "TokenRefreshed", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("", "Try again later " + arg0.getStatus());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //  Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSearchData() {

        ivNTS.setVisibility(View.GONE);
        final TextView tv_nothing_to_show = (TextView) findViewById(R.id.tv_nothing_to_show);
        tv_nothing_to_show.setVisibility(View.GONE);

        RestClient.get(context).SearchFilesApi(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new SearchFilesReq(Search_query,Integer.toString(Index), "10",SortType,sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<PlayListResp_emotion>() {
                    @Override
                    public void success(final PlayListResp_emotion arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {

                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();
                            ParsePublicFiles(arg0);
                            if (Index == 1) {
                                SaveResponseForOffline srfo = new SaveResponseForOffline(context);
                                srfo.save(arg1, SharedPrefUtils.SpPublicFiles);
                            }
                            Index = Index + 1;
                        } else if (arg0.getCode().equals("601")) {
                            RefreshTokenMethodName = "getData";
                            RefreshToken();

                        } else if (arg0.getCode().equals("202")) {
                            String a1;
                            a1 ="Nothing found with the name ";
                            ToastCustom toastCustom = new ToastCustom(Search_activity_list.this);
                            SpannableString ss = new SpannableString(a1 +Search_query+", \n Why don’t you Xpress one with that name yourself!");

                            ss.setSpan(new ForegroundColorSpan(Color.GREEN), a1.length(), a1.length()+Search_query.length(), 0);
                            ss.setSpan(new RelativeSizeSpan(1.5f),a1.length(), a1.length()+Search_query.length(), 0);
                            toastCustom.ShowToast("ixprez ",ss,0);

                          //  tv_nothing_to_show.setText(ss);
                          //  tv_nothing_to_show.setVisibility(View.VISIBLE);
                            ivNTS.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();


                        }
                        Index = Index + 1;
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
                        rl_heading.setVisibility(View.GONE);
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
                int eLength = arg0.getData().getRecords()[i].getEmotion().length;
                String emotions;
                for(int e=0; e < eLength;e++){
                    Emotion emo = arg0.getData().getRecords()[i].getEmotion()[e];
                    // Toast.makeText(_activity, " "+e +"  "+ emo.getEmotion(), Toast.LENGTH_LONG).show();
                    emotions = emo.getEmotion(); 
                    sb.append(emotions).append(",");  }


            } catch (Exception e) {
                return;
            }
            playlistItems = new PlayListitems_emotion(iii.getFileuploadFilename(), iii.getTitle(), iii.getCreated_date(), iii.getFrom_email()
                    , iii.getThumbnailPath(), iii.getFilemimeType(), iii.getFileuploadPath(), iii.getFileuploadFilename()
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getIsUserLiked(),sb.toString(),iii.getEmotionCount()
                    ,iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user());
            playlist.add(playlistItems);
        }
        EndOfRecords = arg0.getData().getLast();

        adapter.notifyDataSetChanged();
        rl_heading.setVisibility(View.VISIBLE);
        tv_search_title.setText(Search_query);
      //  tv_tb.setText(Search_query);

    }




}
