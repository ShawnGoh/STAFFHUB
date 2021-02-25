package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MiscAdminFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Context mcontext;
    Button signoutbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflator =LayoutInflater.from(container.getContext());
        View view = inflator.inflate(R.layout.fragment_misc_admin, container, false);

        mcontext = getActivity();
        mAuth=FirebaseAuth.getInstance();

        signoutbutton = view.findViewById(R.id.adminsignoutbutton);

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