package com.example.blewifiterm5project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.UserWorld.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//Splashscreen activity, shown when loading into app.
public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    ImageView Splashimg;
    Animation fadein;
    ConstraintLayout SplashScreen;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "creating splashscreen");
        setContentView(R.layout.activity_splash_screen);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);

        setupfirebaseauth();



        //Animation
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Splashimg = findViewById(R.id.splashimage);
        SplashScreen = findViewById(R.id.splashscreen);


        Splashimg.setAnimation(fadein);
    }

    private boolean restorePrefData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        Boolean isuserintroed = pref.getBoolean("isIntroed", false);
        return isuserintroed;
    }

    //------------------------------------------ Firebase ----------------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatelistner);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthstatelistner!=null){
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentuser: checking if user is logged in");

        if(user == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 }
            }, 2500);

        }
        else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String email = user.getEmail();
                        System.out.println(email);
                        final UserClass[] user = {new UserClass()};
                        db.collection("users")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                //Checking if user is admin or normal user
                                                UserClass userClass = document.toObject(UserClass.class);
                                                user[0] = userClass;


                                                if(userClass.getEmail().equals(email) && userClass.getAdmin().equals("N")){
                                                    Log.d(TAG, "admin = " + user[0].getAdmin());
                                                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    finish();
                                                }
                                                else if(userClass.getEmail().equals(email) && userClass.getAdmin().equals("Y")){
                                                    Log.d(TAG, "admin = " + userClass.getAdmin());
                                                    Log.d(TAG, "email = " + userClass.getEmail());
                                                    Log.d(TAG, "entered email = " + email);
                                                    startActivity(new Intent(getApplicationContext(), AdminHome.class));
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    finish();
                                                    break;
                                                }
                                            }

                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                    }
                }, 2500);
        }
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

                //check if user is logged in
                checkCurrentUser(user);

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