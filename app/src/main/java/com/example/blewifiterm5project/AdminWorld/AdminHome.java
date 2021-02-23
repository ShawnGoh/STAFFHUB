package com.example.blewifiterm5project.AdminWorld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.blewifiterm5project.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdminHome extends AppCompatActivity {

    private static final String TAG = "AdminHome";
    private static ChipNavigationBar menu_bottom;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Instantiate widgets
        menu_bottom = findViewById(R.id.adminhome_navigation);


        //Setting initial selected item on navbar
        menu_bottom.setItemSelected(0, true);

        if (savedInstanceState == null){
            menu_bottom.setItemSelected(R.id.admin, true);
            fragmentManager = getSupportFragmentManager();
        }

        //Changing of fragments when using bottom nav bar
        menu_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id) {
                    case R.id.admin:

                        break;
                    case R.id.mapping:

                        break;
                    case R.id.testing:
//                        fragment = new MyProjectsFragment();
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