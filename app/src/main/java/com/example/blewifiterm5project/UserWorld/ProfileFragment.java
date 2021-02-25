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

    TextView mName, mEmail, mProfilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflator =LayoutInflater.from(container.getContext());
        View view = inflator.inflate(R.layout.fragment_profile, container, false);

        mcontext = getActivity();

        firebaseMethods = new FirebaseMethods(container.getContext());
        mAuth=FirebaseAuth.getInstance();
        String userID = mAuth.getUid();

        UserClass user = new UserClass();

        //initiate views/widgets
        mName = view.findViewById(R.id.Name);
        mEmail = view.findViewById(R.id.email);
        mProfilePic = view.findViewById(R.id.profilepic);
        signoutbutton = view.findViewById(R.id.signoutbutton);


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //Checking if user is admin or normal user

                                if(userID.equals(document.getId())){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    UserClass userClassfromdoc = document.toObject(UserClass.class);

                                    user.setEmail(userClassfromdoc.getEmail());
                                    user.setName(userClassfromdoc.getName());
                                    user.setProfile_picture(userClassfromdoc.getProfile_picture());
                                    System.out.println(":this is the user email" + userClassfromdoc.getEmail());

                                    mName.setText(userClassfromdoc.getName());
                                    mEmail.setText(userClassfromdoc.getEmail());
                                    mProfilePic.setText(userClassfromdoc.getProfile_picture());
                                    break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(mcontext, SignIn.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        return view;
    }


}