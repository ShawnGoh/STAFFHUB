package com.example.blewifiterm5project.AdminWorld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.blewifiterm5project.Adapter.AdminNotificationAdapter;
import com.example.blewifiterm5project.Adapter.StaffListReyclerAdapter;
import com.example.blewifiterm5project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.blewifiterm5project.Utils.WifiScanner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffListFragment extends Fragment {


    StaffListReyclerAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<String> staffnamelist, staffstatuslist, stafficonstauslist;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Context mcontext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stafflist, container, false);

        recyclerView = view.findViewById(R.id.stafflist);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mcontext = getActivity();




        initstafflists();


        myAdapter = new StaffListReyclerAdapter(staffnamelist,staffstatuslist,stafficonstauslist,mcontext);
        recyclerView.setAdapter(myAdapter);




        return view;
    }

    private void initstafflists(){

        stafficonstauslist = new ArrayList<>();
        staffnamelist = new ArrayList<>();
        staffstatuslist = new ArrayList<>();

        staffnamelist.add("Qi Yan Seah");
        staffstatuslist.add("Active - Working");
        stafficonstauslist.add("online");

        staffnamelist.add("Jing Sen Tjoa");
        staffstatuslist.add("Inactive - Off-work");
        stafficonstauslist.add("offline");

    }



}