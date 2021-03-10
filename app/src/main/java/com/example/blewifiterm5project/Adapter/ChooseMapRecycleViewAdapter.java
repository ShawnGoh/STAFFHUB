package com.example.blewifiterm5project.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.AdminWorld.MappingFragment;
import com.example.blewifiterm5project.R;

import java.util.List;

public class ChooseMapRecycleViewAdapter extends RecyclerView.Adapter<ChooseMapRecycleViewAdapter.MyHolder> {

    private List mList;//Data source

    public ChooseMapRecycleViewAdapter(List list) {
        mList = list;
    }

    //Create viewholder
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_map_recycler_view_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    //Bind data to viewholder
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(mList.get(position).toString());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), AdminHome.class));
//                System.out.println("Test");
//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), MappingFragment.class);
//                intent.putExtra("Test", true);
//                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    /**
     * MyHolder
     */
    class MyHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
        }
    }
}