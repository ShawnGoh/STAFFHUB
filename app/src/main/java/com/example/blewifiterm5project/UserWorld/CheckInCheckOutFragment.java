package com.example.blewifiterm5project.UserWorld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.Models.ActivityLog;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CheckInCheckOutFragment extends Fragment {


    private static final String TAG = "CheckinCheckoutFragment";
    TextView paidleave, sickleave;
    RecyclerView activitylog;
    Button clockin, clockout;
    Context mcontext;
    UserActivityLogRecyclerAdapter activityLogRecyclerAdapter;

    ArrayList<String> notificationsList = new ArrayList<>();
    ArrayList<String> notificationsdateList = new ArrayList<>();
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

        initwidgets();

        notificationsdateList.add(String.valueOf(new Date().getTime()));
        notificationsList.add("Hello");



        activitylog.setLayoutManager(new LinearLayoutManager(view.getContext()));
        activityLogRecyclerAdapter = new UserActivityLogRecyclerAdapter(notificationsList, notificationsdateList, mcontext);
        activitylog.setAdapter(activityLogRecyclerAdapter);

        clockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar clockincal = Calendar.getInstance();
                String newstring = String.format("Clocked in on %s", clockincal.getTime());
//                notificationsList.add(0,newstring);
                clockout.setVisibility(View.VISIBLE);
                clockin.setVisibility(View.GONE);
                checkinout(newstring, clockincal.getTime());
            }
        });

        clockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar clockoutcal = Calendar.getInstance();
                String newstring = String.format("Clocked out on %s", clockoutcal.getTime());
//                notificationsList.add(0,newstring);
                clockin.setVisibility(View.VISIBLE);
                clockout.setVisibility(View.GONE);
                checkinout(newstring, clockoutcal.getTime());
            }
        });
        return view;
    }


    private void initwidgets(){
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        final UserClass[] newuser = {new UserClass()};
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserClass userClass = document.toObject(UserClass.class);
                                newuser[0] = userClass;

                                if(userClass.getEmail().equals(email)){
                                    paidleave.setText(String.valueOf(newuser[0].getPaid_leave()));
                                    sickleave.setText(String.valueOf(newuser[0].getSick_leave()));
                                    notificationsList = newuser[0].getActivitylist();
                                    notificationsdateList = newuser[0].getActivitydatelist();
                                    if (notificationsList.size()>0){
                                        if(notificationsList.get(notificationsList.size()-1).contains("Clocked in")){
                                            clockin.setVisibility(View.GONE);
                                            clockout.setVisibility(View.VISIBLE);
                                        }
                                    }


                                    UserActivityLogRecyclerAdapter newadapter = new UserActivityLogRecyclerAdapter(notificationsList,notificationsdateList,mcontext);
                                    activitylog.setAdapter(newadapter);
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });}

    private void checkinout(String statustobeset, Date date){
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        final UserClass[] newuser = {new UserClass()};
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Checking if user is admin or normal user
                                String docid = document.getId();
                                UserClass userClass = document.toObject(UserClass.class);
                                newuser[0] = userClass;

                                if(userClass.getEmail().equals(email)){
                                    newuser[0].setStatusmessage(statustobeset);
                                    Toast.makeText(mcontext, statustobeset, Toast.LENGTH_SHORT).show();

                                    notificationsList = newuser[0].getActivitylist();
                                    notificationsList.add(statustobeset);

                                    notificationsdateList = newuser[0].getActivitydatelist();
                                    notificationsdateList.add(String.valueOf(date.getTime()));

                                    UserActivityLogRecyclerAdapter newadapter = new UserActivityLogRecyclerAdapter(notificationsList,notificationsdateList,mcontext);
                                    activitylog.setAdapter(newadapter);

                                    if(statustobeset.contains("Clocked out")){
                                        long timediff = date.getTime()-new Date(Long.parseLong(notificationsdateList.get(notificationsdateList.size()-2))).getTime();
                                        newuser[0].setHoursthismonth(newuser[0].getHoursthismonth()+(float)timediff/3600000);
                                    }

                                    newuser[0].setActivitydatelist(notificationsdateList);
                                    newuser[0].setActivitylist(notificationsList);
                                    db.collection("users").document(docid).set(newuser[0]);


                                    break;
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });}

}

