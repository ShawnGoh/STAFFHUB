package com.example.blewifiterm5project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Date;

public class StaffListReyclerAdapter extends RecyclerView.Adapter<StaffListReyclerAdapter.Viewholder> {

    private ArrayList<String> staffnamelist;
    private ArrayList<String> staffstatuslist;
    private ArrayList<String> stafficonstauslist;
    private Context mcontext;


    public StaffListReyclerAdapter(ArrayList<String> staffnamelist, ArrayList<String> staffstatuslist, ArrayList<String> stafficonstauslist, Context mcontext) {
        this.staffnamelist = staffnamelist;
        this.staffstatuslist = staffstatuslist;
        this.stafficonstauslist = stafficonstauslist;
        this.mcontext = mcontext;
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView staffname, statustext;
        ImageView iconon, iconoff;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            staffname = itemView.findViewById(R.id.stafflistrowname);
            statustext = itemView.findViewById(R.id.stafflistrowstatus);
            iconoff = itemView.findViewById(R.id.statusindicatoruseritemoffline);
            iconon = itemView.findViewById(R.id.statusindicatoruseritemonline);




        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.stafflistrow, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.staffname.setText(staffnamelist.get(position));
        holder.statustext.setText(staffstatuslist.get(position));

        if(stafficonstauslist.get(position).equals("online")){
            holder.iconon.setVisibility(View.VISIBLE);
        }
        else {
            holder.iconoff.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return staffnamelist.size();
    }
}