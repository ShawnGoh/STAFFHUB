package com.example.blewifiterm5project.SignInSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.UserWorld.UserHome;

public class SignIn extends AppCompatActivity {

    Button adminbtn, userbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        adminbtn = findViewById(R.id.adminbutton);
        userbtn = findViewById(R.id.userbutton);

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getApplicationContext(), UserHome.class)));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getApplicationContext(), AdminHome.class)));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}