package com.example.blewifiterm5project.AdminWorld;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.Adapter.ChooseMapRecycleViewAdapter;

import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseImageActivity extends AppCompatActivity {

    public static final int REQUEST_APPLY = 2000;
    public static final int RESULT_DONE = 3000;

    private RecyclerView mRecycleView;
    private ChooseMapRecycleViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        //List to save data
        mList = new ArrayList();
        mRecycleView = findViewById(R.id.rv_list);

        //Initialize data (For test)
        initData(mList);
        //Create layout manager
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Create adapter
        mAdapter = new ChooseMapRecycleViewAdapter(mList, this);
        //Set layout manager
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        //Set adapter
        mRecycleView.setAdapter(mAdapter);
    }

    // Hardcode for testing
    // TODO: Apply database before the last meeting
    public void initData(List list) {
        list.add("Building 2 Level 1");
        list.add("Building 2 Level 2");
        list.add("Floor WAP 1");
        list.add("Floor WAP 2");
        list.add("Test Doggy");
    }
}