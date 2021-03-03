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

public class UserActivityLogRecyclerAdapter extends RecyclerView.Adapter<UserActivityLogRecyclerAdapter.Viewholder> {

    private ArrayList<ActivityLog> userlog;
    private Context mcontext;


    public UserActivityLogRecyclerAdapter(ArrayList<ActivityLog> userlog, Context mcontext) {
        this.userlog = userlog;
        this.mcontext = mcontext;
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(@NonNull View itemView) {
            super(itemView);

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
    }

    @Override
    public int getItemCount() {
        return userlog.size();
    }
}