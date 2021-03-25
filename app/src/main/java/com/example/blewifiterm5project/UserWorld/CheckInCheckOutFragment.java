package com.example.blewifiterm5project.UserWorld;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CheckInCheckOutFragment extends Fragment {

    TextView paidleave, sickleave;
    RecyclerView activitylog;
    Button clockin, clockout;
    Context mcontext;
    UserActivityLogRecyclerAdapter activityLogRecyclerAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_in_check_out, container, false);

        mcontext = getActivity();
        mAuth= FirebaseAuth.getInstance();

        paidleave = view.findViewById(R.id.paidleavetextview);
        sickleave = view.findViewById(R.id.sickleavetextview);
        clockin = view.findViewById(R.id.clockinbutton);
        clockout = view.findViewById(R.id.clockoutbutton);
        activitylog = view.findViewById(R.id.useractivitylog);


        ArrayList<String> notificationsList = new ArrayList<>();
        ArrayList<Date> timelist = new ArrayList<>();
        timelist.add(new Date());


        activitylog.setLayoutManager(new LinearLayoutManager(view.getContext()));
        activityLogRecyclerAdapter = new UserActivityLogRecyclerAdapter(notificationsList, timelist, mcontext);
        activitylog.setAdapter(activityLogRecyclerAdapter);

        clockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsList.add(0,"You clocked in");

                timelist.add(0,new Date());
                clockout.setVisibility(View.VISIBLE);
                clockin.setVisibility(View.GONE);
                timelist.set((timelist.size()-1), new Date());
                activitylog.setAdapter(activityLogRecyclerAdapter);

//                HashMap<String, ArrayList<String>> map = new HashMap<>();
//                ArrayList<String> wifi1info = new ArrayList<>();
//                wifi1info.add("mac Address");
//                wifi1info.add("number");
//                map.put("wifi 1", wifi1info);
//
//
//
//                dbdatapoint data = new dbdatapoint(map, wifi1info);

//                db.collection("clockinout").add(data);
            }
        });

        clockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsList.add(0,"You clocked out");
                timelist.add(0,new Date());
                clockin.setVisibility(View.VISIBLE);
                clockout.setVisibility(View.GONE);
                timelist.set((timelist.size()-1), new Date());
                activitylog.setAdapter(activityLogRecyclerAdapter);

            }
        });
        return view;
    }
}