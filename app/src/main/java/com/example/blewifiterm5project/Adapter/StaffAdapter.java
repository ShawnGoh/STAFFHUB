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

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.myholder> {
    private ArrayList<String> staffList;
//    private StorageReference storageReference,projectref;

    public boolean isEmpty;

    public StaffAdapter(ArrayList<String> staffList) {
        this.staffList = staffList;
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
        View view = inflater.inflate(R.layout.staff_row, parent, false);
        Log.d(TAG, "Creating myholder");
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.mytext1.setText(staffList.get(position));
    }

    @Override
    public int getItemCount() {
        int length = staffList.size();
        return length;
    }


    public class myholder extends RecyclerView.ViewHolder {

        TextView mytext1;
//        ImageView thumbnail;

        public myholder(@NonNull View itemView) {
            super(itemView);
//            thumbnail = itemView.findViewById(R.id.project_picture);
            mytext1 = itemView.findViewById(R.id.staff_name);
//            mytext2 = itemView.findViewById(R.id.description);
        }
    }
}



