package com.quad.xpress.models.tagList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.R;
import com.quad.xpress.models.abuse_resp.Abuse_Req;
import com.quad.xpress.models.abuse_resp.Abuse_response;
import com.quad.xpress.Utills.helpers.LoadingDialog;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 7/25/2016.
 */
public class Tag_cloud_activity_new extends AppCompatActivity {
    TextView btn_post,btn_cancel,btn_abuse;
    EditText et_abuse;
    Boolean abuseOrnot=false;
    Context context;
    ActionBar actionBar;
    List<TagsModel> tag_list_data = new ArrayList<>();
    int pos=0;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public LoadingDialog LD;
    static String file_id;
    String description="No Value",file_type= "video";

    RecyclerView rv_tag_list;
    TagsAdapter_list_view adapter;
    LinearLayout Ll_btm,Ll_btm_CP;
    InputMethodManager inputMethodManager;
    TextView tv_tb_title;
    ImageButton btn_back_tb;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_list_commnets);
        context = getApplicationContext();




        Ll_btm_CP = (LinearLayout) findViewById(R.id.linearLayout_btm_CP);
        Ll_btm = (LinearLayout) findViewById(R.id.linearLayout_btm);
        btn_post = (TextView) findViewById(R.id.textView_post);
        btn_cancel = (TextView) findViewById(R.id.textView_cancel);
        btn_abuse = (TextView) findViewById(R.id.textView_abuse);
        tv_tb_title = (TextView) findViewById(R.id.tb_normal_title);
        btn_back_tb= (ImageButton) findViewById(R.id.tb_normal_back);
        tv_tb_title.setText("React");
        btn_back_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



        LD = new LoadingDialog(Tag_cloud_activity_new.this);
        rv_tag_list = (RecyclerView) findViewById(R.id.Rv_tags_list);
        et_abuse = (EditText) findViewById(R.id.editText_report_abuse);
        et_abuse.setVisibility(View.INVISIBLE);
        Intent in =getIntent();
        pos= in.getIntExtra("pos",0);
        file_id = in.getStringExtra("file_id");
        file_type = in.getStringExtra("file_type");
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        getTagData();

        adapter = new TagsAdapter_list_view(tag_list_data,context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_tag_list.setLayoutManager(mLayoutManager);
        rv_tag_list.setItemAnimator(new DefaultItemAnimator());
        rv_tag_list.setAdapter(adapter);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!abuseOrnot){
                    finish();
                }else {
                    abuseOrnot = false;
                    btn_abuse.setVisibility(View.VISIBLE);
                    et_abuse.setVisibility(View.INVISIBLE);
                    Ll_btm.setOrientation(LinearLayout.HORIZONTAL);
                    et_abuse.clearFocus();

                    inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(et_abuse.getWindowToken(), 0);
                    btn_cancel.setVisibility(View.INVISIBLE);
                    btn_post.setText("Back");
                    description = et_abuse.getText().toString();
                    if(description.length()  < 10){

                      Toast.makeText(getApplicationContext(),"Minimum length 10 characters ",Toast.LENGTH_SHORT).show();
                    }else {
                      //  Toast.makeText(getApplicationContext(),"ft "+file_type,Toast.LENGTH_SHORT).show();
                        post_abuse();
                        et_abuse.clearComposingText();
                        Toast.makeText(getApplicationContext(),"Thank you for your response, We are looking into your concern.",Toast.LENGTH_SHORT).show();
                    }



                }


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_abuse.clearFocus();
                if (!abuseOrnot){
                    finish();
                }else {
                    btn_cancel.setVisibility(View.INVISIBLE);
                    btn_post.setText("Back");
                    abuseOrnot = false;
                    btn_abuse.setVisibility(View.VISIBLE);
                    et_abuse.setVisibility(View.INVISIBLE);
                    Ll_btm.setOrientation(LinearLayout.HORIZONTAL);

                    inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(et_abuse.getWindowToken(), 0);



                }
            }
        });
        btn_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abuseOrnot = true;
                btn_abuse.setVisibility(View.GONE);
                et_abuse.setVisibility(View.VISIBLE);
                Ll_btm.setOrientation(LinearLayout.VERTICAL);
                btn_post.setText("Post");
                btn_cancel.setVisibility(View.VISIBLE);
                et_abuse.requestFocus();

                inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_abuse, InputMethodManager.SHOW_FORCED);
                et_abuse.setCursorVisible(true);



            }
        });

    }

    private void post_abuse() {


        RestClient.get(context).Post_abuse(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Abuse_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),description,file_type,Tag_cloud_activity_new.file_id),
                new Callback<Abuse_response>() {
                    @Override
                    public void success(final Abuse_response arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {
                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                            String Post_status = arg0.getStatus();
                            if(Post_status.equalsIgnoreCase("ok")){
                                //   Toast.makeText(context, "Success +1", Toast.LENGTH_LONG).show();
                                inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(et_abuse.getWindowToken(), 0);
                            }else {
                                Toast.makeText(context, "Failed retry...", Toast.LENGTH_LONG).show();

                            }

                            //  Populate();
                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(context, "hmm, Trouble..", Toast.LENGTH_LONG).show();
                    }
                });


    }

    public void  getTagData() {

        LD.ShowTheDialog("Please Wait", "Loading..", true);

        RestClient.get(context).TagCloudApi(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new TagListReq(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),file_id),
                new Callback<TagListResp>() {
                    @Override
                    public void success(final TagListResp arg0, Response arg1) {
                        LD.DismissTheDialog();
                        if (arg0.getCode().equals("200")) {
                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                            int RLength = arg0.getData().getRecords().length;
                            for (int i = 0; i < RLength; i++) {
                                TagListRecords iii = arg0.getData().getRecords()[i];
                              //Log.v("Taglist 200"," e "+iii.getEmotion()+" c "+iii.getCount()+" ch "+iii.getIsUserChecked());
                                String t_e,t_c,t_bool;
                                t_e = iii.getEmotion();
                                t_c = iii.getCount();
                                t_bool = iii.getIsUserChecked();
                                TagsModel tagsModel = new TagsModel(t_e,t_c,t_bool,file_id);
                                tag_list_data.add(tagsModel);
                            }
                            LD.DismissTheDialog();
                            adapter.notifyDataSetChanged();


                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(context, "Please, try again", Toast.LENGTH_LONG).show();
                          //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LD.DismissTheDialog();
                     //   errorReporting.SendMail("Xpress Error-publicplaylist-getData", error.toString());
                        Toast.makeText(context, "Error Raised", Toast.LENGTH_LONG).show();
                    }
                });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
