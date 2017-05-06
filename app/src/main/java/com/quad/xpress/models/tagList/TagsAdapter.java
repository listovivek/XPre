package com.quad.xpress.models.tagList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quad.xpress.R;

import java.util.List;

/**
 * Created by kural on 7/12/2016.
 */
public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {
    private List<TagsModel>TagsList;

    public class TagsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tags,tv_tag_count;
        public TagsViewHolder(View itemView) {
            super(itemView);
          /*  tv_tags = (TextView) itemView.findViewById(R.id.textView_taglist);
            tv_tag_count = (TextView) itemView.findViewById(R.id.textView_tag_count);*/
        }
    }
    public TagsAdapter(List<TagsModel>TagsList){
    this.TagsList = TagsList;
    }

    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview  = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_tagbox,parent,false);


        return new TagsViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(TagsViewHolder holder, int position) {
        TagsModel tagsModel = TagsList.get(position);
        holder.tv_tags.setText(tagsModel.getTag());
        holder.tv_tag_count.setText(tagsModel.getRank());


    }

    @Override
    public int getItemCount() {
        return TagsList.size();
    }




}
