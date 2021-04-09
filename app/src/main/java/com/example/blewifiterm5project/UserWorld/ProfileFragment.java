package com.example.blewifiterm5project.UserWorld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FirebaseMethods firebaseMethods;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Context mcontext;
    Button signoutbutton;

    String useremail;
    UserClass user;

    TextView mName, mEmail, mProfilePic, hoursworked, payentitled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflator =LayoutInflater.from(container.getContext());
        View view = inflator.inflate(R.layout.fragment_profile, container, false);

        mcontext = getActivity();

        firebaseMethods = new FirebaseMethods(container.getContext());
        mAuth=FirebaseAuth.getInstance();
        useremail = mAuth.getCurrentUser().getEmail();

        user = new UserClass();

        //initiate views/widgets
        mName = view.findViewById(R.id.employeereviewname);
        mEmail = view.findViewById(R.id.employeereviewemail);
        mProfilePic = view.findViewById(R.id.profilepic);
        signoutbutton = view.findViewById(R.id.signoutbutton);
        hoursworked = view.findViewById(R.id.employeereviewhoursworked);
        payentitled = view.findViewById(R.id.employeereviewpay);


        initwidgets();


        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offline("offline");
                mAuth.signOut();
                startActivity(new Intent(mcontext, SignIn.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        return view;
    }
    private void offline(String status){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            return;
        }
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

    private void initwidgets(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docid = document.getId();
                                UserClass userClassfromdoc = document.toObject(UserClass.class);
                                //Checking if user is admin or normal user

                                if(useremail.equals(userClassfromdoc.getEmail())){
                                    Log.d(TAG, document.getId() + " => " + document.getData());


                                    user.setEmail(userClassfromdoc.getEmail());
                                    user.setName(userClassfromdoc.getName());
                                    user.setProfile_picture(userClassfromdoc.getProfile_picture());
                                    System.out.println(":this is the user email" + userClassfromdoc.getEmail());

                                    mName.setText(userClassfromdoc.getName());
                                    mEmail.setText(userClassfromdoc.getEmail());
                                    mProfilePic.setText(userClassfromdoc.getProfile_picture());
                                    String hoursworkedstring = String.format("%.1f hours", userClassfromdoc.getHoursthismonth());
                                    String payentitledstring = String.format("$ %.2f", userClassfromdoc.getPayrate()*userClassfromdoc.getHoursthismonth() );
                                    payentitled.setText(payentitledstring);
                                    hoursworked.setText(hoursworkedstring);
                                    break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}