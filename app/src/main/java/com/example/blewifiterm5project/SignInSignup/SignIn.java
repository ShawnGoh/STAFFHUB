package com.example.blewifiterm5project.SignInSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.AdminWorld.EmployeeReviewActivity;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.UserWorld.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignIn";

    Button signinbutton;
    TextView welcomesignin, errormessage, attemptmessage, forgotpassword;
    EditText emailsignin, passwordsignin;
    ImageView Logoimage;
    LinearLayout signinscreen;
    ProgressBar loadingwheel;

    int attemptcount = 0;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mcontext = getApplicationContext();

        //initialize UI elements
        signinbutton = findViewById(R.id.signinconfirmbutton);
        emailsignin = findViewById(R.id.Emailsignin);
        passwordsignin = findViewById(R.id.Passwordsignin);
        welcomesignin = findViewById(R.id.welcomesignin);
        forgotpassword = findViewById(R.id.forgotpassword);

        signinscreen = findViewById(R.id.signinscreen);
        loadingwheel = findViewById(R.id.progressBar);
        errormessage = findViewById(R.id.errormessage);
        attemptmessage = findViewById(R.id.attemptsleft);
        Log.d(TAG, "onCreate Started");

        loadingwheel.setVisibility(View.GONE);

        setupfirebaseauth();

        init();

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, PasswordResetActivity.class).putExtra("Emailfill", emailsignin.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatelistner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthstatelistner!=null){
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
    }


    private boolean isStringNUll(String string){
        if(string.equals("")) return true;
        else return false;
    }

    private void init(){
        Button loginbutton = findViewById(R.id.signinconfirmbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: Attempting to login");


                String email = emailsignin.getText().toString();
                String password = passwordsignin.getText().toString();

                if(!isStringNUll(email) && !isStringNUll(password)) {
                    loadingwheel.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                try {
                                    Log.d(TAG, "signInWithEmail:success");
                                    loadingwheel.setVisibility(View.GONE);
                                    final UserClass[] userClass = new UserClass[1];
                                    db.collection("users")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            //Checking if user is admin or normal user
                                                            userClass[0] = document.toObject(UserClass.class);


                                                            if(userClass[0].getEmail().equals(email) && userClass[0].getAdmin().equals("N")){
                                                                Log.d(TAG, "admin = " + userClass[0].getAdmin());
                                                                Toast.makeText(getApplicationContext(), "Authentication Passed.", Toast.LENGTH_SHORT).show();
                                                                userClass[0].setStatus("online");
                                                                startActivity(new Intent(getApplicationContext(), UserHome.class));
                                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                finish();
                                                            }


                                                            else if(userClass[0].getEmail().equals(email) && userClass[0].getAdmin().equals("Y")){
                                                                Log.d(TAG, "admin = " + userClass[0].getAdmin());
                                                                Log.d(TAG, "email = " + userClass[0].getEmail());
                                                                Log.d(TAG, "entered email = " + email);
                                                                Toast.makeText(getApplicationContext(), "Authentication Passed.", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(), AdminHome.class));
                                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                finish();
                                                            }

                                                        }

                                                    } else {
                                                        Log.w(TAG, "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });



                                }catch (NullPointerException e){
                                    Log.e(TAG, "onComplete: NullPointerException "+e.getMessage());
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                loadingwheel.setVisibility(View.GONE);
                                final UserClass[] userClass = new UserClass[1];
                                final boolean[] foundemail = {false};
                                db.collection("users")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        //Checking if user is admin or normal user
                                                        userClass[0] = document.toObject(UserClass.class);
                                                        if(userClass[0].getEmail().equals(email)){
                                                            foundemail[0] = true;
                                                        }

                                                    }
                                                    if(foundemail[0]){
                                                        errormessage.setText("Incorrect Password");
                                                        attemptcount+=1;
                                                        attemptmessage.setText("Attempts left: "+ (5-attemptcount));
                                                        errormessage.setVisibility(View.VISIBLE);
                                                        attemptmessage.setVisibility(View.VISIBLE);
                                                    }
                                                    else{
                                                        errormessage.setText("Account not in system. Contact Admin");
                                                        errormessage.setVisibility(View.VISIBLE);
                                                    }


                                                } else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                            loadingwheel.setVisibility(View.GONE);

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill in all fields", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //FirebaseAuth
    private void setupfirebaseauth(){
        Log.d(TAG, "Setup FirebaseAuth");
        mAuth = FirebaseAuth.getInstance();

        //check if user is sign in
        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  = firebaseAuth.getCurrentUser();


                if(user !=null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in" +user.getUid());
                }
                else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

    }
}