package com.example.blewifiterm5project.AdminWorld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
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

        Log.d(TAG, "onCreate: adminhome");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Instantiate widgets
        menu_bottom = findViewById(R.id.adminhome_navigation);


        //Setting initial selected item on navbar
        menu_bottom.setItemSelected(0, true);

        if (savedInstanceState == null){
            menu_bottom.setItemSelected(R.id.admin, true);
            fragmentManager = getSupportFragmentManager();
            MiscAdminFragment miscAdminFragment = new MiscAdminFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.adminhome_fragment_container, miscAdminFragment)
                    .commit();
        }

        //Changing of fragments when using bottom nav bar
        menu_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id) {
                    case R.id.admin:
                        fragment = new MiscAdminFragment();
                        break;
                    case R.id.staff:
                        fragment = new TestingFragment();
                        break;
                    case R.id.mapping:
                        fragment = new MappingFragment("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");
                        break;
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.adminhome_fragment_container, fragment)
                            .commit();
                }
                else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseImageActivity.REQUEST_APPLY) {
            if (resultCode == ChooseImageActivity.RESULT_DONE) {
                System.out.println("URL: "+data.getStringExtra("URL"));
                // Change fragment
                fragmentManager.beginTransaction()
                            .replace(R.id.adminhome_fragment_container, new MappingFragment(data.getStringExtra("URL")))
                            .commit();
            }
        }
    }
}