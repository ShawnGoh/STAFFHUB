package com.example.blewifiterm5project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.Models.ActivityLog;
import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Date;

public class UserActivityLogRecyclerAdapter extends RecyclerView.Adapter<UserActivityLogRecyclerAdapter.Viewholder> {

    private ArrayList<String> userlog;
    private Context mcontext;
    private ArrayList<Date> timelog;


    public UserActivityLogRecyclerAdapter(ArrayList<String> userlog, ArrayList<Date>timelog, Context mcontext) {
        this.userlog = userlog;
        this.mcontext = mcontext;
        this.timelog = timelog;
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView activityname, activitytime;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            activityname = itemView.findViewById(R.id.activtydesc);
            activitytime = itemView.findViewById(R.id.activitytime);


        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useractivityloglayout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.activityname.setText(userlog.get(position));
        holder.activitytime.setText(String.valueOf((timelog.get(timelog.size()-1).getTime()-timelog.get(position).getTime())/1000)+"s ago");
    }

    @Override
    public int getItemCount() {
        return userlog.size();
    }
}