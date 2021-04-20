package com.example.blewifiterm5project.AdminWorld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blewifiterm5project.Adapter.StaffListReyclerAdapter;
import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class CreateNewUser extends AppCompatActivity {

    private static final String TAG = "CreateNewUser";
    EditText name, email;
    ImageView succesicon;
    TextView errormsg;
    ProgressBar progressBar;
    ImageButton backbutton;
    Button confirmbutton;

    Context mcontext = this;
    String tint="00C714";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore  db = FirebaseFirestore.getInstance();
    FirebaseMethods firebaseMethods = new FirebaseMethods(mcontext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        progressBar = findViewById(R.id.signupprogressBar);
        name = findViewById(R.id.Namesignup);
        email = findViewById(R.id.Emailsignup);
        errormsg = findViewById(R.id.signuperrormessage);
        backbutton = findViewById(R.id.signupbackbutton);
        confirmbutton = findViewById(R.id.signupconfirmbutton);
        succesicon = findViewById(R.id.signupsuccessicon);


        init();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    private Boolean validatename(){
        String val = name.getText().toString();
        String checknumbers =".*\\d.*";
        if(val.isEmpty()){
            name.setError("Please enter a name");
            return false;
        }
        else if(val.matches(checknumbers)){
            name.setError("Please enter a valid name");
            return false;
        }
        else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validateemail(){
        final String emailval = email.getText().toString().trim();

        db = FirebaseFirestore.getInstance();
        final Boolean[] output = {false};
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserClass userClassfromdoc = document.toObject(UserClass.class);
                                if(userClassfromdoc.getEmail().equals(emailval)){
                                    output[0] = false;
                                    email.setError("Email already in use");
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        if(emailval.isEmpty()){

            email.setError("Please enter a valid email");
            output[0] = false;
        }
        else if(!isValidEmailId(emailval)){
            email.setError("Invalid email address");
            output[0] = false;
        }
        else {
            errormsg.setVisibility(View.INVISIBLE);
            output[0] = true;
        }
        return output[0];
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private void init(){
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validatename() && validateemail()){
                    progressBar.setVisibility(View.VISIBLE);

                    String namesignup = name.getText().toString();

                    String emailsignup = email.getText().toString();
                    String passwordsignup = "123456";
                    registerNewEmail(namesignup,emailsignup,passwordsignup);


                }

            }
        });
    }

    private void registerNewEmail(final String name, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {


                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserClass userClass = new UserClass(name, "none", email);
                    db.collection("users").add(userClass);
                    errormsg.setText("Account created with password: 123456");
                    errormsg.setTextColor(getResources().getColor(R.color.sucess));
                    errormsg.setVisibility(View.VISIBLE);
                    succesicon.setVisibility(View.VISIBLE);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    errormsg.setText("Account creation failed");
                    errormsg.setTextColor(getResources().getColor(R.color.colorPrimary));
                    errormsg.setVisibility(View.VISIBLE);
                    succesicon.setVisibility(View.GONE);

                }

            }
        });
    }





}