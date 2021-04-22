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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blewifiterm5project.Adapter.AdminNotificationAdapter;
import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.github.chrisbanes.photoview.PhotoView;
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
import static com.google.firebase.firestore.Query.Direction.ASCENDING;


public class MiscAdminFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    // Components
    ImageDotLayout imageDotLayout;
    Spinner mapDropdown;

    String currentmap = "Building 2 Level 1";

    Context mcontext;
    TextView signoutbutton;
    RecyclerView recyclerView;

    private ArrayList<String> mapNameList;
    private ArrayList<String> mapUrlList;
    private ArrayAdapter<String> mAdapter;

    CollectionReference collectionReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflator =LayoutInflater.from(container.getContext());
        View view = inflator.inflate(R.layout.fragment_misc_admin, container, false);

        mcontext = getActivity();
        mAuth=FirebaseAuth.getInstance();

        imageDotLayout = view.findViewById(R.id.map);
        recyclerView = view.findViewById(R.id.feed_recycler);
        signoutbutton = view.findViewById(R.id.adminsignoutbutton);

        mapDropdown = view.findViewById(R.id.admin_dropdown);

        initMapList();

        mAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_item, mapNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapDropdown.setAdapter(mAdapter);
        mapDropdown.setOnItemSelectedListener(this);

        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/blewifiterm5.appspot.com/o/Auditorium.jpg?alt=media&token=2027275e-4961-4b42-ace9-bd091cfa0272");

        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                initwidgets();
                initIcon(currentmap);
            }
        }) ;




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

    private void initIcon(String currentmap) {
        final List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();
//        List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserClass userClass = document.toObject(UserClass.class);

                                if (userClass.getCurrentmap() != null) {

                                    if (userClass.getCurrentmap().equals(currentmap) && userClass.getUsercoordinates() != null) {
                                        ArrayList<Float> coordinates = userClass.getUsercoordinates();
                                        ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(count, coordinates.get(0), coordinates.get(1), null);
                                        iconBeanList.add(bean);
                                        count++;
                                        }
                                    }
                                }

                            imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {
                                @Override
                                public void onLayoutReady() {
                                    imageDotLayout.removeAllIcon();
                                    imageDotLayout.addIcons(iconBeanList);
                                }
                            });


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        System.out.println("finished populating");
        System.out.println(iconBeanList.size());

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imageDotLayout.setImage(mapUrlList.get(position));
        currentmap = mapNameList.get(position);
        imageDotLayout.removeAllIcon();
        initIcon(currentmap);

        collectionReference = db.collection(currentmap);
        if(collectionReference!=null) {
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "listen:error", error);
                        return;
                    }
                    initIcon(currentmap);
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void initMapList(){
        mapNameList = new ArrayList<>();
        mapUrlList = new ArrayList<>();

        db.collection("maps")
                .orderBy("name",ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("The task is: "+task.isSuccessful());
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                mapNameList.add((String)document.getData().get("name"));
                                mapUrlList.add((String)document.getData().get("url"));
                            }
                            System.out.println("NameList: "+ mapNameList);
                            System.out.println("UrlList: "+ mapUrlList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}