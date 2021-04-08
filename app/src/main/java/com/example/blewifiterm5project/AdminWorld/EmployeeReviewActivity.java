package com.example.blewifiterm5project.AdminWorld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
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

public class EmployeeReviewActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeReviewActivity";
    Context mcontext;

    TextView nametextview, hoursworked, payentitled;
    ImageButton backbutton;
    Button managebutton;
    ConstraintLayout managewidow;
    EditText payrate;
    ImageView onlineindicator, offlineindicator;


    String docid;

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

        nametextview = findViewById(R.id.employeereviewname);
        backbutton = findViewById(R.id.employeereviewbackbutton);
        managebutton = findViewById(R.id.employeereviewmanagebutton);
        managewidow = findViewById(R.id.employeereviewmanagewindow);
        payrate = findViewById(R.id.editTextpayrate);
        payentitled = findViewById(R.id.employeereviewpay);
        hoursworked = findViewById(R.id.employeereviewhoursworked);
        onlineindicator = findViewById(R.id.statusindicatoruseritemonline2);
        offlineindicator = findViewById(R.id.statusindicatoruseritemoffline2);


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
                    updateuser(payrate.getText().toString());
                    initwidgets();
                    managewidow.setVisibility(View.GONE);
                }
                else{managewidow.setVisibility(View.VISIBLE);}
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
                        payrate.setText(payratestring);
                        hoursworked.setText(hoursworkedstring);
                        payentitled.setText(payentitledstring);
                        if(userClass.getStatus().equals("online")){
                            onlineindicator.setVisibility(View.VISIBLE);
                        }
                        else {
                            offlineindicator.setVisibility(View.VISIBLE);
                        }

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
        });}
}