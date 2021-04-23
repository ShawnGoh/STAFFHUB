package com.example.blewifiterm5project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.Models.ActivityLog;
import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Date;

public class UserActivityLogRecyclerAdapter extends RecyclerView.Adapter<UserActivityLogRecyclerAdapter.Viewholder> {

    private ArrayList<String> activityLogs;
    private ArrayList<String> activitydateLogs;
    private Context mcontext;

    // Constructor for the adapter. Requires userlogm usertimelog and activity context.
    // Mainly used when recycler of user time records displayed.
    public UserActivityLogRecyclerAdapter(ArrayList<String> userlog, ArrayList<String> usertimelog, Context mcontext) {
        this.activityLogs = userlog;
        this.activitydateLogs = usertimelog;
        this.mcontext = mcontext;
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
        Date newdate = new Date(Long.parseLong(activitydateLogs.get(activitydateLogs.size()-1-position)));
        holder.activityname.setText(activityLogs.get(activityLogs.size()-1-position));
        String dateformatting = String.format("%d/%d/%d", newdate.getDate(), newdate.getMonth()+1, newdate.getYear()+1900 );
        holder.activitytime.setText(dateformatting);
    }

    @Override
    public int getItemCount() {
        return activityLogs.size();
    }
}