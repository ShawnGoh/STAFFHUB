package com.example.blewifiterm5project.UserWorld;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Components
    ImageDotLayout imageDotLayout;
    Button locatemebutton;
    Spinner mapDropdown;
    private List mList;

    private Context mcontext;
    private WifiScanner wifiScanner;
    private HashMap<String, ArrayList<Double>> dataValues;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    float x_coordinates = 0;
    float y_coordinates = 0;

    final String TAG = "FirebaseMethods";

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        mList = new ArrayList();
        mList.add("Building 2 Level 1");
        mList.add("Building 2 Level 2");
        mList.add("Campus Center");

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(mcontext,
                android.R.layout.simple_spinner_item,mList);
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
                                    System.out.println("Result Coordinates are: "+resultCoordinates);
                                    ImageDotLayout.IconBean location = new ImageDotLayout.IconBean(0, sx, sy, null);
                                    imageDotLayout.addIcon(location);

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

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                // Building 2 Level 1 Image
                imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                // Building 2 Level 2 Image
                System.out.println("choosing image 2");
                imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_2.png?alt=media&token=e194f391-373b-4337-acc1-682258a62970");
                break;
            case 2:
                // Whatever you want to happen when the second item gets selected
                // Building 2 Level 2 Image
                System.out.println("choosing image 3");
                imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/blewifiterm5.appspot.com/o/2021-04-08%2014.54.57.jpg?alt=media&token=7293fe67-d235-4cd3-8539-f895312c490e");
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}