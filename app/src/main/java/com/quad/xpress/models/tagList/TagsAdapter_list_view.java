package com.quad.xpress.models.tagList;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quad.xpress.R;
import com.quad.xpress.Utills.helpers.SharedPrefUtils;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.webservice.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 7/12/2016.
 */
public class TagsAdapter_list_view extends RecyclerView.Adapter<TagsAdapter_list_view.TagsViewHolder> {
    private List<TagsModel>TagsList;
    Context context;
    String Emotion,Status;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String file_id;
    public class TagsViewHolder extends RecyclerView.ViewHolder {


        TextView tv_tags,tv_tag_count;
        ImageView iv_user_liked;
        public TagsViewHolder(View itemView) {
            super(itemView);
            tv_tags = (TextView) itemView.findViewById(R.id.textView_taglist_text);
            tv_tag_count = (TextView) itemView.findViewById(R.id.textView_tag_count);
            iv_user_liked = (ImageView) itemView.findViewById(R.id.iv_tick);
        }
    }
    public
    TagsAdapter_list_view(List<TagsModel>TagsList, Context context){
        this.TagsList = TagsList;
        this.context = context;
    }

    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview  = LayoutInflater.from(parent.getContext()).inflate(R.layout.tagview_comments,parent,false);


        return new TagsViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final TagsViewHolder holder, int position) {
        final TagsModel tagsModel = TagsList.get(position);
        holder.tv_tags.setText(tagsModel.getTag());
        holder.tv_tag_count.setText(tagsModel.getRank());

        if(tagsModel.getLiked().equals("1")){holder.iv_user_liked.setVisibility(View.VISIBLE);}
        else {}

        holder.tv_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Emotion = holder.tv_tags.getText().toString();
            file_id = tagsModel.getFile_id();
            boolean ab;
             if(tagsModel.getLiked().equals("1")){
                 ab =true;
             }else {
                 ab =false;
             }
                int count = Integer.parseInt(tagsModel.getRank());
            if (!ab){
               Status = "1";
                tagsModel.setLiked("1");
                holder.iv_user_liked.setVisibility(View.VISIBLE);
                mtd_emotions_count();
                count++;

                tagsModel.setRank(""+count);
                holder.tv_tag_count.setText(tagsModel.getRank());
            }
                else {
                tagsModel.setLiked("0");
                Status = "0";
                holder.iv_user_liked.setVisibility(View.INVISIBLE);
                mtd_emotions_count();
                count--;
                tagsModel.setRank(""+count);
                holder.tv_tag_count.setText(tagsModel.getRank());
            }
            }
        });

    }

    @Override
    public int getItemCount() {
        return TagsList.size();
    }

    private void mtd_emotions_count() {
     // Toast.makeText(context, "va"+Emotion, Toast.LENGTH_LONG).show();

        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        RestClient.get(context).Liked(sharedpreferences.getString(SharedPrefUtils.SpToken, ""), new Like_Req(sharedpreferences.getString(SharedPrefUtils.SpEmail, ""),Emotion,Status, file_id),
                new Callback<Like_Resp>() {
                    @Override
                    public void success(final Like_Resp arg0, Response arg1) {

                        if (arg0.getCode().equals("200")) {
                            // Toast.makeText(context, "Data().length" + arg0.getData().length, Toast.LENGTH_LONG).show();

                            String Post_status = arg0.getStatus();
                            if(Post_status.equalsIgnoreCase("ok")){
                             //   Toast.makeText(context, "Success +1", Toast.LENGTH_LONG).show();

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


}
