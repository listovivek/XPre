package com.quad.xpress;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
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
import com.quad.xpress.OOC.ToastCustom;
import com.quad.xpress.contacts.Contact;
import com.quad.xpress.contacts.ContactMainActivity;
import com.quad.xpress.contacts.FullContactDBOBJ;
import com.quad.xpress.contacts.FullContactsDBhandler;
import com.quad.xpress.contacts.contactSyncService;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.models.counter.CounterReq;
import com.quad.xpress.models.counter.CounterResp;
import com.quad.xpress.models.featuredVideos.featuredReq;
import com.quad.xpress.models.featuredVideos.featuredResp;
import com.quad.xpress.uploadsFragments.act_my_uploads;
import com.quad.xpress.utills.StatiConstants;
import com.quad.xpress.utills.StaticConfig;
import com.quad.xpress.utills.ViewPagerCustom;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.NetConnectionDetector;
import com.quad.xpress.utills.helpers.PermissionStrings;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.commons.lang3.StringUtils;

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


public class DashBoard extends AppCompatActivity {


    Context context;
    Dialog AVDialog,GuideDialouge;
    ImageView iv_no_data;
    ProgressBar pb;
    Boolean exit = false;
    Boolean is_vp_Touched = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    int rl_ht;
    Cursor cursorPhone;
    int counter;
    public static String FileNameWithMimeType;
    RecyclerView Rv_lists;
    Boolean isLoadingContacts = false;
    Dialog pdialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String AppName;
    Activity _activity;
    ImageButton dash_receive;
    ImageButton DashPublicPlaylist;
    ImageButton AudioFab;
    ImageButton VideoFab;
    TextView tv_nb_MyUploads,tv_nb_userName,tv_nb_contacts,tv_nb_help,tv_nb_support,tv_nb_about;
    ImageButton btn_nb_exit,btn_nb_settings;
     SlidingMenu Smenu;
    Adapter_vp_horizontal adapter_vp_db;
    FrameLayout fl_counter;
    NetConnectionDetector CD;
    Toast NoInternet = null;
    ViewPagerAdapter vp_frga_adapter;
    NetConnectionDetector NDC;
    RelativeLayout rl_btm;
    ViewPager viewPager_dash_lists;
    String count="",PreviousNotificOUNT;
    TabLayout tabLayout;
    Boolean guide;
    CoordinatorLayout cl;
    ProgressBar pbar;
    Dialog GuideDialog;
   /* Intent slider_intent;*/
    int noofsize = 0;
    int noofsizeguide = 3,currentPage;
    ViewPagerCustom myPager = null;
    PagerContainer mPagerContainer;
    ImageButton btn_btm_settings,btn_btm_notifi,btn_tb_navigation,btn_nb_nav;

    //Data for vp
    final ArrayList<String>user_name = new ArrayList<>();
    final ArrayList<String>user_img= new ArrayList<>();
    final ArrayList<String>thumb_url= new ArrayList<>();
    final ArrayList<String>time= new ArrayList<>();
   static final ArrayList<String>likes= new ArrayList<>();
   static final ArrayList<String>views = new ArrayList<>();
    final ArrayList<String>media= new ArrayList<>();
    final ArrayList<String>reactions= new ArrayList<>();
    final ArrayList<String>title= new ArrayList<>();
    final ArrayList<String>file_id= new ArrayList<>();
    final ArrayList<String>tags= new ArrayList<>();
    final ArrayList<String>isUserLiked= new ArrayList<>();
    CircleImageView iv_user_pic,iv_user_pic_nb;
    Toolbar toolbar;
    TextView tv_counter,tv_counter_nb;
 //   SwipeRefreshLayout swipeRefreshLayout;
    static Boolean iSnotificationClicked =false;
    public static    ArrayList<String> ixprez_email = new ArrayList<>();
    int TotalCount;
    NotificationBadge TopBadge,tv_PvtCount,tv_notifi_count;;
    private boolean NoPermisson  = true;
    FullContactsDBhandler fDB;
    TextView tv_guide_prog;
    List<FullContactDBOBJ> Dbcontacts;

    @Override
    protected void onStart() {
        super.onStart();

        vp_frga_adapter = new ViewPagerAdapter(getSupportFragmentManager());

        vp_frga_adapter.addFragment(new DashboardFragment_pop(), "Trending");
        vp_frga_adapter.addFragment(new DashboardFragment_new(), "Recent");
        // vp_frga_adapter.addFragment(new DashboardFragment_pop_autoplay(), "Autoplay");
        viewPager_dash_lists.setAdapter(vp_frga_adapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                try {
                    Fragment f = vp_frga_adapter.getItem(tab.getPosition());
                    if (f != null) {
                        View fragmentView = f.getView();
                        RecyclerView mRecyclerView;
                        if(tab.getPosition() == 0) {

                            mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerView_frag_pop);
                        }else {
                            mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerView_frag_recent);
                        }

                        if (mRecyclerView != null)
                            mRecyclerView.scrollToPosition(0);
                    }
                } catch (NullPointerException npe) {
                }


            }
        });


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager_dash_lists);
            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nb);


        AppName = getResources().getString(R.string.app_name);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        guide = sharedpreferences.getBoolean(SharedPrefUtils.SpGuideDiplayed, false);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        btn_tb_navigation = (ImageButton) findViewById(R.id.navigation_ic_tb);
        Rv_lists = (RecyclerView) findViewById(R.id.rv_dash);
        tv_counter = (TextView) findViewById(R.id.textView_tb_counter);
        cl = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        TopBadge = (NotificationBadge) findViewById(R.id.tb_db_badge);
        fl_counter = (FrameLayout) findViewById(R.id.fl_couter_holder);
        fDB = new FullContactsDBhandler(DashBoard.this);
        _activity = this;
        StaticConfig.IsPublicActivity = true;
        pb = (ProgressBar) findViewById(R.id.progressBar_dash);
        NDC = new NetConnectionDetector();


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager_dash_lists = (ViewPager) findViewById(R.id.viewPager_dash);
        pdialog = new Dialog(context);

        fl_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   ToastCustom toastCustom = new ToastCustom(DashBoard.this);
                String tc = TotalCount+"";
                final SpannableString ss = new SpannableString(tc+" Xpressions and counting ...");

                ss.setSpan(new ForegroundColorSpan(Color.RED), 0, tc.length(), 0);
                ss.setSpan(new RelativeSizeSpan(0f), 0, tc.length(), 0);



                LayoutInflater inflater=_activity.getLayoutInflater();
                View customToastroot =inflater.inflate(R.layout.toast_counts, null);
                Toast customtoastx=new Toast(context);
                customtoastx.setView(customToastroot);
                TextView textTitle = (TextView) customToastroot.findViewById(R.id.tv_toast_title);
                textTitle.setText("iXprez");
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
                Intent intentMyuploads = new Intent(DashBoard.this,act_my_uploads.class);
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

                String shareBody = "Hi! Check out this app, iXprez, I am using to express myself!";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "iXprez");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));


            }
        });
        tv_nb_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!NoPermisson) {
                        Intent in_contact = new Intent(DashBoard.this, ContactMainActivity.class);
                        startActivity(in_contact);

                    }else {
                        CheckAndRequestPermission();
                    }
                }else {
                    Intent in_contact = new Intent(DashBoard.this, ContactMainActivity.class);
                    startActivity(in_contact);
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


         /*   myPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(context, "vp  "+myPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });*/

            mPagerContainer.setPageItemClickListener(new PageItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {


                    if(position != -1) {

                        Intent video_act = new Intent(context, Actvity_video.class);
                        String MediaPath, TBPath;
                        if (media.get(position).contains(StaticConfig.ROOT_URL_Media)) {
                            MediaPath = StaticConfig.ROOT_URL + media.get(position).replace(StaticConfig.ROOT_URL_Media, "");
                        }  else if  (media.get(position).contains("https")){
                            MediaPath = media.get(position);
                          //  Toast.makeText(context, ""+MediaPath, Toast.LENGTH_SHORT).show();
                        }else {
                            MediaPath = StaticConfig.ROOT_URL + "/" + media.get(position);
                        }
                        if (media.get(position).contains(StaticConfig.ROOT_URL_Media)) {
                            TBPath = StaticConfig.ROOT_URL + media.get(position).replace(StaticConfig.ROOT_URL_Media, "");
                        } else if  (media.get(position).contains("https")){
                            TBPath = media.get(position);
                        } else {
                            TBPath = StaticConfig.ROOT_URL + "/" + media.get(position);
                        }

                        video_act.putExtra("url", MediaPath);
                        video_act.putExtra("thumbImg", TBPath);
                        video_act.putExtra("type", "video");
                        video_act.putExtra("likes", likes.get(position));
                        video_act.putExtra("views", views.get(position));
                        video_act.putExtra("file_id", file_id.get(position));
                        video_act.putExtra("title", title.get(position));
                        video_act.putExtra("tags", tags.get(position));
                        video_act.putExtra("upload_date", time);
                        video_act.putExtra("isliked", isUserLiked.get(position));
                        video_act.putExtra("img_url", TBPath);
                        video_act.putExtra("isPrivate", "false");
                        video_act.putExtra("fromfeatured",true);
                        startActivity(video_act);
                    }


                //    user_name,user_img,thumb_url,time,likes,views,media,reactions,title,file_id,tags

                }
            });

        mtd_getFeatured_bkp();

        myPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                is_vp_Touched = true;
                myPager.setScrollDuration(300);

                return false;

            }
        });



        btn_nb_exit= (ImageButton) findViewById(R.id.btn_exit_nb);
        btn_nb_settings= (ImageButton) findViewById(R.id.btn_settings_nb);

        btn_nb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
               /* final Dialog proceedDiscardDialog = new Dialog(DashBoard.this,
                        R.style.Theme_Transparent);
                String a = "Are you sure you want to logout ?.\n\nYou will still receive notifications if haven't turned it off in your settings.";
               // String a = "Are you sure you want to logout ?";
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

                      *//*  editor.clear();
                        editor.commit();*//*
                        finish();
                      *//*  Intent intent = new Intent(DashBoard.this, GcmIntentService.class);
                        intent.putExtra("key", "UNSUBSCRIBE");
                        startService(intent);*//*
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
                lt.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
                myPager.setLayoutTransition(lt);



            }
        };

        Timer   timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 4000);




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


        }

        //guide = false;







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
        mtd_resize();

        if(!guide) {
            mtd_guide_view_pager();

        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                CheckAndRequestPermission();

            }
            else{

                if(guide) {
                    Intent intent = new Intent(this, contactSyncService.class);
                    startService(intent);
                }else {
                new ContactsReaderClass().execute();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }





    }


    private class ContactsReaderClass extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoadingContacts = true;
            pbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            Contact.getInstance().email_list.clear();
            Contact.getInstance().contact_namelist.clear();
            Contact.getInstance().contact_urilist.clear();
            Contact.getInstance().ContactPairs_phone.clear();
            Contact.getInstance().email_name.clear();

            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            String CONTACT_LAST_UPDATED_TIMESTAMP = ContactsContract.RawContacts.VERSION;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            ContentResolver contentResolver = getContentResolver();
            cursorPhone = contentResolver.query(CONTENT_URI, null,null, null, null);


            // Iterate every contact in the phone
            if (cursorPhone.getCount() > 0) {
                counter = 0;
                while (cursorPhone.moveToNext()) {

                    String contact_id = cursorPhone.getString(cursorPhone.getColumnIndex(_ID));
                    String name = cursorPhone.getString(cursorPhone.getColumnIndex(DISPLAY_NAME));
                    int hasPhoneNumber = Integer.parseInt(cursorPhone.getString(cursorPhone.getColumnIndex(HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        //This is to read multiple phone numbers associated with the same contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                            // Log.d("pno",output.toString());

                        }
                        phoneCursor.close();


                        if (name != null && phoneNumber != null && FieldsValidator.isPhoneNumberString(phoneNumber, true)) {

                            //phonecontactList.add(output.toString());

                            Contact.getInstance().email_list.add(phoneNumber.replace(" ", ""));
                            Contact.getInstance().contact_namelist.add(name);
                            Contact.getInstance().contact_urilist.add(String.valueOf(R.drawable.ic_user_icon));
                            Contact.getInstance().ContactPairs_phone.put(phoneNumber, name);
                            fDB.addContact(new FullContactDBOBJ(name.trim(), phoneNumber.replace(" ", "").trim(),String.valueOf(R.drawable.ic_user_icon), "false","yes"));
                            phoneNumber = null;

                        }

                    }
                }



            }
            return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        callwebForContacts();
    }
}
    private void callwebForContacts() {

        SharedPreferences sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);



            RestClient.get(this).PostPhoneContacts(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new ContactsReq(Contact.getInstance().email_list),
                    new Callback<ContactsResp>() {
                        @Override
                        public void success(ContactsResp contactsResp, Response response) {

                            Contact.getInstance().ixpressemail.clear();
                            Contact.getInstance().ixpressname.clear();
                            Contact.getInstance().ixpress_user_pic.clear();
                            Contact.getInstance().is_ixpress_user.clear();


                            if (contactsResp.getCode().equals("200")) {

                                for (int i = 0; i < contactsResp.getData().length; i++) {

                               fDB.addContact(new FullContactDBOBJ(contactsResp.getData()[i].getUser_name().trim(), contactsResp.getData()[i].getEmail_id().trim().toLowerCase(), contactsResp.getData()[i].getProfile_image().trim(), "true","yep"));


                                }




                                if(!guide){
                                    pbar.setVisibility(View.GONE);
                                    isLoadingContacts = false;
                                    tv_guide_prog.setVisibility(View.GONE);

                                }
                                Dbcontacts = fDB.getAllContacts();

                                for (FullContactDBOBJ cn : Dbcontacts) {

                                    if(cn.get_ixprezuser().equalsIgnoreCase("true")){

                                        Contact.getInstance().ixpressemail.add(cn.getPhoneNumber());
                                        Contact.getInstance().ixpressname.add(cn.getName());
                                        Contact.getInstance().ixpress_user_pic.add(cn.get_profile_pic());
                                        Contact.getInstance().is_ixpress_user.add(true);
                                    }

                                }



                                new ContactsReaderEmail().execute();

                            }  else {

                                   new ContactsReaderEmail().execute();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(DashBoard.this, "Contacts Sync failed miserably...  ", Toast.LENGTH_SHORT).show();

                            new ContactsReaderEmail().execute();
                                }
                    });


    }

    private class ContactsReaderEmail extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            if(!guide) {

                ContentResolver contentResolver = getContentResolver();
                Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                if (cur.getCount() > 0) {

                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor cur1 = contentResolver.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (cur1.moveToNext()) {

                            String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String emails = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                            if (emails != null && emails.length() > 5) {
                               // Log.d("EMAI", emails);
                                if (name != null && !name.contains("@")) {

                                    fDB.addContact(new FullContactDBOBJ(StringUtils.abbreviate(name, 18), emails.trim().toLowerCase(), String.valueOf(R.drawable.ic_user_icon), "false","nope"));


                                } else {
                                    String val[] = name.split("@");
                                    fDB.addContact(new FullContactDBOBJ(StringUtils.abbreviate(val[0], 18), emails.trim().toLowerCase(), String.valueOf(R.drawable.ic_user_icon), "false","nope"));

                                }
                            }


                        }
                        cur1.close();
                    }


                }
                cur.close();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

          }



    }

    private void mtd_getFeatured_bkp() {

        user_name.clear();
        user_img.clear();
        thumb_url.clear();
        time.clear();likes.clear();
        views.clear();media.clear();
        reactions.clear();title.clear();
        file_id.clear();
        tags.clear();
        isUserLiked.clear();


        //   adapter_vp_db.notifyDataSetChanged();


        RestClient.get(context).GetFeatured(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new featuredReq("video",sharedpreferences.getString(SharedPrefUtils.SpEmail, "")),
                new Callback<featuredResp>() {

                    @Override
                    public void success(featuredResp featuredResp, Response response) {
                        if(featuredResp.getCode().equals("200")){
                            for (int i=0 ; i < featuredResp.getData().length;i++){
                                user_name.add(featuredResp.getData()[i].getUsername());
                                user_img.add(featuredResp.getData()[i].getProfilePicture());
                                thumb_url.add(featuredResp.getData()[i].getThumbtokenizedUrl());
                                time.add(featuredResp.getData()[i].getTimePost());
                                likes.add(featuredResp.getData()[i].getLikeCount());
                                views.add(featuredResp.getData()[i].getView_count());
                                media.add(featuredResp.getData()[i].getTokenizedUrl());
                                reactions.add(featuredResp.getData()[i].getSmailyCount());
                                title.add(featuredResp.getData()[i].getTitle());
                                file_id.add(featuredResp.getData()[i].get_id());
                                tags.add(featuredResp.getData()[i].getTags());
                                isUserLiked.add(featuredResp.getData()[i].getLiked());

                                // Toast.makeText(_activity, "rved"+user_name.get(0), Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(_activity, "Featured Videos are not Available", Toast.LENGTH_LONG).show();

                        }
                        noofsize= featuredResp.getData().length;
                        adapter_vp_db = new Adapter_vp_horizontal(DashBoard.this,noofsize,user_name,user_img,thumb_url,time,likes,views,media,reactions,title,file_id,tags,isUserLiked);
                        myPager.setAdapter(adapter_vp_db);
                        myPager.setOffscreenPageLimit(adapter_vp_db.getCount());
                        mPagerContainer.setVisibility(View.VISIBLE);

                    //    myPager.setOffscreenPageLimit(2);

                        new CoverFlow.Builder()
                                .with(myPager)
                                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.db_vp_margin))
                                .scale(0.01f)
                                .spaceSize(1f)
                                .rotationY(14f)
                                .build();

                        adapter_vp_db.notifyDataSetChanged();


                    }



                    @Override
                    public void failure(RetrofitError error) {

                        myPager = (ViewPagerCustom) findViewById(R.id.reviewpager);
                        mPagerContainer.setVisibility(View.GONE);


                    }});

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

        RestClient.get(context).CounterFeelings(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new CounterReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),PreviousNotificOUNT),
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
                                   tv_counter_nb.setText(""+tc+" K");

                               }else if(tc >=10000 && tc < 100000){

                                   tc =Integer.parseInt(Integer.toString(tc).substring(0, 2));
                                   TopBadge.setText(""+tc+" K");
                                   tv_counter_nb.setText(""+tc+" K");
                               }
                               else if(tc >= 100000 && tc < 10000000){

                                   tc =Integer.parseInt(Integer.toString(tc).substring(0, 3));

                                   TopBadge.setText(""+tc+" K");
                                   tv_counter_nb.setText(""+tc+" K");
                               }
                              else  {
                                   TopBadge.setText(""+tc);
                                   tv_counter_nb.setText(""+tc);
                               }


                               //  TopBadge.setText(counterResp.getData().getTotalNumberofrecords());


                           }

                            if(counterResp.getData().getTotalNumberofrecords().equalsIgnoreCase("0")){
                                tv_counter_nb.setVisibility(View.GONE);
                            }else {
                                tv_counter_nb.setVisibility(View.VISIBLE);
                              //  tv_counter_nb.setText(counterResp.getData().getTotalNumberofrecords());
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
                            }

                            else {
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
                            }
                            else if  (imgUrl.contains("https")){
                                imgUrl = counterResp.getData().getProfileImage();
                            }
                            else {
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




       GuideDialog = new Dialog(DashBoard.this, R.style.guideDiaoluge);

       GuideDialog.setContentView(R.layout.guide_dashboard_new);
       GuideDialog.setCancelable(false);
       GuideDialog.show();

        pbar = GuideDialog.findViewById(R.id.progressBar_guide);
        tv_guide_prog = GuideDialog.findViewById(R.id.tv_guide_progress);
        pbar.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pbar.setTooltipText("Sync in progress..");
        }
        RelativeLayout rl_guide = (RelativeLayout) GuideDialog.findViewById(R.id.rl_guide);
            rl_guide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isLoadingContacts){

                    guide = true;
                    editor.putBoolean(SharedPrefUtils.SpGuideDiplayed,true);
                    editor.commit();
                    GuideDialog.dismiss();
                    }else{
                        Toast.makeText(context, "Please wait for initial run...", Toast.LENGTH_SHORT).show();
                    }
                }
            });



    }



    @Override
    protected void onPause() {
        super.onPause();
        is_vp_Touched = true;


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






      if(Actvity_video.isFromFeatured){

           mtd_getFeatured_bkp();

             }else{

        }

        is_vp_Touched = false ;

        try {
            
            mtd_counter();
            iSnotificationClicked=false;

        } catch (Exception e) {
            e.printStackTrace();
        }

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

               /* ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) swipeRefreshLayout.getLayoutParams();
                lp2.bottomMargin += rl_ht;*/
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

       /* if (emailSyncDisp!=null && !emailSyncDisp.isDisposed()) {
            emailSyncDisp.dispose();
        }*/

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
            Toast.makeText(getApplicationContext(),"Press Back Again to Exit",Toast.LENGTH_LONG).show();
        }


    }






    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{PermissionStrings.GET_ACCOUNTS}, 93);

                    NoPermisson = false;

                    if(guide) {
                        Intent intent = new Intent(this, contactSyncService.class);
                        startService(intent);
                    }else {
                        new ContactsReaderClass().execute();
                    }

                } else {

                    NoPermisson = true;

                    Toast.makeText(_activity, "This Permission is Required for the application to perform all basic functions, Kindly accept. For more information kindly vist our website.", Toast.LENGTH_LONG).show();


                }

                break;
        }
    }

    public void CheckAndRequestPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // Marshmallow+

            requestPermission(PermissionStrings.GET_ACCOUNTS);


        } else {
                                          // Intent2Activity();
        }
    }

    private void requestPermission(String Permission) {
        ActivityCompat.requestPermissions(_activity, new String[]{Permission}, PERMISSION_REQUEST_CODE);
    }


}

