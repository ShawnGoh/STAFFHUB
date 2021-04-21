package com.example.blewifiterm5project.AdminWorld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmployeeReviewActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeReviewActivity";
    Context mcontext;

    TextView nametextview, hoursworked, payentitled;
    ImageButton backbutton;
    Button managebutton;
    ConstraintLayout managewidow;
    EditText payrate;
    ImageView onlineindicator, offlineindicator;
    ImageDotLayout imageDotLayout;

    UserActivityLogRecyclerAdapter activityLogRecyclerAdapter;
    RecyclerView activitylog;

    ArrayList<String> notificationsList = new ArrayList<>();
    ArrayList<String> notificationsdateList = new ArrayList<>();

    String docid;
    String mapURL;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_review);

        mcontext = getApplicationContext();
        Intent intent = getIntent();
        docid = intent.getStringExtra("DocumentId");
        mAuth= FirebaseAuth.getInstance();
        mcontext = getApplicationContext();

        nametextview = findViewById(R.id.employeereviewname);
        backbutton = findViewById(R.id.employeereviewbackbutton);
        managebutton = findViewById(R.id.employeereviewmanagebutton);
        managewidow = findViewById(R.id.employeereviewmanagewindow);
        payrate = findViewById(R.id.editTextpayrate);
        payentitled = findViewById(R.id.employeereviewpay);
        hoursworked = findViewById(R.id.employeereviewhoursworked);
        onlineindicator = findViewById(R.id.statusindicatoruseritemonline2);
        offlineindicator = findViewById(R.id.statusindicatoruseritemoffline2);
        imageDotLayout = findViewById(R.id.employeereviewuserlocation);
        activitylog = findViewById(R.id.employeereviewuseractivitylog);

//        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");


        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                imageDotLayout.removeAllIcon();
                initwidgets();

            }
        }) ;


        activitylog.setLayoutManager(new LinearLayoutManager(mcontext));
        activityLogRecyclerAdapter = new UserActivityLogRecyclerAdapter(notificationsList, notificationsdateList, mcontext);
        activitylog.setAdapter(activityLogRecyclerAdapter);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        managebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(managewidow.getVisibility() == View.VISIBLE){
                    if(payrate.getText().toString().matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?")) {
                        updateuser(payrate.getText().toString());
                        initwidgets();
                    }else{
                        Toast.makeText(mcontext, "Invalid Number", Toast.LENGTH_SHORT).show();
                    }
                    managewidow.setVisibility(View.GONE);
                }
                else{managewidow.setVisibility(View.VISIBLE);}
            }
        });

    }

    private void setMapURL(String currentmap, ArrayList<Float> coordinates) {

        // hello i changed hereee

        db.collection("maps")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if (document.getData().get("name").equals(currentmap)) {
                                    mapURL = (String) document.getData().get("url");
                                }
                            }
                            System.out.println("MAP URL: "+mapURL);
                            imageDotLayout.setImage(mapURL);
//                            System.out.println("COORDINATES: "+coordinates);
                            imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {

                                @Override
                                public void onLayoutReady() {
                                    ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(0, coordinates.get(0), coordinates.get(1), null);
                                    imageDotLayout.addIcon(bean);
                                }
                            });

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }


    private void initwidgets(){
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        System.out.println(email);
        final UserClass[] newuser = {new UserClass()};
        DocumentReference docRef = db.collection("users").document(docid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserClass userClass = document.toObject(UserClass.class);
                        nametextview.setText(userClass.getName());
                        String hoursworkedstring = String.format("%.1f hours", userClass.getHoursthismonth());
                        String payentitledstring = String.format("$ %.2f", userClass.getPayrate()*userClass.getHoursthismonth() );
                        String payratestring = String.format("%.2f", userClass.getPayrate() );
                        System.out.println("CURRENT MAP: "+userClass.getCurrentmap());
                        System.out.println("USER COORDINATES: "+userClass.getUsercoordinates());
                        setMapURL(userClass.getCurrentmap(), userClass.getUsercoordinates());
                        payrate.setText(payratestring);
                        hoursworked.setText(hoursworkedstring);
                        payentitled.setText(payentitledstring);
                        if(userClass.getStatus().equals("online")){
                            onlineindicator.setVisibility(View.VISIBLE);
                        }
                        else {
                            offlineindicator.setVisibility(View.VISIBLE);
                        }
                        notificationsList = userClass.getActivitylist();
                        notificationsdateList = userClass.getActivitydatelist();

                        UserActivityLogRecyclerAdapter newadapter = new UserActivityLogRecyclerAdapter(notificationsList,notificationsdateList,mcontext);
                        activitylog.setAdapter(newadapter);
//
//                        locationmap.setImage(mapURL);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateuser(String payrate){
        final UserClass[] newuser = {new UserClass()};
        DocumentReference docRef = db.collection("users").document(docid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserClass userClass = document.toObject(UserClass.class);
                        userClass.setPayrate(Float.parseFloat(payrate));
                        db.collection("users").document(docid).set(userClass);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}