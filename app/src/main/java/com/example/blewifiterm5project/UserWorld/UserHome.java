package com.example.blewifiterm5project.UserWorld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.UserActivityLogRecyclerAdapter;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Date;

public class UserHome extends AppCompatActivity {

    private static final String TAG = "UserHome";
    private static ChipNavigationBar menu_bottom;
    private FragmentManager fragmentManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: userhome");
        setContentView(R.layout.activity_user_home);

        //Instantiate widgets
        menu_bottom = findViewById(R.id.userhome_navigation);

        menu_bottom.setItemSelected(0, true);
        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null){
            menu_bottom.setItemSelected(R.id.checkin, true);
            fragmentManager = getSupportFragmentManager();
            CheckInCheckOutFragment checkInCheckOutFragment = new CheckInCheckOutFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.userhome_fragment_container, checkInCheckOutFragment)
                    .commit();
        }
        menu_bottom.showBadge(R.id.checkin);

        //Changing of fragments when using bottom nav bar
        menu_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id) {
                    case R.id.checkin:
                        fragment = new CheckInCheckOutFragment();
                        break;
                    case R.id.navigation:
                        fragment = new NavigationFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.userhome_fragment_container, fragment)
                            .commit();
                }
                else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        offline("offline");
    }
    @Override
    protected void onStart() {
        super.onStart();
        offline("online");
    }

    private void offline(String status){
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        System.out.println(email);
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
                                    newuser[0].setStatus(status);
                                    db.collection("users").document(docid).set(newuser[0]);
                                    }
                                    break;
                                }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });}
}