package com.quad.xpress;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nex3z.notificationbadge.NotificationBadge;
import com.quad.xpress.Contacts.Contact;
import com.quad.xpress.Contacts.ContactMainActivity;
import com.quad.xpress.Contacts.DatabaseHandler;
import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.Utills.EndlessRecyclerOnScrollListener;
import com.quad.xpress.Utills.StatiConstants;
import com.quad.xpress.Utills.ViewPagerCustom;
import com.quad.xpress.Utills.helpers.NetConnectionDetector;
import com.quad.xpress.Utills.helpers.PermissionStrings;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.Utills.helpers.StaticConfig;
import com.quad.xpress.models.Follow.FollowRep;
import com.quad.xpress.models.Follow.FollowReq;
import com.quad.xpress.models.acceptRejectCount.AcceptRejectCount;
import com.quad.xpress.models.acceptRejectCount.ReqPvateCount;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.models.counter.CounterReq;
import com.quad.xpress.models.counter.CounterResp;
import com.quad.xpress.models.featuredVideos.featuredReq;
import com.quad.xpress.models.featuredVideos.featuredResp;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.Records;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.adapter_dashboard;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DashBoard extends AppCompatActivity implements adapter_dashboard.OnRecyclerListener {


    Context context;
    Dialog AVDialog,GuideDialouge;
    Dialog SendDiscardDialog;
    AutoCompleteTextView av_email;
    Dialog AudioRecordDialog;
    ImageView iv_no_data;
    private Uri fileUri;
    ProgressBar pb;
    Boolean exit = false;
    Boolean is_vp_Touched = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    int rl_ht;

    public static String FileNameWithMimeType;
    RecyclerView Rv_lists;



    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    static Context staticContex;

    String AppName;
    Activity _activity;
    ImageButton dash_receive;
    ImageButton DashPublicPlaylist;
    String RefreshTokenMethodName = "";

    ImageButton AudioFab;
    ImageButton VideoFab;
    public Animation animBlink;
    TextView tv_nb_MyUploads,tv_nb_userName,tv_nb_contacts,tv_nb_help,tv_nb_support,tv_nb_about;
    ImageButton btn_nb_exit,btn_nb_settings;
     SlidingMenu Smenu;


    String EndOfRecords = "0";
    int Index = 0;
    Boolean last = false;
    private List<PlayListitems_emotion> playlist = new ArrayList<>();
    PlayListitems_emotion playlistItems;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterRcv;
    String Selected_file_url = "";
    String selected_file_path;
    String selected_file_name;
    String selected_file_mime;
    FrameLayout fl_counter;
    static int previousposition;
    NetConnectionDetector CD;
    Toast NoInternet = null;

    NetConnectionDetector NDC;
    RelativeLayout rl_btm;
    ViewPager viewPager_dash_lists;
    String count="",PreviousNotificOUNT;
    TabLayout tabLayout;

    CoordinatorLayout cl;

   /* Intent slider_intent;*/
    int noofsize = 5;
    int noofsizeguide = 3,currentPage;
    ViewPagerCustom myPager = null;
    PagerContainer mPagerContainer;
    ImageButton btn_btm_settings,btn_btm_notifi,btn_tb_navigation,btn_nb_nav;
    Boolean Api_Popular = Boolean.TRUE ;
    //Data for vp
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
    CircleImageView iv_user_pic,iv_user_pic_nb;
    Toolbar toolbar;
    TextView tv_counter,tv_counter_nb;
    SwipeRefreshLayout swipeRefreshLayout;
    static Boolean iSnotificationClicked =false;
    Cursor cur;
    private int mNameColIdx, mIdColIdx;
    public static int ixemailcount;

    public static ArrayList<String> ixprez_email = new ArrayList<>();
    ArrayList<String> ixprez_username = new ArrayList<>();
    ArrayList<String> ixprez_profilepic = new ArrayList<>();


    ArrayList<String> appendEmail = new ArrayList<String>();
    ArrayList<String> appendName = new ArrayList<String>();
    ArrayList<String> appendProfilePic = new ArrayList<String>();



    int TotalCount;
    NotificationBadge TopBadge,tv_PvtCount,tv_notifi_count;;
    private boolean NoPermisson  = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nb);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        btn_tb_navigation = (ImageButton) findViewById(R.id.navigation_ic_tb);
        Rv_lists = (RecyclerView) findViewById(R.id.rv_dash);
        tv_counter = (TextView) findViewById(R.id.textView_tb_counter);
        cl = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        TopBadge = (NotificationBadge) findViewById(R.id.tb_db_badge);
        fl_counter = (FrameLayout) findViewById(R.id.fl_couter_holder);
        fl_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   ToastCustom toastCustom = new ToastCustom(DashBoard.this);
                String tc = TotalCount+"";
                final SpannableString ss = new SpannableString(tc+" Xpressions and counting ...");

                ss.setSpan(new ForegroundColorSpan(Color.RED), 0, tc.length(), 0);
                ss.setSpan(new RelativeSizeSpan(0f), 0, tc.length(), 0);

               // toastCustom.ShowToast("Ixprez",ss,1);



                LayoutInflater inflater=_activity.getLayoutInflater();
                View customToastroot =inflater.inflate(R.layout.toast_counts, null);
                Toast customtoastx=new Toast(context);
                customtoastx.setView(customToastroot);
                TextView textTitle = (TextView) customToastroot.findViewById(R.id.tv_toast_title);
                textTitle.setText("Ixprez");
                final TextView text = (TextView) customToastroot.findViewById(R.id.tv_toast_msg);

                final NotificationBadge nb_toast = (NotificationBadge) customToastroot.findViewById(R.id.toast_db_badge);
                nb_toast.setText(""+TotalCount);
                nb_toast.setTextColor(Color.WHITE);
             //   text.setVisibility(View.INVISIBLE);
                text.setText(ss);


                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {

                        nb_toast.setText(""+TotalCount);
                        text.setText(ss);
                        text.setVisibility(View.VISIBLE);

                    }
                };

                Timer   timer = new Timer(); // This will create a new Thread
                timer .schedule(new TimerTask() { // task to be scheduled

                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 1000, 1000);






                        //top
               customtoastx.setGravity(Gravity.FILL,0,0);
               customtoastx.setDuration(Toast.LENGTH_LONG);
               customtoastx.show();







            }
        });



      /*  ArrayList<String> ixem =new ArrayList<>();
        ArrayList<String> ixusername =  new ArrayList<>();
        ArrayList<String> ixcontact = new ArrayList<>();
        GetContacts gc = new GetContacts(ixem,ixusername,ixcontact);
*/

        //heartView = (ImageView) findViewById(R.id.heart_icon);


        /*Thread tt = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                              //  heartView.startAnimation(animSequential);
                                Toast.makeText(DashBoard.this, "call", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        tt.start();*/




        AppName = getResources().getString(R.string.app_name);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                playlist.clear();
                Index = 0;
                getData();
            }
        });

        Smenu = new SlidingMenu(this);
        Smenu.setMode(SlidingMenu.RIGHT);
        Smenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        Smenu.setShadowWidthRes(R.dimen.shadow_width);
        Smenu.setShadowDrawable(R.drawable.drawer_shadow);
        Smenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        Smenu.setFadeDegree(0.35f);
        Smenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW|SlidingMenu.SLIDING_CONTENT);
        Smenu.setMenu(R.layout.nav_header_main);
        btn_nb_nav = (ImageButton) findViewById(R.id.nb_ic_tb);
        tv_counter_nb = (TextView) findViewById(R.id.nb_textView_tb_counter);

        final PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    Api_Popular = true;
                }else {
                    Api_Popular = false;
                }

                mtd_load_list();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        try {
            mtd_contacts_reader();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Smenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                btn_tb_navigation.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.setElevation(0);
                }
                mtd_counter();
               // mtd_getProfilePic();

            }
        });
        Smenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                btn_tb_navigation.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.setElevation(4);
                }
                onResume();
                tv_nb_userName.setText(sharedpreferences.getString(SharedPrefUtils.SpUserName, "----"));
                mtd_counter();
               // mtd_getProfilePic();
            }
        });
        tv_nb_userName = (TextView) Smenu.findViewById(R.id.nb_user_name);
        tv_nb_MyUploads = (TextView) Smenu.findViewById(R.id.nb_myuploads);
        tv_nb_contacts = (TextView) Smenu.findViewById(R.id.nb_contacts);
        tv_nb_help = (TextView) Smenu.findViewById(R.id.nb_help);
        tv_nb_support = (TextView) Smenu.findViewById(R.id.nb_support);
        tv_nb_about = (TextView) Smenu.findViewById(R.id.nb_about);




        CircleImageView btn_nb_user = (CircleImageView) Smenu.findViewById(R.id.nb_user_pic);
        btn_nb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent= new Intent(DashBoard.this, SettingsActivity.class);
                startActivity(settingIntent);
            }
        });
        btn_tb_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "s", Toast.LENGTH_SHORT).show();
                Smenu.toggle();
            }
        });
        btn_nb_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Smenu.toggle();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        mPagerContainer = (PagerContainer) findViewById(R.id.pager_container);

        iv_user_pic = (CircleImageView) findViewById(R.id.img_btn_user_profile_img);
        iv_user_pic_nb = (CircleImageView) findViewById(R.id.nb_user_pic);


        tv_nb_MyUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyuploads = new Intent(DashBoard.this,MyUploads.class);
                startActivity(intentMyuploads);
            }
        });
        tv_nb_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemParent = new Intent(DashBoard.this,Act_HelpSupportAbout.class);
                ItemParent.putExtra("ItemParent","about");
                startActivity(ItemParent);
            }
        });
        tv_nb_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemParent = new Intent(DashBoard.this,PrivatePlayListActivity.class);
               // IemParent.putExtra("ItemParent","support");
                startActivity(ItemParent);
            }
        });

        tv_nb_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show();
               /* Intent ItemParent = new Intent(DashBoard.this,Act_HelpSupportAbout.class);
                ItemParent.putExtra("ItemParent","help");
                startActivity(ItemParent);*/

                String shareBody = "Hi, iam using this Xpressive app, Ixprez to send my emotions.";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ixprez");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));


            }
        });
        tv_nb_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!NoPermisson) {

                    Intent in_contact = new Intent(DashBoard.this, ContactMainActivity.class);
                    startActivity(in_contact);

                }else {

                    CheckAndRequestPermission();


                }
            }
        });


        

        iv_user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent= new Intent(DashBoard.this, SettingsActivity.class);
                startActivity(settingIntent);

            }
        });
        iv_user_pic_nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent= new Intent(DashBoard.this, SettingsActivity.class);
                startActivity(settingIntent);

            }
        });

        myPager = (ViewPagerCustom) findViewById(R.id.reviewpager);

        myPager.setScrollDuration(1500);
        //Web sv for featured vids
         mtd_getFeatured();


            mPagerContainer.setPageItemClickListener(new PageItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    //Toast.makeText(context,"position:c" + position,Toast.LENGTH_SHORT).show();
                    mtd_getFeatured();

                }
            });


        myPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                is_vp_Touched = true;
                myPager.setScrollDuration(300);

                return false;

            }
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == noofsize-1) {
                    currentPage = 0;
                }
                if(!is_vp_Touched){
                    myPager.setScrollDuration(1500);
                myPager.setCurrentItem(currentPage++, true);

                    try {
                        mtd_counter();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

               LayoutTransition lt = new LayoutTransition();
               lt.disableTransitionType(LayoutTransition.DISAPPEARING);
               myPager.setLayoutTransition(lt);
              // myPager.setPageTransformer(true, new DepthPageTransformer());


            }
        };

        Timer   timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 4000);



        // Verifying guide
        Boolean guide = sharedpreferences.getBoolean(SharedPrefUtils.SpGuideDiplayed, false);
       // mtd_getProfilePic();
        guide = false;
        if(!guide) {
            mtd_guide_view_pager();
        }


        btn_nb_exit= (ImageButton) findViewById(R.id.btn_exit_nb);
        btn_nb_settings= (ImageButton) findViewById(R.id.btn_settings_nb);

        btn_nb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
             /*   final Dialog proceedDiscardDialog = new Dialog(DashBoard.this,
                        R.style.Theme_Transparent);
                String a = "Are you sure you want to log out ?.\n\nYou will still receive notifications if haven't turned it off in your settings.";
                proceedDiscardDialog.setContentView(R.layout.dialog_conform_discard);
                TextView tv_msg = (TextView) proceedDiscardDialog.findViewById(R.id.tv_pvt_alert_reject_msg);
                tv_msg.setText(a);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                }
                Button btn_spam = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_spam);
                btn_spam.setVisibility(View.GONE);
                Button btn_confirm = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_proceed);
                btn_confirm.setText("Logout");
                Button btn_discard = (Button) proceedDiscardDialog.findViewById(R.id.pvt_alert_dismiss);
                proceedDiscardDialog.show();


                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editor.putBoolean(SharedPrefUtils.SpOtpVerify,false);
                        editor.commit();
                       Exit_Actvity.exitApplicationAnRemoveFromRecent(context);
                        proceedDiscardDialog.dismiss();




                    }
                });
                btn_discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        proceedDiscardDialog.dismiss();

                    }
                });*/
            }
        });
        btn_nb_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent= new Intent(DashBoard.this, SettingsActivity.class);
                startActivity(settingIntent);
            }
        });

        context = getApplicationContext();
        staticContex = this;
        _activity = this;
        StaticConfig.IsPublicActivity = true;
        pb = (ProgressBar) findViewById(R.id.progressBar_dash);
        NDC = new NetConnectionDetector();
        viewPager_dash_lists = (ViewPager) findViewById(R.id.viewPager_dash);
        //// TODO: 11/18/2016


        setupViewPager(viewPager_dash_lists);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager_dash_lists);
            }
        });

      //  initViewPagerAndTabs();
        iv_no_data = (ImageView) findViewById(R.id.ll_rv_bg);
        btn_btm_settings= (ImageButton) findViewById(R.id.btn_dash_post_audio);
        btn_btm_notifi = (ImageButton) findViewById(R.id.btn_dash_post_vid);
        tv_PvtCount = (NotificationBadge) findViewById(R.id.tv_pvt_count);
        tv_notifi_count = (NotificationBadge) findViewById(R.id.tv_notif_count);

        AudioFab = (ImageButton) findViewById(R.id.audioFab);
        VideoFab = (ImageButton) findViewById(R.id.videoFab);

        dash_receive = (ImageButton) findViewById(R.id.dash_receive);
        CD = new NetConnectionDetector();
        rl_btm = (RelativeLayout) findViewById(R.id.relativeLayout);
        DashPublicPlaylist = (ImageButton) findViewById(R.id.dash_public_playlist);


        if(!NDC.isOnline()){
            iv_no_data.setVisibility(View.VISIBLE);

        }else {
            mtd_pvtCount();
        }
        mtd_resize();






        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        NoInternet = Toast.makeText(context, "Please check your Internet Connection", Toast.LENGTH_LONG);
        dash_receive.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i;
                                                i = new Intent(DashBoard.this, PrivatePlayListActivity.class);
                                                startActivity(i);

                                            }
                                        }
        );
        DashPublicPlaylist.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      Intent i;
                                                      i = new Intent(DashBoard.this,Search_activity_list.class);
                                                      startActivity(i);
                                                  }
                                              }
        );

        btn_btm_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent= new Intent(DashBoard.this, SettingsActivity.class);
                startActivity(settingIntent);


            }
        });
        btn_btm_notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iSnotificationClicked =true;

                Intent MyUploadsIntent= new Intent(DashBoard.this, Act_notifications.class);
                MyUploadsIntent.putExtra("notificationCount",PreviousNotificOUNT);
                startActivity(MyUploadsIntent);

            }
        });
        AudioFab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            Intent i = new Intent(DashBoard.this, AudioRecordActivity.class);
                                            i.putExtra("positionEmailID", -1);
                                            startActivity(i);

                                        }
                                    }
        );

        VideoFab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            Intent i = new Intent(DashBoard.this, CameraRecordActivity.class);
                                            i.putExtra("positionEmailID", -1);
                                            startActivity(i);

                                        }
                                    }

        );
        GuideDialouge = new Dialog(DashBoard.this, R.style.DialogMaxwidth);
        GuideDialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        GuideDialouge.setContentView(R.layout.guide);

        AVDialog = new Dialog(DashBoard.this, R.style.DialogMaxwidth);
        AudioRecordDialog = new Dialog(DashBoard.this, R.style.DialogMaxwidth);
        SendDiscardDialog = new Dialog(DashBoard.this, R.style.DialogMaxwidth);

        AVDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AudioRecordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SendDiscardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        AudioRecordDialog.setContentView(R.layout.record_audio);
        AVDialog.setContentView(R.layout.av_details);
        SendDiscardDialog.setContentView(R.layout.send_discard_dialog);

        av_email = (AutoCompleteTextView) AVDialog.findViewById(R.id.av_email);


        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);


    }



    private void callwebForContacts() {

        Contact.getInstance().ixpressemail.clear();
        Contact.getInstance().ixpressname.clear();
        Contact.getInstance().ixpress_user_pic.clear();


        ixprez_email.clear();
        ixprez_username.clear();
        ixprez_profilepic.clear();

        appendEmail.clear();
        appendName.clear();
        appendProfilePic.clear();


        Log.d("email contactttss", Contact.getInstance().email_list.toString());

        if (Contact.getInstance().email_list != null) {

            RestClient.get(DashBoard.this).PostContacts(new ContactsReq(Contact.getInstance().email_list),
                    new Callback<ContactsResp>() {
                        @Override
                        public void success(ContactsResp contactsResp, Response response) {

                            if (contactsResp.getCode().equals("200")) {

                                for (int i = 0; i < contactsResp.getData().length; i++) {

                                    ixprez_email.add(contactsResp.getData()[i].getEmail_id());
                                    ixprez_username.add(contactsResp.getData()[i].getUser_name());
                                    ixprez_profilepic.add(contactsResp.getData()[i].getProfile_image());

                                    //count=i++;
                                }

                                DatabaseHandler handler = new DatabaseHandler(DashBoard.this);
                                List<String> contacts = handler.getAllDetailsFormDatabase();
                                ixemailcount = 0;
                                /*if(DatabaseHandler.dbEmailList!=null &&
                                        DatabaseHandler.dbEmailList.size()>0){
                                    appendEmail.addAll(DatabaseHandler.dbEmailList);
                                    appendName.addAll(DatabaseHandler.dbNameList);
                                    appendProfilePic.addAll(DatabaseHandler.dbPicList);
                                }*/

                               // ixemailcount = ixprez_email.size();

                                // ixprez user from service

                                appendEmail.addAll(ixprez_email);
                                appendName.addAll(ixprez_username);
                                appendProfilePic.addAll(ixprez_profilepic);

                                // internal contacts list

                                appendEmail.addAll(Contact.getInstance().email_list);
                                appendName.addAll(Contact.getInstance().contact_namelist);
                                appendProfilePic.addAll(Contact.getInstance().contact_urilist);

                                Contact.getInstance().ixpressname = appendName;
                                Contact.getInstance().ixpressemail = appendEmail;
                                Contact.getInstance().ixpress_user_pic = appendProfilePic;
                                Contact.getInstance().xpressUser = "iXpressUser";


                            } else if (contactsResp.getCode().equals("202")) {
                              /*  Toast.makeText(DashBoard.this, "ixpress users...found " +
                                        "in your contacts...", Toast.LENGTH_SHORT).show();*/
                              //  recyclerView.setAdapter(new ContactsAdapter(cur, null));

                            } else {
                              /*  Toast.makeText(DashBoard.this, "Hmm,.. " +
                                        "Something went wrong...", Toast.LENGTH_SHORT).show();*/
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //recyclerView.setAdapter(new ContactsAdapter(cur, null));
                        }
                    });
        } else {
           /* Toast.makeText(DashBoard.this, "Hmm,.. " +
                    "No records found...", Toast.LENGTH_SHORT).show();*/
        }
    }

    private void mtd_getFeatured() {
        RestClient.get(context).GetFeatured(new featuredReq("video"),
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
                            Toast.makeText(_activity, "Featured Videos are not Available", Toast.LENGTH_LONG).show();

                        }
                        noofsize= featuredResp.getData().length;
                        Adapter_vp_horizontal adapter_vp_db = new Adapter_vp_horizontal(DashBoard.this,noofsize,user_name,user_img,thumb_url,time,likes,views,media,reactions,title,file_id,tags);
                        myPager.setOffscreenPageLimit(adapter_vp_db.getCount());
                        myPager.setAdapter(adapter_vp_db);
                        adapter_vp_db.notifyDataSetChanged();
                        // myPager.setOffscreenPageLimit(2);
                        // myPager.setCurrentItem(1,true);

                        new CoverFlow.Builder()
                                .with(myPager)
                                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.db_vp_margin))
                                .scale(0.01f)
                                .spaceSize(1f)
                                .rotationY(14f)
                                .build();

                        //  Toast.makeText(_activity, ""+media.size(), Toast.LENGTH_LONG).show();
                        mtd_counter();
                        //  mtd_getProfilePic();

                    }



                    @Override
                    public void failure(RetrofitError error) {

                        myPager = (ViewPagerCustom) findViewById(R.id.reviewpager);
                        mPagerContainer.setVisibility(View.GONE);
                    }});

    }

    private void mtd_load_list() {

        pb = (ProgressBar) findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.VISIBLE);


        layoutManager = new LinearLayoutManager(this);
        Rv_lists.setLayoutManager(layoutManager);
        adapterRcv = new adapter_dashboard(playlist,context,DashBoard.this);
        Rv_lists.setAdapter(adapterRcv);
        playlist.clear();
        Index = 0;
        getData();
        Index++;

        // show hide floating btn



        Rv_lists.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                pb.setVisibility(View.VISIBLE);
                Index ++;
                getData();
            }
        });

            //Swipe function

     /*  Rv_lists.setOnTouchListener(new View.OnTouchListener() {

            int downX, upX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    Log.i("event.getX()", " downX " + downX);
                    return true;
                }

                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    Log.i("event.getX()", " upX " + downX);
                    if (upX - downX > 100) {

                        // swipe right
                        TabLayout.Tab tab = tabLayout.getTabAt(1);
                        tab.select();
                    }

                    else if (downX - upX > -100) {

                        // swipe left
                        TabLayout.Tab tab = tabLayout.getTabAt(0);
                        tab.select();
                    }
                    return true;

                }
                return false;
            }
        });*/


    }
    private void getData() {
        swipeRefreshLayout.setRefreshing(true);
        pb = (ProgressBar) findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.VISIBLE);
        String emotion = "Like";
      //  Toast.makeText(context, ""+Index, Toast.LENGTH_SHORT).show();
        if(Api_Popular){


            RestClient.get(context).MyPublicLists_Popular(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new PublicPlayListReq(Integer.toString(Index), "10",sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),emotion),
                    new Callback<PlayListResp_emotion>() {
                        @Override
                        public void success(final PlayListResp_emotion arg0, Response arg1) {

                            if (arg0.getCode().equals("200")) {

                                ParsePublicFiles(arg0);

                            } else if (arg0.getCode().equals("601")) {
                                RefreshTokenMethodName = "getData";

                            } else if (arg0.getCode().equals("202")) {
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                            }else if(arg0.getData().getLast().equals("1")||arg0.getData().getRecords().length == 0){
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, "End Of List.. ", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();
                            pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                            pb.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
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
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                            } else if (arg0.getCode().equals("202")) {
                                Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);
                            }else {
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);
                                Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                            }

                            if(arg0.getData().getLast().equalsIgnoreCase("1")){
                                pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                                pb.setVisibility(View.GONE);

                                if(!last){
                                    last = true;
                                    Toast.makeText(context, "End Of List.. ", Toast.LENGTH_LONG).show();
                                }

                            }
                            //  Index = Index + 1;
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(context, "Unable to Connect", Toast.LENGTH_LONG).show();
                            pb = (ProgressBar) findViewById(R.id.progressBar_dash);
                            pb.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

        }}

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
                    , iii.get_id(), iii.getTags(),iii.getLikeCount(),iii.getView_count(),iii.getIsUserLiked(),sb.toString(),iii.getEmotionCount()
                    ,iii.getIsuerfollowing(),iii.getFieldstatus(),iii.getTo_email(),iii.getFrom_user());

            playlist.add(playlistItems);
        }
        EndOfRecords = arg0.getData().getLast();

        pb = (ProgressBar) findViewById(R.id.progressBar_dash);
        pb.setVisibility(View.GONE);
        iv_no_data.setVisibility(View.INVISIBLE);
        adapterRcv.notifyDataSetChanged();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }



    private void mtd_counter() {
        tv_counter .setVisibility(View.GONE);
        tv_counter_nb.setVisibility(View.GONE);
        if(!iSnotificationClicked){
            PreviousNotificOUNT = "0";
            iSnotificationClicked=false;
        }

        RestClient.get(context).CounterFeelings(new CounterReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),PreviousNotificOUNT),
                new Callback<CounterResp>() {
                    @Override
                    public void success(CounterResp counterResp, Response response) {

                       if (counterResp.getCode().equals("404")){
                            ToastCustom tc = new ToastCustom(DashBoard.this);
                           SpannableString ss =  new SpannableString("Oops Our server is too worked up to let you xpress today.");
                           tc.ShowToast("Server Down",ss,2);
                        }else {

                           String totalxpressions = "0";

                           totalxpressions = counterResp.getData().getTotalNumberofrecords();



                           if (totalxpressions.equals("0") || totalxpressions == null) {
                               tv_counter.setVisibility(View.GONE);
                           } else {

                               TotalCount = Integer.parseInt(totalxpressions);
                               int tc = TotalCount;
                               int tc_point = 0;

                               //  tv_counter.setVisibility(View.VISIBLE);
                               // tv_counter.setText(counterResp.getData().getTotalNumberofrecords());
                               TopBadge.setMaxTextLength(5);

                               if(tc >=1000 && tc < 10000){

                                   tc =Integer.parseInt(Integer.toString(tc).substring(0, 1));



                                   TopBadge.setText(""+tc+" K");

                               }else if(tc >=10000 && tc < 100000){

                                   tc =Integer.parseInt(Integer.toString(tc).substring(0, 2));
                                   TopBadge.setText(""+tc+" K");
                               }
                               else if(tc >= 100000 && tc < 10000000){

                                   tc =Integer.parseInt(Integer.toString(tc).substring(0, 3));

                                   TopBadge.setText(""+tc+" K");
                               }
                              else  {
                                   TopBadge.setText(""+tc);
                               }


                               //  TopBadge.setText(counterResp.getData().getTotalNumberofrecords());


                           }

                            if(counterResp.getData().getTotalNumberofrecords().equalsIgnoreCase("0")){
                                tv_counter_nb.setVisibility(View.GONE);
                            }else {
                                tv_counter_nb.setVisibility(View.VISIBLE);
                                tv_counter_nb.setText(counterResp.getData().getTotalNumberofrecords());
                            }


                            // pvt notificount

                            if(counterResp.getData().getPrivateCount().equalsIgnoreCase("0")){
                                tv_PvtCount.setVisibility(View.GONE);
                            }else {
                                tv_PvtCount.setText(" "+counterResp.getData().getPrivateCount()+" ");
                                tv_PvtCount.setVisibility(View.VISIBLE);
                            }

                            if(counterResp.getData().getPrivateFollowCount().equalsIgnoreCase("0")){
                                tv_notifi_count.setVisibility(View.GONE);
                            }else {
                                PreviousNotificOUNT = counterResp.getData().getPrivateFollowCount();
                                tv_notifi_count.setText(" "+counterResp.getData().getPrivateFollowCount()+" ");
                                tv_notifi_count.setVisibility(View.VISIBLE);
                            }


                            String imgUrl = "" ;
                            if(counterResp.getData().getProfileImage()!= null){
                                imgUrl = counterResp.getData().getProfileImage();
                            }
                            if (imgUrl.contains(StaticConfig.ROOT_URL_Media)) {
                                imgUrl = StaticConfig.ROOT_URL + imgUrl.replace(StaticConfig.ROOT_URL_Media, "");
                            } else {
                                imgUrl = StaticConfig.ROOT_URL + "/" +imgUrl;
                            }

                            Glide.with(context).load(imgUrl).dontAnimate().placeholder(R.drawable.ic_user_icon)
                                    .fitCenter().into(iv_user_pic);

                            Glide.with(context).load(imgUrl).dontAnimate().placeholder(R.drawable.ic_user_icon)
                                    .centerCrop().into(iv_user_pic_nb);
                            StatiConstants.user_profilepic_url = imgUrl;
                            StatiConstants.followers_following = counterResp.getData().getUserFollowers()+" Following - "+counterResp.getData().getFollowingCount()+" Followers.";
                            tv_nb_userName.setText(sharedpreferences.getString(SharedPrefUtils.SpUserName, "----"));

                       }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


    }

    private void mtd_guide_view_pager() {
       final Dialog GuideDialog = new Dialog(DashBoard.this,R.style.guideDiaoluge);

       GuideDialog.setContentView(R.layout.guide_dashboard_new);
       GuideDialog.show();
        RelativeLayout rl_guide = (RelativeLayout) GuideDialog.findViewById(R.id.rl_guide);
            rl_guide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuideDialog.dismiss();
                }
            });
       editor.putBoolean(SharedPrefUtils.SpGuideDiplayed,true);
       editor.commit();

    }



    @Override
    protected void onPause() {
        super.onPause();
        is_vp_Touched = true;
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DashboardFragment_popular(), "Trending");
        adapter.addFragment(new DashboardFragment_recent(), "Recent");
        viewPager.setAdapter(adapter);
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


            Intent videoIntent = new Intent(getApplicationContext(),Actvity_video.class);
            videoIntent.putExtra("url",Selected_file_url);
            videoIntent.putExtra("type",selected_file_mime);
            videoIntent.putExtra("likes",playlistItems.getLikesCount());
            videoIntent.putExtra("views",playlistItems.getViewsCount());
            videoIntent.putExtra("file_id",playlistItems.getFileID());
            videoIntent.putExtra("title",playlistItems.getTitle());
            videoIntent.putExtra("tags",playlistItems.getTags());
            videoIntent.putExtra("upload_date",playlistItems.getCreatedDate());
            videoIntent.putExtra("isliked",playlistItems.getIsUserLiked());
            videoIntent.putExtra("img_url",TBPath);
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
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).FollowRequest(new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Followed successfully.", Toast.LENGTH_SHORT).show();

                        mtd_counter();
                        mtd_load_list();
                        adapterRcv.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    @Override
    public void UnSubcribed(int position, String email) {
        sharedpreferences = getApplicationContext().getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        RestClient.get(getApplicationContext()).UnFollowRequest(new FollowReq(email,sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<FollowRep>() {
                    @Override
                    public void success(FollowRep followRep, Response response) {
                        Toast.makeText(getApplicationContext(), "Unfollowed successfully.", Toast.LENGTH_SHORT).show();

                        mtd_counter();
                        mtd_load_list();
                        adapterRcv.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
      //  mtd_pvtCount();
        //Change value of like count here based on input on AVDisply Screen

        if( Actvity_video.LikeChangedValue != 0){


            PlayListitems_emotion  playlistItemstemp = playlist.get(previousposition);

        // is userlike derevative

            int lkin = Integer.parseInt(playlistItemstemp.getLikesCount());

            if(lkin < Actvity_video.LikeChangedValue){

                playlistItemstemp.setIsUserLiked("1");

            }else {

                playlistItemstemp.setIsUserLiked("0");

            }

            int vC = Integer.parseInt(playlistItemstemp.getViewsCount());
            vC++;
            playlistItemstemp.setViewsCount(""+vC);
            playlistItemstemp.setLikesCount(""+Actvity_video.LikeChangedValue);
            playlist.remove(previousposition);
            playlist.add(previousposition,playlistItemstemp);

            adapterRcv.notifyDataSetChanged();
        }
        is_vp_Touched = false ;

        try {
            mtd_counter();

            iSnotificationClicked=false;
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

private void mtd_contacts_reader(){

    Contact.getInstance().contact_namelist.clear();
    Contact.getInstance().email_list.clear();
    Contact.getInstance().contact_urilist.clear();

    Contact.getInstance().ixpressemail.clear();
    Contact.getInstance().ixpressname.clear();
    Contact.getInstance().ixpress_user_pic.clear();

    ContentResolver cr = this.getContentResolver();
    String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
    String order = "CASE WHEN "
            + ContactsContract.Contacts.DISPLAY_NAME
            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
            + ContactsContract.Contacts.DISPLAY_NAME
            + ", "
            + ContactsContract.CommonDataKinds.Email.DATA
            + " COLLATE NOCASE";
    String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
    cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
            filter, null, order);

    mNameColIdx = cur.getColumnIndex(ContactsContract.Contacts.
            DISPLAY_NAME_PRIMARY);
    mIdColIdx = cur.getColumnIndex(ContactsContract.Contacts._ID);

    for (int t = 0; t < cur.getCount(); t++) {
        cur.moveToPosition(t);
        String contactName = cur.getString(mNameColIdx);
        long contactId = cur.getLong(mIdColIdx);
        String email = cur.getString(cur.getColumnIndex
                (ContactsContract.CommonDataKinds.Email.DATA));
        String contact_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId).toString();

        Contact.getInstance().contact_namelist.add(contactName);
        Contact.getInstance().contact_urilist.add(contact_uri);
        Contact.getInstance().email_list.add(email);

        ArrayList<String> e = Contact.getInstance().email_list;
        ArrayList<String> n = Contact.getInstance().contact_namelist;


           /* Contact.getInstance().contactusername = contact_namelist;
            Contact.getInstance().contactuseremail = email_list;
            Contact.getInstance().contact_user_pic = contact_urilist;*/
        //      Log.d("email", email);
    }
    callwebForContacts();
}



    private void mtd_pvtCount() {

        RestClient.get(this).GetPvateCount(new ReqPvateCount(sharedpreferences.getString(SharedPrefUtils.SpEmail, "")), new Callback<AcceptRejectCount>() {
            @Override
            public void success(AcceptRejectCount acceptRejectCount, Response response) {
                for (int i = 0; i < acceptRejectCount.getData().length; i++) {
                   count = acceptRejectCount.getData()[i].getCount();

                }
            }

            @Override
            public void failure(RetrofitError error) {

                count="";

            }
        });
        tv_PvtCount.setText(count);

    }




    private void mtd_resize() {
        ViewTreeObserver observer = rl_btm.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                rl_ht =  rl_btm.getHeight();
                rl_btm.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager_dash_lists.getLayoutParams();
                lp.bottomMargin += rl_ht;

                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) swipeRefreshLayout.getLayoutParams();
                lp2.bottomMargin += rl_ht;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (Smenu.isMenuShowing()== true) {
            Smenu.toggle();
        } else {
            if(exit){

                super.onBackPressed();
                finish();
                return;
            }

            exit = true;
            Toast.makeText(getApplicationContext(),"Press Back Again to exit",Toast.LENGTH_LONG).show();
        }


    }






    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.GET_ACCOUNTS}, 93);

                    NoPermisson =false;
                    mtd_contacts_reader();

                } else {

                    NoPermisson = true;
                    Toast.makeText(_activity, "This Permission is Required for the application to perform all basic functions, Kindly accept. For more information kindly vist our website.", Toast.LENGTH_LONG).show();


                }

                break;
        }
    }

    public void CheckAndRequestPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+
            /*if (checkPermission(Permission4)) {
               // Intent2Activity();
                return;
            }*/
            requestPermission(PermissionStrings.GET_ACCOUNTS);


        } else {
            // Intent2Activity();
        }
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, PERMISSION_REQUEST_CODE);
    }



}

