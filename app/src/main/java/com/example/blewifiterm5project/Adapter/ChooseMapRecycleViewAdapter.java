package com.example.blewifiterm5project.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.AdminWorld.ChooseImageActivity;
import com.example.blewifiterm5project.AdminWorld.MappingFragment;
import com.example.blewifiterm5project.R;

import java.util.List;

public class ChooseMapRecycleViewAdapter extends RecyclerView.Adapter<ChooseMapRecycleViewAdapter.MyHolder> {

    private List mList;//Data source
    private Activity activity;

    public ChooseMapRecycleViewAdapter(List list, Activity activity) {
        this.mList = list;
        this.activity = activity;
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
                // Set activity result
                String url = findMap(position);
                Intent intent = activity.getIntent();
                intent.putExtra("URL",url);
                activity.setResult(ChooseImageActivity.RESULT_DONE, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Find the url of the selected item
     * Hardcode for testing
     * TODO: Apply database before the last meeting
     *
     * @return String url in string
     */
    public String findMap(int position){
        String[] stringArray = new String[]{"https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/B2L1.PNG?alt=media&token=4c40f339-e4b1-4019-bc5f-ee315074bac2",
                "https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/B2L2.PNG?alt=media&token=14d1b0f9-7791-4d96-808d-2a42a11393f4",
                "https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2",
                "https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_2.png?alt=media&token=e194f391-373b-4337-acc1-682258a62970",
                "https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/download.jpg?alt=media&token=be62b98e-dff1-4135-b234-8951a4b0d66d"};
        return stringArray[position];
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