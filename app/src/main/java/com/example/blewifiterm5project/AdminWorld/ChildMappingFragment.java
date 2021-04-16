package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.Models.dbdatapoint;
import com.example.blewifiterm5project.R;
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

import static android.content.ContentValues.TAG;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class ChildMappingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // Components
    ImageDotLayout imageDotLayout;
    PhotoView photoView;
    Button button;
    Button confirmscanbutton;
    Spinner mapDropdown;


    String url;
    String currentmap = "Building 2 Level 1";

    private Context mcontext;
    private WifiScanner wifiScanner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseMethods firebaseMethods;

    private ArrayList<Float> coordinates;
    private String documentName;
    private HashMap<String, ArrayList<Double>> dataValues;
    private HashMap<ArrayList<Float>, HashMap<String, ArrayList<Double>>> dataPoint;
    //    SpinnerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<String> mapNameList;
    private ArrayList<String> mapUrlList;
    private ArrayAdapter<String> mAdapter;

    float x_coordinates = 0;
    float y_coordinates = 0;
    ImageDotLayout.IconBean moving_bean = null;

    CollectionReference collectionReference;

//    public class DataPoint {
//        Float X;
//        Float Y;
//        HashMap<String, ArrayList<Double>> dataValues;
//
//        DataPoint(Float X, Float Y, HashMap<String, ArrayList<Double>> dataValues){
//            this.X = X;
//            this.Y = Y;
//            this.dataValues = dataValues;
//        }
//    }

    // Initialize the fragment with url of the default image
    public ChildMappingFragment(String url) {
        this.url = url;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child_mapping, container, false);
        imageDotLayout = view.findViewById(R.id.map);
        confirmscanbutton = view.findViewById(R.id.confirmlocation_button);
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);
        firebaseMethods = new FirebaseMethods(mcontext);

        mapDropdown = view.findViewById(R.id.map_dropdown);

        initMapList();

        mAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_item, mapNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapDropdown.setAdapter(mAdapter);
        mapDropdown.setOnItemSelectedListener(this);

        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");

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
                initIcon(currentmap);
                moving_bean = bean;
                x_coordinates = bean.sx;
                y_coordinates = bean.sy;
                wifiScanner.scanWifi();
            }
        });

        // Set click listener to icons
        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean= (ImageDotLayout.IconBean) v.getTag();
                dataValues = wifiScanner.getMacRssi();
//                documentName = bean.sx + "," + bean.sy;
//                db.collection("datapoints").document(documentName)
//                        .set(dataValues);
                // db.collection("datapoints").add(newdatapoint);
                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy, Toast.LENGTH_SHORT).show();
            }
        });

        // Set image of photoView from url
        if (url!=null){
            imageDotLayout.setImage(url);
        }



        // Initialize icons
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

        confirmscanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageDotLayout.IconBean bean = (ImageDotLayout.IconBean) v.getTag();
                dataValues = wifiScanner.getMacRssi();
                documentName = x_coordinates + "," + y_coordinates;
                // db.collection("datapoints").document(documentName)
                //         .set(dataValues);
                ArrayList<Float> coordarray = new ArrayList<>();

                coordarray.add(x_coordinates);
                coordarray.add(y_coordinates);
                dbdatapoint newdatapoint = new dbdatapoint(dataValues, coordarray);
                db.collection(currentmap).add(newdatapoint);
//                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy, Toast.LENGTH_SHORT).show();
                initIcon(currentmap);
                Toast.makeText(getActivity(), "Position = "+x_coordinates+", "+y_coordinates+" has been added!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initIcon(String collectionname) {
        final List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();
//        List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();

        // Initialized
        // get datapoint coordinates from database and create beans

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
                            System.out.println(allData);
                            int count = 0;
                            for(int i = 0; i<allData.size(); i++){
                                dbdatapoint dbdatapoint = allData.get(i);
                                //System.out.println("coordinates: "+dbdatapoint.getCoordinates().get(0)+", "+dbdatapoint.getCoordinates().get(1));
                                ImageDotLayout.IconBean bean;
                                bean = new ImageDotLayout.IconBean(count, dbdatapoint.getCoordinates().get(0), dbdatapoint.getCoordinates().get(1), getContext().getDrawable(R.drawable.ic_baseline_location_on_24));
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
        System.out.println(iconBeanList.size());

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

        collectionReference = db.collection(currentmap);
        if(collectionReference!=null) {
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "listen:error", error);
                        return;
                    }
                    initIcon(currentmap);
                }
            });
        }
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