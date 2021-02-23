package com.example.blewifiterm5project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity/Homescreen";

    ArrayAdapter adapter;
    static ChipNavigationBar menu_bottom;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // use this to switch between activity views
        //this.setTitle("Explore");


        //Instantiate widgets
        menu_bottom = findViewById(R.id.navigation);

        menu_bottom.setItemSelected(0, true);

        if (savedInstanceState == null){
            menu_bottom.setItemSelected(R.id.explore, true);
            fragmentManager = getSupportFragmentManager();
//            DiscoverFragment discoverFragment = new DiscoverFragment();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, discoverFragment)
//                    .commit();
        }



        //Changing of fragments when using bottom nav bar
        menu_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id) {
                    case R.id.explore:

                        break;
                    case R.id.chats:

                        break;
                    case R.id.projects:
//                        fragment = new MyProjectsFragment();
                        break;
                    case R.id.profile:

                        break;
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });



    }
}