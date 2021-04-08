package com.example.blewifiterm5project.SignInSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordResetActivity";
    Context mcontext;

    EditText email;
    Button sendemail;
    ImageButton backbutton;
    ProgressBar progressBar;
    String emailfield;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mcontext = getApplicationContext();
        Intent intent = getIntent();
        emailfield = intent.getStringExtra("Emailfill");
        mAuth= FirebaseAuth.getInstance();

        email = findViewById(R.id.passwordresetemailedittext);
        sendemail = findViewById(R.id.passwordresetemailbutton);
        progressBar = findViewById(R.id.passwordresetprogressBar);
        backbutton = findViewById(R.id.passwordresetbackbutton);

        if(!emailfield.equals("")) {
            email.setText(emailfield);
        }

        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                sendresetemail();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }


    public void sendresetemail(){
        emailfield = email.getText().toString();

        if (emailfield.isEmpty()) {
            email.setError("Please fill in your email");
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!isValidEmailId(emailfield)) {
            email.setError("Please enter valid email");
            progressBar.setVisibility(View.GONE);
            return;
        }


        mAuth.sendPasswordResetEmail(emailfield)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(mcontext, "An password reset email has been sent to you.", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            Toast.makeText(mcontext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}