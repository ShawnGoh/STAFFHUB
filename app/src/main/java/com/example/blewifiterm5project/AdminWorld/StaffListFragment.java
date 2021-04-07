package com.example.blewifiterm5project.AdminWorld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.blewifiterm5project.Models.UserClass;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StaffListFragment extends Fragment {

    private static final String TAG = "Admin StaffListFragment";
    StaffListReyclerAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<String> staffnamelist, staffstatuslist, stafficonstauslist, docidlist = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Context mcontext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stafflist, container, false);

        recyclerView = view.findViewById(R.id.stafflist);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mcontext = getActivity();


        initstafflists();

        return view;
    }

    private void initstafflists(){

        stafficonstauslist = new ArrayList<>();
        staffnamelist = new ArrayList<>();
        staffstatuslist = new ArrayList<>();
        UserClass user = new UserClass();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserClass userClassfromdoc = document.toObject(UserClass.class);
                                if(userClassfromdoc.getAdmin().equals("N")){
                                    staffnamelist.add(userClassfromdoc.getName());
                                    staffstatuslist.add(userClassfromdoc.getStatusmessage());
                                    stafficonstauslist.add(userClassfromdoc.getStatus());
                                    docidlist.add(document.getId());
                                }
                            }
                            myAdapter = new StaffListReyclerAdapter(staffnamelist,staffstatuslist,stafficonstauslist,mcontext, docidlist);
                            recyclerView.setAdapter(myAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }



}