package com.example.blewifiterm5project.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class  AdminNotificationAdapter extends RecyclerView.Adapter<AdminNotificationAdapter.Viewholder> {
    private ArrayList<String> notificationList;
    private ArrayList<String> notificationDateList;
    private LinkedHashMap<String, String> compiledNotificationLog;
    Context mcontext;
//    private StorageReference storageReference,projectref;

    public boolean isEmpty;

    public AdminNotificationAdapter(ArrayList<String> notificationList, ArrayList<String> notificationDateList, Context mcontext) {
        this.notificationList = notificationList;
        this.notificationDateList = notificationDateList;
        this.mcontext = mcontext;

        compileNotifications();
    }
//    public void setNotificationList(List<String> notificationList) {
//        this.notificationList = notificationList;
//        notifyDataSetChanged();
//    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView notificationName, notificationTime;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            notificationName = itemView.findViewById(R.id.activtydesc);
            notificationTime = itemView.findViewById(R.id.activitytime);
        }
    }

    public void compileNotifications() {
        HashMap<String, String> notifLog = new HashMap<String, String>();
        for (int i=0; i<notificationList.size(); i++){
            // index 0 is date, index 1 is notif content
            notifLog.put(notificationDateList.get(i), notificationList.get(i));
        }

        // sort values chronologically
        TreeMap<String, String> sorted = new TreeMap<>(Collections.reverseOrder());
        sorted.putAll(notifLog);
//        Set<Map.Entry<String, String>> mappings = sorted.entrySet();

        LinkedHashMap<String, String> orderedMap = new LinkedHashMap<>();
        ArrayList<String> orderedNotificationList = new ArrayList<>();
        ArrayList<String> orderedNotificationDateList = new ArrayList<>();

        for (Map.Entry<String, String> entry : sorted.entrySet()){
            orderedMap.put(entry.getKey(), entry.getValue());
            orderedNotificationDateList.add(entry.getKey());
            orderedNotificationList.add(entry.getValue());
        }

        this.compiledNotificationLog = orderedMap;
        this.notificationList = orderedNotificationList;
        this.notificationDateList = orderedNotificationDateList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.useractivityloglayout, parent, false);
        Log.d(TAG, "Creating Viewholder");
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Date newdate = new Date(Long.parseLong(notificationDateList.get(position)));
        holder.notificationName.setText(notificationList.get(position));
        String dateformatting = String.format("%d/%d/%d", newdate.getDate(), newdate.getMonth()+1, newdate.getYear()+1900 );
        holder.notificationTime.setText(dateformatting);
    }

    @Override
    public int getItemCount() {
        int length = notificationList.size();
        return length;
    }

}



