package com.example.blewifiterm5project.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.blewifiterm5project.Models.UserClass;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirebaseMethods {

    final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private String userID;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
    private String[] cleanstring;
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
                                break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return allData;
    }

//    //updates the display name of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateName(String displayName){
//        Log.d(TAG, "updateName: updating name to: " + displayName);
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.field_name))
//                .setValue(displayName);
//    }
//
//    //updates the profile picture of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateProfilePicture(String pictureURL){
//        Log.d(TAG, "updateProfilePicture: updating name to: " + pictureURL);
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.profile_picture))
//                .setValue(pictureURL);
//    }
//
//    //updates the profile bio of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateBio(String Bio){
//        Log.d(TAG, "updateName: updating name to: " + Bio);
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.field_bio))
//                .setValue(Bio);
//    }
//
//    //updates the about me section of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateAboutMe(String aboutMe){
//        Log.d(TAG, "updateName: updating name to: " + aboutMe);
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.field_aboutme))
//                .setValue(aboutMe);
//    }
//
//    //updates the looking for section of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateLookingFor(String lookingfor){
//        Log.d(TAG, "updateName: updating name to: " + lookingfor);
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.field_lookingfor))
//                .setValue(lookingfor);
//    }
//
//    //updates the skill chips of the user by updating the name field in the database.
//    //Used when editing the user profile.
//    public void updateSkillChips(String skillChipsString){
//        Log.d(TAG, "updateName: updating name to: " + skillChipsString);
//        String[] str = skillChipsString.split(" ");
//        cleanstring = new String[str.length];
//        for (int i = 0; i<str.length; i++) {
//            cleanstring[i] = str[i].trim();
//        }
//        List<String> al = new ArrayList<String>();
//        al = Arrays.asList(cleanstring);
//
//        myRef.child(mContext.getString(R.string.db_usersdisplay))
//                .child(userID)
//                .child(mContext.getString(R.string.field_skills))
//                .setValue(al);
//    }
//
//    //Checks if the email used to register the account exists already in the system.
//    //Used in Signup page.
//    public boolean checkifemailexists(String email, DataSnapshot dataSnapshot){
//        Log.d(TAG, "checkifemailexists: checking if "+email+" is already in use");
//
//        users_private usersprivate = new users_private();
//
//        //every iteration gets 1 child of the root node and all its info
//        //0th iteration is users_display
//        //1st iteration is users_private
//        for(DataSnapshot ds: dataSnapshot.getChildren()){
//            Log.d(TAG, "checkifemailexists: datasnapshot "+ds);
//            usersprivate.setEmail(ds.getValue(users_private.class).getEmail());
//
//            Log.d(TAG, "checkifemailexists: datasnapshot user email matches "+ usersprivate.getEmail());
//            if(usersprivate.getEmail().equals(email)){
//                Log.d(TAG, "checkifemailexists: Found a match!");
//                return true;
//            }
//        }
//        return false;
//
//    }
//
//    /**
//     * Register a new email and password to Firebase Authentication
//     * @param firstname
//     * @param lastname
//     * @param email
//     * @param password
//     */
//    public void registerNewEmail(final String firstname, final String lastname, final String email, String password){
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    //Send verification email
//                    sendVerificationEmail();
//
//
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    setUserID(mAuth.getCurrentUser().getUid());
//                    Toast.makeText(mContext, "Account created", Toast.LENGTH_SHORT).show();
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                    Toast.makeText(mContext, "Account Creation failed",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
//
//
//    //Used to send a verification email to the user upon successful signup.
//    public void sendVerificationEmail(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(user!=null){
//            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(!task.isSuccessful()){
//                        Toast.makeText(mContext,"Could not send verification email", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//
//    }
//
//
//    /**
//     * Add information to user_display and user_private nodes
//     * @param firstname
//     * @param lastname
//     * @param email
//     */
//    public void addNewUser(String firstname, String lastname, String email){
//        users_private usersprivate1 = new users_private(email, firstname, lastname, userID);
//        myRef.child(mContext.getString(R.string.db_usersprivate)).child(userID).setValue(usersprivate1);
//        users_display display = new users_display(firstname+" "+lastname);
//        myRef.child(mContext.getString(R.string.db_usersdisplay)).child(userID).setValue(display);
//    }
//
//    /**
//     * Retrieves the profile display information for the user that is currently logged in
//     * database: users_display node
//     * @param dataSnapshot
//     * @return
//     */
//    public Userdataretrieval getUserData(DataSnapshot dataSnapshot){
//        Log.d(TAG, "getUserData: retrieving user information from firebase ");
//
//        users_display display = new users_display();
//        users_private privatedata = new users_private();
//
//        for(DataSnapshot ds: dataSnapshot.getChildren()){
//            if(ds.getKey().equals(mContext.getString(R.string.db_usersdisplay))){
//                Log.d(TAG, "getUserData: getting display data : datasnapshot: "+ ds);
//                try {
//                    display.setName(ds.child(userID).getValue(users_display.class).getName());
//                    display.setAbout_me(ds.child(userID).getValue(users_display.class).getAbout_me());
//                    display.setBio(ds.child(userID).getValue(users_display.class).getBio());
//                    display.setProjects_completed(ds.child(userID).getValue(users_display.class).getProjects_completed());
//                    display.setLooking_for(ds.child(userID).getValue(users_display.class).getLooking_for());
//                    display.setSkills(ds.child(userID).getValue(users_display.class).getSkills());
//                    display.setChats(ds.child(userID).getValue(users_display.class).getChats());
//                    display.setProjectDescription(ds.child(userID).getValue(users_display.class).getProjectDescription());
//                    display.setProjectTitle(ds.child(userID).getValue(users_display.class).getProjectTitle());
//                    display.setStatus(ds.child(userID).getValue(users_display.class).getStatus());
//                    display.setCompeletedsetup(ds.child(userID).getValue(users_display.class).isCompeletedsetup());
//                    Log.d(TAG, "onDataChange: " + display.getStatus());
//                    Log.d(TAG, "getUserData: retrieved user display data "+display.toString());
//                }catch (NullPointerException E){
//                    Log.d(TAG, "getUserData: null field encountered ");
//                }
//            }
//            if(ds.getKey().equals(mContext.getString(R.string.db_usersprivate))){
//                Log.d(TAG, "getUserData: getting private data : datasnapshot: "+ ds);
//                try {
//                    privatedata.setFirstname(ds.child(userID).getValue(users_private.class).getFirstname());
//                    privatedata.setLastname(ds.child(userID).getValue(users_private.class).getLastname());
//                    privatedata.setEmail(ds.child(userID).getValue(users_private.class).getEmail());
//                    privatedata.setUser_id(ds.child(userID).getValue(users_private.class).getUser_id());
//                    Log.d(TAG, "getUserData: retrieved user private data "+privatedata.toString());
//                }catch (NullPointerException E){
//                    Log.d(TAG, "getUserData: null field encountered");
//                }
//            }
//        }
//        return new Userdataretrieval(display, privatedata);
//    }
//
//
//    //Retrieves an Arraylist of all users in the database and their profile information
//    public ArrayList<Userdataretrieval> getuserlist(DataSnapshot dataSnapshot){
//        Log.d(TAG, "getUserData: retrieving user information from firebase ");
//
//        ArrayList<Userdataretrieval> result =  new ArrayList<>();
//        ArrayList<users_display> resultpart1 =  new ArrayList<>();
//        ArrayList<users_private> resultpart2 =  new ArrayList<>();
//
//        for(DataSnapshot ds: dataSnapshot.getChildren()){
//            if(ds.getKey().equals("users_display")){
//
//                for(DataSnapshot snap: ds.getChildren()){
//                    users_display display = new users_display();
//
//                    Log.d(TAG, "getUserData: getting display data : datasnapshot: "+ ds);
//                    try {
//                        display.setAbout_me(snap.getValue(users_display.class).getAbout_me());
//                        display.setBio(snap.getValue(users_display.class).getBio());
//                        display.setName(snap.getValue(users_display.class).getName());
//                        display.setProjects_completed(snap.getValue(users_display.class).getProjects_completed());
//                        display.setLooking_for(snap.getValue(users_display.class).getLooking_for());
//                        display.setSkills(snap.getValue(users_display.class).getSkills());
//                        display.setChats(snap.getValue(users_display.class).getChats());
//                        display.setProfile_picture(snap.getValue(users_display.class).getProfile_picture());
//                        display.setStatus(snap.getValue(users_display.class).getStatus());
//
//                        Log.d(TAG, "getUserData: retrieved user display data "+display.toString());
//                    }catch (NullPointerException E){
//                        Log.d(TAG, "getUserData: null field encountered ");
//                    }
//
//                    resultpart1.add(display);
//                }
//            }}
//        for(DataSnapshot ds: dataSnapshot.getChildren()){
//            if(ds.getKey().equals("users_private")){
//
//                for(DataSnapshot prsnap: ds.getChildren()){
//
//                    users_private privatedata = new users_private();
//                    Log.d(TAG, "getUserData: getting private data : datasnapshot: "+ ds);
//                    try {
//                        privatedata.setFirstname(prsnap.getValue(users_private.class).getFirstname());
//                        privatedata.setLastname(prsnap.getValue(users_private.class).getLastname());
//                        privatedata.setEmail(prsnap.getValue(users_private.class).getEmail());
//                        privatedata.setUser_id(prsnap.getValue(users_private.class).getUser_id());
//                        Log.d(TAG, "getUserData: retrieved user private data "+privatedata.toString());
//                    }catch (NullPointerException E){
//                        Log.d(TAG, "getUserData: null field encountered");
//                    }
//
//                    resultpart2.add(privatedata);
//                }
//            }
//
//
//        }
//
//
//        for(int i =0 ; i<resultpart1.size();i++){
//            users_display displayconsol = resultpart1.get(i);
//            users_private privateconsol = resultpart2.get(i);
//            result.add(new Userdataretrieval(displayconsol, privateconsol));}
//        return result;
//    }
//
//    public String getUserID() {
//        return userID;
//    }
//
//    private void setUserID(String userID) {
//        this.userID = userID;
//    }
}
