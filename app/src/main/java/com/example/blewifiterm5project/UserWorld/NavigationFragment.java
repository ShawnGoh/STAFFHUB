package com.example.blewifiterm5project.UserWorld;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.FingerprintAlgo;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;


public class NavigationFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    // Components
    ImageDotLayout imageDotLayout;
    Button locatemebutton;
    Spinner mapDropdown;

    private String currentmap;

    private Context mcontext;
    private WifiScanner wifiScanner;
    private HashMap<String, ArrayList<Double>> dataValues;
    private ArrayList<String> mapNameList;
    private ArrayList<String> mapUrlList;
    private ArrayAdapter<String> mAdapter;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    float x_coordinates = 0;
    float y_coordinates = 0;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    final String TAG = "FirebaseMethods";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        imageDotLayout = view.findViewById(R.id.map);
        locatemebutton = view.findViewById(R.id.Locatemebutton);
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);
        wifiScanner.scanWifi();

        initMapList();

        mAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, mapNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapDropdown = view.findViewById(R.id.map_dropdown_user);
        mapDropdown.setAdapter(mAdapter);
        mapDropdown.setOnItemSelectedListener(this);

        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");

        locatemebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataValues = wifiScanner.getMacRssi();
                ArrayList<Float> coordarray = new ArrayList<>();

                coordarray.add(x_coordinates);
                coordarray.add(y_coordinates);
                System.out.println("Coordinate Array of clicked point: "+coordarray);
                dbdatapoint wifiResults = new dbdatapoint();
                wifiResults.setCoordinates(coordarray);
                wifiResults.setAccesspoints(dataValues);
//                System.out.println("datasetpos0: "+dataSet.get(0).getAccesspoints());
//                FirebaseMethods firebaseMethods = new FirebaseMethods(mcontext);
//                ArrayList<dbdatapoint> dataSet = firebaseMethods.getData();

                // START OF LONG METHOD

                ArrayList<Float> usercoords = new ArrayList<>();

                ArrayList<dbdatapoint> dataSet = new ArrayList<>();
                db.collection("datapoints")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        com.example.blewifiterm5project.Models.dbdatapoint dbdatapointFromDoc = document.toObject(com.example.blewifiterm5project.Models.dbdatapoint.class);

//                                dbdatapoint.setAccesspoints(dbdatapointFromDoc.getAccesspoints());
//                                dbdatapoint.setCoordinates(dbdatapointFromDoc.getCoordinates());
                                        dataSet.add(dbdatapointFromDoc);
                                    }
                                    System.out.println(dataSet);
                                    FingerprintAlgo fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
                                    Pair<Double, Double> resultCoordinates = fingerprintAlgo.estimateCoordinates();
                                    float sx = resultCoordinates.first.floatValue();
                                    float sy = resultCoordinates.second.floatValue();
                                    usercoords.add(sx);
                                    usercoords.add(sy);
                                    System.out.println("Result Coordinates are: "+resultCoordinates);
                                    ImageDotLayout.IconBean location = new ImageDotLayout.IconBean(0, sx, sy, null);
                                    imageDotLayout.addIcon(location);
                                    Map<String, Object> coordhashmap = new HashMap<>();
                                    coordhashmap.put("usercoordinates", usercoords);
                                    coordhashmap.put("currentmap", currentmap);
                                    db.collection("users").document(mAuth.getCurrentUser().getUid()).update(coordhashmap);

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });



                // END OF LONG METHOD


//                FingerprintAlgo fingerprintAlgo = new FingerprintAlgo(dataSet, wifiResults);
//                Pair<Double, Double> resultCoordinates = fingerprintAlgo.estimateCoordinates();
//                float sx = resultCoordinates.first.floatValue();
//                float sy = resultCoordinates.second.floatValue();
//                System.out.println(resultCoordinates);
//                ImageDotLayout.IconBean location = new ImageDotLayout.IconBean(0, sx, sy, null);
//                imageDotLayout.addIcon(location);
            }
        });

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imageDotLayout.setImage(mapUrlList.get(position));
        currentmap = mapNameList.get(position);
        Toast.makeText(mcontext, currentmap, Toast.LENGTH_LONG).show();
        imageDotLayout.removeAllIcon();
        //initIcon(currentmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void initMapList(){
        mapNameList = new ArrayList<>();
        mapUrlList = new ArrayList<>();

        db.collection("maps")
                .orderBy("name",ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("The task is: "+task.isSuccessful());
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                mapNameList.add((String)document.getData().get("name"));
                                mapUrlList.add((String)document.getData().get("url"));
                            }
                            System.out.println("NameList: "+ mapNameList);
                            System.out.println("UrlList: "+ mapUrlList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}