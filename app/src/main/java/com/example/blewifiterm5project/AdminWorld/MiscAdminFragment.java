package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;

import com.example.blewifiterm5project.Adapter.AdminNotificationAdapter;
import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MiscAdminFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Context mcontext;
    Button signoutbutton;
    AdminNotificationAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<String> notificationsList;
    ArrayList<String> notificationsDateList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflator =LayoutInflater.from(container.getContext());
        View view = inflator.inflate(R.layout.fragment_misc_admin, container, false);

        mcontext = getActivity();
        mAuth=FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.feed_recycler);
        signoutbutton = view.findViewById(R.id.adminsignoutbutton);


        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                initwidgets();
            }
        }) ;



//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        myAdapter = new AdminNotificationAdapter(notificationsList, notificationsDateList, mcontext);
//        recyclerView.setAdapter(myAdapter);



        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(mcontext, SignIn.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        return view;
    }

    private void initwidgets(){

        ArrayList<String> notificationsList = new ArrayList<>();
        ArrayList<String> notificationsDateList = new ArrayList<>();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserClass userClass = document.toObject(UserClass.class);
                                String admin = userClass.getAdmin();
                                System.out.println(admin);
                                System.out.println(userClass.getActivitydatelist());
                                System.out.println(userClass.getActivitylist());

                                if (userClass.getAdmin().equals("N")) {
                                    for (String activity: userClass.getActivitylist()) {
                                        System.out.println(activity);
                                        notificationsList.add(userClass.getName()+" "+activity);
                                    }

                                    for (String activitydate: userClass.getActivitydatelist()) {
                                        System.out.println(activitydate);
                                        notificationsDateList.add(activitydate);
                                    }
                                }

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
                            System.out.println("Notif List: "+notificationsList);
                            System.out.println("Notif Date List: "+notificationsDateList);
                            AdminNotificationAdapter newadapter = new AdminNotificationAdapter(notificationsList,notificationsDateList,mcontext);
                            recyclerView.setAdapter(newadapter);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}