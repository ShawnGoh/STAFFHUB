package com.example.blewifiterm5project.AdminWorld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EmployeeReviewActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeReviewActivity";
    Context mcontext;

    TextView nametextview, emailtextview, profilepictextview;
    ImageButton backbutton;


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
        emailtextview = findViewById(R.id.employeereviewemail);
        profilepictextview = findViewById(R.id.employeereviewprofilepic);
        backbutton = findViewById(R.id.employeereviewbackbutton);


        initwidgets();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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
                        emailtextview.setText(userClass.getEmail());
                        profilepictextview.setText(userClass.getProfile_picture());

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