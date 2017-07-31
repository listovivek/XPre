package com.quad.xpress.models.receivedFiles.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quad.xpress.R;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.clickResponce.Viewed_Req;
import com.quad.xpress.utills.helpers.ErrorReporting;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.utills.helpers.StaticConfig;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListitems_emotion;
import com.quad.xpress.models.tagList.Tag_cloud_activity_new;
import com.quad.xpress.models.tagList.Tag_list_activity_search;
import com.quad.xpress.models.tagList.TagsAdapter;
import com.quad.xpress.models.tagList.TagsModel;
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

public class SearchAdapter_emotion extends RecyclerView.Adapter<SearchAdapter_emotion.MyViewHolder> {
    public TagsAdapter Tag_adp;
    public List<TagsModel> TagList = new ArrayList<>();
    private List<PlayListitems_emotion> rvListitems;
    Context _context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public OnRecyclerListener recyclerListener;
    public String file_id_;
    ErrorReporting errorReporting;
    ProgressDialog pDialog;
    Boolean user_liked = false;
    String Post_type= "nil";
    String video_type;
    String Status = "";
    private View.OnClickListener Reply_click;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView RvVideoTitle;
        public TextView RvSender;
        public TextView RvTags,RvTags_full;
        public TextView RvTime, RvMoreTag, Rvlikes;
        public ImageView RvImage, Play_icon;
        public LinearLayout rv_menu_button_ll;
        public int position;
        public String data = null;
        public ImageButton button = null;
        public PopupWindow popupWindow = null;
        private OnRecyclerListener recyclerListener = null;
        private TextView Tv_tag1,Tv_tag2,Tv_tag3,Tv_tag4;
        public ProgressBar pbar;
        TextView  Tv_comment_1, Tv_comment_2, Tv_comment_3, Tv_comment_4;

        public MyViewHolder(View view, final Context context, OnRecyclerListener recyclerListeners) {
            super(view);
            this.recyclerListener = recyclerListeners;
            Rvlikes = (TextView) itemView.findViewById(R.id.textView_likes);

           // Rl_moreTags = (RelativeLayout) itemView.findViewById(R.id.RelativeLayout_moreTgas);
            rv_menu_button_ll = (LinearLayout) itemView.findViewById(R.id.rv_menu_button_ll);
            button = (ImageButton) itemView.findViewById(R.id.menu_button);
            RvImage = (ImageView) itemView.findViewById(R.id.RvImage);
            Play_icon = (ImageView) itemView.findViewById(R.id.imageView_play_icon);
            RvVideoTitle = (TextView) itemView.findViewById(R.id.RvVideoTitle);
            RvSender = (TextView) itemView.findViewById(R.id.RvSender);
            RvTags = (TextView) itemView.findViewById(R.id.RvTags);
            RvTags_full = (TextView) itemView.findViewById(R.id.RvTags_new);
            RvTime = (TextView) itemView.findViewById(R.id.RvTime);
            RvMoreTag = (TextView) itemView.findViewById(R.id.textView_viewsMoreTags);
            pbar = (ProgressBar) itemView.findViewById(R.id.progressBar_publiclsit);
            popupWindow = new PopupWindow(context);
            Tv_tag1 = (TextView)itemView.findViewById(R.id.textView_tag_1);
            Tv_tag2 = (TextView)itemView.findViewById(R.id.textView_tag_2);
            Tv_tag3 = (TextView)itemView.findViewById(R.id.textView_tag_3);
            Tv_tag4 = (TextView)itemView.findViewById(R.id.textView_tag_4);

            Tv_comment_1 = (TextView)itemView.findViewById(R.id.textView_comment_1);
            Tv_comment_2 = (TextView)itemView.findViewById(R.id.textView_comment_2);
            Tv_comment_3 = (TextView)itemView.findViewById(R.id.textView_comment_3);
            Tv_comment_4 = (TextView)itemView.findViewById(R.id.textView_comment_4);

            View contentView = LayoutInflater.from(context).inflate(R.layout.rv_options_layout, null);
            contentView.findViewById(R.id.rv_opt_accept).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_reject).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_block).setOnClickListener(this);
            contentView.findViewById(R.id.rv_opt_later).setOnClickListener(this);


            popupWindow.setContentView(contentView);

            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, context.getResources().getDisplayMetrics());
            popupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setWidth((int) width);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
            button.setOnClickListener(this);
            if (StaticConfig.IsPublicActivity) {
                rv_menu_button_ll.setVisibility(LinearLayout.GONE);
                // RvTags.setVisibility(TextView.VISIBLE);
            }

        }


        @Override
        public void onClick(View v) {






        }


    }


    public SearchAdapter_emotion(List<PlayListitems_emotion> rvListitems, Context _context, OnRecyclerListener recyclerListener) {
        this.rvListitems = rvListitems;
        this._context = _context;
        this.recyclerListener = recyclerListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.public_video_lists_new, parent, false);

                  return new MyViewHolder(itemView, _context, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        final PlayListitems_emotion list = rvListitems.get(position);
        holder.pbar.setVisibility(View.INVISIBLE);
        holder.RvVideoTitle.setText(list.getTitle());
        holder.RvSender.setText(list.getFromEmail());
        holder.RvTime.setText(list.getCreatedDate());
        holder.RvMoreTag.setText(list.getViewsCount()+" Views");
        holder.RvTags_full.setText(list.getTags().trim());
        String tag_txt = list.getEmotion().trim();
        String tag_store[] = tag_txt.split(",");
        holder.setIsRecyclable(false);


        if(list.getIsUserLiked().matches("1")){
            holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled_liked, 0, 0, 0);
        }else {

        }
        for(int i =0; i < tag_store.length;i++){

            if(tag_store[i].equalsIgnoreCase("null")){
             tag_store[i]= tag_store[i].replace(tag_store[i],"Reply");

            }
            else {

            }




        }
       // Toast.makeText(_context,"tag_store"+tag_store[1],Toast.LENGTH_LONG).show();
        switch (tag_store.length){
            case 1:holder.Tv_tag1.setText(tag_store[0]);

                holder.Tv_tag2.setVisibility(View.INVISIBLE);
                holder.Tv_tag3.setVisibility(View.INVISIBLE);
                holder.Tv_tag4.setVisibility(View.INVISIBLE);
                holder.Tv_tag1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                break;
            case  2:
                holder.Tv_tag1.setText(tag_store[0]);
                holder.Tv_tag2.setText(tag_store[1]);
                holder.Tv_tag3.setVisibility(View.INVISIBLE);
                holder.Tv_tag4.setVisibility(View.INVISIBLE);
                holder.Tv_tag1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                break;
            case 3:
                holder.Tv_tag1.setText(tag_store[0]);
                holder.Tv_tag2.setText(tag_store[1]);
                holder.Tv_tag3.setText(tag_store[2]);
                holder.Tv_tag4.setVisibility(View.INVISIBLE);
                holder.Tv_tag1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                break;
            case  4:
                holder.Tv_tag1.setText(tag_store[0]);
                holder.Tv_tag2.setText(tag_store[1]);
                holder.Tv_tag3.setText(tag_store[2]);
                holder.Tv_tag4.setText(tag_store[3]);
                holder.Tv_tag1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag4.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                break;
            default:
                holder.Tv_tag1.setText(tag_store[0]);
                holder.Tv_tag2.setText(tag_store[1]);
                holder.Tv_tag3.setText(tag_store[2]);
                holder.Tv_tag4.setText(tag_store[3]);
                holder.Tv_tag1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                holder.Tv_tag4.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_purple));
                break;
        }

        switch (tag_store.length){
            case 1:holder.Tv_comment_1.setText(tag_store[0]);

                holder.Tv_comment_2.setVisibility(View.INVISIBLE);
                holder.Tv_comment_3.setVisibility(View.INVISIBLE);
                holder.Tv_comment_4.setVisibility(View.INVISIBLE);
                holder.Tv_comment_1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_1.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_post, 0, 0, 0);


                break;
            case  2:
                holder.Tv_comment_1.setText(tag_store[0]);
                holder.Tv_comment_2.setText(tag_store[1]);
                holder.Tv_comment_3.setVisibility(View.INVISIBLE);
                holder.Tv_comment_4.setVisibility(View.INVISIBLE);
                holder.Tv_comment_1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_2.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_post, 0, 0, 0);
                break;
            case 3:
                holder.Tv_comment_1.setText(tag_store[0]);
                holder.Tv_comment_2.setText(tag_store[1]);
                holder.Tv_comment_3.setText(tag_store[2]);
                holder.Tv_comment_4.setVisibility(View.INVISIBLE);
                holder.Tv_comment_1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_3.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_post, 0, 0, 0);
                break;
            case  4:
                holder.Tv_comment_1.setText(tag_store[0]);
                holder.Tv_comment_2.setText(tag_store[1]);
                holder.Tv_comment_3.setText(tag_store[2]);
                holder.Tv_comment_4.setText(tag_store[3]);
                holder.Tv_comment_1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_4.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                if(tag_store[3].contains("Reply")){
                   holder.Tv_comment_4 .setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_post, 0, 0, 0);
                }
                break;
            default:
                holder.Tv_comment_1.setText(tag_store[0]);
                holder.Tv_comment_2.setText(tag_store[1]);
                holder.Tv_comment_3.setText(tag_store[2]);
                holder.Tv_comment_4.setText(tag_store[3]);
                holder.Tv_comment_1.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_2.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_3.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                holder.Tv_comment_4.setBackground(_context.getResources().getDrawable(R.drawable.round_corner_tv_comments));
                if(tag_store[3].contains("Reply")){
                    holder.Tv_comment_4 .setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_post, 0, 0, 0);
                }
                break;
        }

        holder.Tv_comment_1.setOnClickListener(Reply_click);
        holder.Tv_comment_2.setOnClickListener(Reply_click);
        holder.Tv_comment_3.setOnClickListener(Reply_click);
        holder.Tv_comment_4.setOnClickListener(Reply_click);

        Reply_click  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }
                file_id_=list.getFileID();
                Intent Tag_act = new Intent(_context, Tag_cloud_activity_new.class);
                Tag_act.putExtra("pos",holder.position);
                Tag_act.putExtra("file_id",file_id_);
                Tag_act.putExtra("file_type",video_type);
                v.getContext().startActivity(Tag_act);
            }
        };


        int likes_count  =  Integer.parseInt(list.getLikesCount());
         holder.Rvlikes.setText(likes_count+" Likes");
        if (!list.getTags().equals("")) {
            holder.RvTags.setVisibility(TextView.VISIBLE);
            holder.RvTags.setText(list.getTags());
        }
        try {
            if (list.getFileMimeType().equals("audio/mp3")) {
                Glide.with(_context).load(R.drawable.ic_rv_audio_ping)
                        .into(holder.RvImage);
               // holder.RvImage.setBackground(_context.getResources().getDrawable(R.drawable.ic_play));
                holder.Play_icon.setVisibility(View.INVISIBLE);

            } else if (list.getFileMimeType().equals("video/mp4")) {
                String TBPath;
                if (list.getTBPath().contains(StaticConfig.ROOT_URL_Media)) {
                    TBPath = StaticConfig.ROOT_URL + list.getTBPath().replace(StaticConfig.ROOT_URL_Media, "");
                } else {
                    TBPath = StaticConfig.ROOT_URL + "/" + list.getTBPath();
                }
                Log.v("", "TBPath " + TBPath);
                Glide.with(_context).load(TBPath).into(holder.RvImage);


            }
        } catch (Exception e) {
            Log.v("Exception ", "Exception " + e);
        }

        holder.position = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbar.setVisibility(View.VISIBLE);

                recyclerListener.onItemClicked(holder.position);

                int views_count;
                views_count=Integer.parseInt(list.getViewsCount());
                views_count++;

                holder.RvMoreTag.setText(views_count+" Views");
                file_id_=list.getFileID();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }

                mtd_views_count();
               // Toast.makeText(_context, "Vid--"+video_type, Toast.LENGTH_LONG).show();

            }
        });
        holder.RvMoreTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(_context, "ID--"+list.getFileID(), Toast.LENGTH_LONG).show();
                String Vtype = list.getFileMimeType();
                if(Vtype.equalsIgnoreCase("video/mp4")){
                    video_type = "video";
                }else {
                    video_type = "audio";
                }
                file_id_=list.getFileID();
              //  Toast.makeText(_context,"Pos"+holder.position+"--id"+file_id_+"---type"+video_type,Toast.LENGTH_LONG).show();
                Intent Tag_act = new Intent(_context, Tag_list_activity_search.class);
                Tag_act.putExtra("pos",holder.position);
                Tag_act.putExtra("file_id",file_id_);
                Tag_act.putExtra("file_type",video_type);
                v.getContext().startActivity(Tag_act);


            }
        });
        holder.Rvlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(_context, "+1", Toast.LENGTH_LONG).show();
                file_id_=list.getFileID();
                int likes_count  =  Integer.parseInt(list.getLikesCount());
                //int drawableResourceId = _context.getResources().getIdentifier("nameOfDrawable", "drawable", _context.getPackageName());
               // Toast.makeText(_context,""+drawableResourceId,Toast.LENGTH_LONG).show();


                if (list.getIsUserLiked().equals("1")  ){
                    holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled, 0, 0, 0);
                    likes_count--;
                    list.setIsUserLiked("0");
                    list.setLikesCount((""+likes_count));
                    holder.Rvlikes.setText(likes_count+" Likes");
                    Status = "0";
                    mtd_like_count();



                }else  {
                    holder.Rvlikes.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_like_filled_liked, 0, 0, 0);
                    Status = "1";
                    likes_count++;
                    list.setIsUserLiked("1");
                    list.setLikesCount((""+likes_count));
                    holder.Rvlikes.setText(likes_count+" Likes");
                    mtd_like_count();

                }



            }
        });



    }

    private void mtd_reply_ic() {

    }


    private void mtd_like_count() {

        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        String Emotion="like";

        RestClient.get(_context).Liked(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Like_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Emotion,Status,file_id_),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {

                            String Post_status = arg0.getStatus();
                            Log.d("Mes-Like",Post_status);
                           if(Post_status.equals("")){
                              // Toast.makeText(_context, "Success +1", Toast.LENGTH_LONG).show();
                           }else {
                              // Toast.makeText(_context, "Failed ", Toast.LENGTH_LONG).show();
                           }
                           // LD.DismissTheDialog();
                            //  Populate();
                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(_context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(_context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(_context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }
                        //Populate();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                       // LD.DismissTheDialog();
                        errorReporting.SendMail("Xpress Error-Liked", error.toString());
                        Toast.makeText(_context, "Error Raised", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void mtd_views_count() {
        sharedpreferences = _context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String viewed = "1";

        //"id":"57318bd6db923d57643edd59","viewed":"1","video_type":"video"}
        RestClient.get(_context).Viewd(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Viewed_Req(file_id_,viewed,video_type),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {
                        // LD.DismissTheDialog();
                        if (arg0.getCode().equals("200")) {

                            String Post_status = arg0.getStatus();
                            Log.d("Mes-views",Post_status);
                            if(Post_status.equals("")){
                               // Toast.makeText(_context, "Success +1", Toast.LENGTH_LONG).show();
                            }else {
                               // Toast.makeText(_context, "Failed ", Toast.LENGTH_LONG).show();
                            }
                            // LD.DismissTheDialog();
                            //  Populate();
                        } else if (arg0.getCode().equals("601")) {
                            Toast.makeText(_context, "Please, try again", Toast.LENGTH_LONG).show();
                            //  RefreshToken();
                        } else if (arg0.getCode().equals("202")) {
                            Toast.makeText(_context, "No Records ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(_context, "ReceiveFile error " + arg0.getCode(), Toast.LENGTH_LONG).show();

                        }
                        //Populate();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // LD.DismissTheDialog();
                        errorReporting.SendMail("Xpress Error-Liked", error.toString());
                        Toast.makeText(_context, "Error Raised", Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return rvListitems.size();
    }

    public interface OnRecyclerListener {
        void onItemClicked(int position);




    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String selected_file_name,AppName;
        Boolean DownloadSucess;
        String filename = "";

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
            pDialog = new ProgressDialog(_context);
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
               // ShowFile(file.toString(), file);
            } else {
                Toast.makeText(_context, "Something went wrong,Try again later", Toast.LENGTH_LONG).show();

            }
        }

    }


}

