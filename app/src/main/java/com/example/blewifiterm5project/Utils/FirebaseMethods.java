package com.example.blewifiterm5project.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirebaseMethods {

    final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private Context mContext;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mfirebasedatabase = FirebaseDatabase.getInstance();
        myRef = mfirebasedatabase.getReference();

        //No current user when signing up. If this check not done, the app will crash
        if(mAuth.getCurrentUser()!=null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public UserClass getuserinfo(){
        UserClass userClass = new UserClass();
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

                                    userClass.setEmail(userClassfromdoc.getEmail());
                                    userClass.setName(userClassfromdoc.getName());
                                    userClass.setProfile_picture(userClassfromdoc.getProfile_picture());
                                    break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return userClass;
    }


    public ArrayList<dbdatapoint> getData() {
        dbdatapoint dbdatapoint = new dbdatapoint();
        ArrayList<dbdatapoint> allData = new ArrayList<>();
        db.collection("datapoints")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                com.example.blewifiterm5project.Models.dbdatapoint dbdatapointFromDoc = document.toObject(com.example.blewifiterm5project.Models.dbdatapoint.class);

                                dbdatapoint.setAccesspoints(dbdatapointFromDoc.getAccesspoints());
                                dbdatapoint.setCoordinates(dbdatapointFromDoc.getCoordinates());
                                allData.add(dbdatapoint);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return allData;
    }
}
