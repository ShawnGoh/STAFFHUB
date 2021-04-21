package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.FingerprintAlgo;
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;


public class ChildTestingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Components
    ImageDotLayout imageDotLayout;
    PhotoView photoView;
    Button button, locatemebutton;
    Spinner mapDropdown;
    ImageView visiblebutton, invisiblebutton;

    private Context mcontext;
    private WifiScanner wifiScanner;

    private LinearLayoutManager mLinearLayoutManager;
    String currentmap = "Auditorium";

    private ArrayList<String> mapNameList;
    private ArrayList<String> mapUrlList;
    private ArrayAdapter<String> mAdapter;

    private List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    float x_coordinates = 0;
    float y_coordinates = 0;
    ImageDotLayout.IconBean moving_bean = null;

    final String TAG = "FirebaseMethods";
    ArrayList<dbdatapoint> dataSet;

    private HashMap<String, ArrayList<Double>> dataValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_testing, container, false);

        imageDotLayout = view.findViewById(R.id.map);
        locatemebutton = view.findViewById(R.id.Locatemebutton);
        visiblebutton = view.findViewById(R.id.visiblebuttonchildtesting);
        invisiblebutton = view.findViewById(R.id.invisiblebuttonchildtesting);
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);

        initMapList();

        mAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, mapNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapDropdown = view.findViewById(R.id.map_dropdown_testing);
        mapDropdown.setAdapter(mAdapter);
        mapDropdown.setOnItemSelectedListener(this);


        //imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");

        initIcon(currentmap);

        CollectionReference collectionReference = db.collection(currentmap);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                initIcon(currentmap);
            }
        }) ;

        visiblebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisiblebutton.setVisibility(View.VISIBLE);
                visiblebutton.setVisibility(View.GONE);
                imageDotLayout.addIcons(iconBeanList);
            }
        });

        invisiblebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invisiblebutton.setVisibility(View.GONE);
                visiblebutton.setVisibility(View.VISIBLE);
                imageDotLayout.removeAllIcon();
            }
        });

        // Set click listener to imageDotLayout
        imageDotLayout.setOnImageClickListener(new ImageDotLayout.OnImageClickListener() {
            @Override
            public void onImageClick(ImageDotLayout.IconBean bean) {
                // Can add some other functions here
                if (moving_bean != null){
                    imageDotLayout.removeIcon(moving_bean);
                }
                bean.drawable = getContext().getDrawable(R.drawable.ic_baseline_location_on_24_diffcolor);
                imageDotLayout.addIcon(bean);
                moving_bean = bean;
                x_coordinates = bean.sx;
                y_coordinates = bean.sy;
                wifiScanner.scanWifi();
                FirebaseMethods firebaseMethods = new FirebaseMethods(mcontext);
//                dataSet = firebaseMethods.getData();
            }
        });

        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean= (ImageDotLayout.IconBean) v.getTag();
                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy, Toast.LENGTH_SHORT).show();
            }
        });

        imageDotLayout.setOnIconLongClickListener(new ImageDotLayout.OnIconLongClickListener() {
            @Override
            public void onIconLongClick(View v) {

            }
        });

        locatemebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageDotLayout.removeAllIcon();
                imageDotLayout.addIcons(iconBeanList);
                imageDotLayout.addIcon(moving_bean);
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
                db.collection(currentmap)
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
                                    ImageDotLayout.IconBean location = new ImageDotLayout.IconBean(0, sx, sy, getResources().getDrawable(R.drawable.ic_baseline_location_on_24_testingbean));
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

    private void initIcon(String collectionname) {
        iconBeanList.clear();
        ArrayList<dbdatapoint> allData = new ArrayList<>();
        db.collection(collectionname)
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
                                allData.add(dbdatapointFromDoc);
                            }
                            int count = 0;
                            for (dbdatapoint dbdatapoint : allData){
                                System.out.println("coordinates: "+dbdatapoint.getCoordinates().get(0)+", "+dbdatapoint.getCoordinates().get(1));
                                ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(count, dbdatapoint.getCoordinates().get(0), dbdatapoint.getCoordinates().get(1), null);
                                iconBeanList.add(bean);
                                count++;
                            }
                            // Check the image is ready or not
                            imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {
                                @Override
                                public void onLayoutReady() {
                                    imageDotLayout.addIcons(iconBeanList);
                                }
                            });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        System.out.println("finished populating");

//        ArrayList<dbdatapoint> datapoints = firebaseMethods.getData();
//        System.out.println(datapoints);
//        int count = 0;
//        System.out.println("testing124u3ty89304t1");

//        for (dbdatapoint dbdatapoint : datapoints){
//            System.out.println("coordinates: "+dbdatapoint.getCoordinates().get(0)+", "+dbdatapoint.getCoordinates().get(1));
//            ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(count, dbdatapoint.getCoordinates().get(0), dbdatapoint.getCoordinates().get(1), null);
//            iconBeanList.add(bean);
//            count++;
//        }


//        ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(0, 0.3f, 0.4f, null);
//        iconBeanList.add(bean);
//        bean = new ImageDotLayout.IconBean(1, 0.5f, 0.4f, null);
//        iconBeanList.add(bean);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imageDotLayout.setImage(mapUrlList.get(position));
        currentmap = mapNameList.get(position);
        Toast.makeText(mcontext, currentmap, Toast.LENGTH_LONG).show();
        imageDotLayout.removeAllIcon();
        initIcon(currentmap);
    }


//    public ArrayList<dbdatapoint> getData() {
//        dbdatapoint dbdatapoint = new dbdatapoint();
//        ArrayList<dbdatapoint> allData = new ArrayList<>();
//        db.collection("datapoints")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                com.example.blewifiterm5project.Models.dbdatapoint dbdatapointFromDoc = document.toObject(com.example.blewifiterm5project.Models.dbdatapoint.class);
//
//                                dbdatapoint.setAccesspoints(dbdatapointFromDoc.getAccesspoints());
//                                dbdatapoint.setCoordinates(dbdatapointFromDoc.getCoordinates());
//                                allData.add(dbdatapoint);
//                                // break;
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        return allData;
//    }

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