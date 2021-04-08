package com.example.blewifiterm5project.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdminNotificationAdapter extends RecyclerView.Adapter<AdminNotificationAdapter.myholder> {
    private ArrayList<String> notificationList;
//    private StorageReference storageReference,projectref;

    public boolean isEmpty;

    public AdminNotificationAdapter(ArrayList<String> notificationList) {
        this.notificationList = notificationList;
//            this.mContext = mContext;
    }
//    public void setNotificationList(List<String> notificationList) {
//        this.notificationList = notificationList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_row, parent, false);
        Log.d(TAG, "Creating myholder");
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.mytext1.setText(notificationList.get(position));

    }

    @Override
    public int getItemCount() {
        int length = notificationList.size();
        return length;
    }


    public class myholder extends RecyclerView.ViewHolder {

        TextView mytext1;

//        ImageView thumbnail;

        public myholder(@NonNull View itemView) {
            super(itemView);
            mytext1 = itemView.findViewById(R.id.notification_text);
        }
    }
}



