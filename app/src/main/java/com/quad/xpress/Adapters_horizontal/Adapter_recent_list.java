package com.quad.xpress.Adapters_horizontal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quad.xpress.R;
import com.quad.xpress.models.AlertStream.AlertStreamModelList;

import java.util.List;

public class Adapter_recent_list extends RecyclerView.Adapter<Adapter_recent_list.MyViewHolder>{

    private List<AlertStreamModelList> ListData;
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    OnRecyclerListener recyclerListener;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;


        public MyViewHolder(View view, Context context) {
            super(view);

            user_name = (TextView) view.findViewById(R.id.textView_adapter_recent_list);

        }
    }


    public Adapter_recent_list(List<AlertStreamModelList> ListData, Context context, OnRecyclerListener recyclerListener) {
        this.ListData = ListData;
        this.context = context;
        this.recyclerListener =recyclerListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recent_list, parent, false);

        return new MyViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AlertStreamModelList notificationStreamModelList = ListData.get(position);
        holder.user_name.setText(notificationStreamModelList.getTitle());


    }

    public interface OnRecyclerListener {
        void onItemClicked(int position, String data);

         }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

}

