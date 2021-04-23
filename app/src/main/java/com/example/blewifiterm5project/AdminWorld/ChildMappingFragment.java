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
import com.example.blewifiterm5project.Utils.FirebaseMethods;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    ImageDotLayout imageDotLayout;
    Button confirmscanbutton;
    Spinner mapDropdown;
    ImageView visiblebutton, invisiblebutton;

    String url;
    String currentmap = "Auditorium";

    private Context mcontext;
    private static WifiScanner wifiScanner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseMethods firebaseMethods;

    private ArrayList<Float> coordinates;
    private String documentName;
    private HashMap<String, ArrayList<Double>> dataValues;
    private HashMap<ArrayList<Float>, HashMap<String, ArrayList<Double>>> dataPoint;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<String> mapNameList;
    private ArrayList<String> mapUrlList;
    private ArrayAdapter<String> mAdapter;

    float x_coordinates = 0;
    float y_coordinates = 0;
    ImageDotLayout.IconBean moving_bean = null;
    List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();

    CollectionReference collectionReference;

    static boolean testing = false;

    // setter method used in Mockito Instrumented Testing (MappingFunctionTest)
    public static void setWifiScanner(WifiScanner testWifiScanner) {
        wifiScanner = testWifiScanner;
        testing = true;
    }

    // Initialize the fragment with url of the default image
    public ChildMappingFragment(String url) {
        this.url = url;
    }

    /**
     * Get icon infomation
     * @return list contains all icons in Iconbean
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child_mapping, container, false);


        // Instantiating Resources
        visiblebutton = view.findViewById(R.id.visiblebuttonchildmapping);
        invisiblebutton = view.findViewById(R.id.invisiblebuttonchildmapping);
        imageDotLayout = view.findViewById(R.id.mappingimage);
        confirmscanbutton = view.findViewById(R.id.confirmlocation_button);
        mcontext = getActivity();

        // if not in testing mode, instantiate WifiScanner
        if (!testing) {
            wifiScanner = new WifiScanner(mcontext);
        }
        firebaseMethods = new FirebaseMethods(mcontext);

        mapDropdown = view.findViewById(R.id.map_dropdown);

        initMapList();

        mAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_item, mapNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapDropdown.setAdapter(mAdapter);
        mapDropdown.setOnItemSelectedListener(this);

        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/blewifiterm5.appspot.com/o/Auditorium.jpg?alt=media&token=2027275e-4961-4b42-ace9-bd091cfa0272");

        // Initialize icons
        initIcon(currentmap);

        // Set click listener to imageDotLayout
        imageDotLayout.setOnImageClickListener(new ImageDotLayout.OnImageClickListener() {
            @Override
            public void onImageClick(ImageDotLayout.IconBean bean) {
                // Can add some other functions here
                if (moving_bean != null){
                    imageDotLayout.removeIcon(moving_bean);
                }
                bean.drawable = getResources().getDrawable(R.drawable.ic_baseline_location_on_24_diffcolor);
                imageDotLayout.addIcon(bean);
                initIcon(currentmap);
                moving_bean = bean;
                x_coordinates = bean.sx;
                y_coordinates = bean.sy;
                wifiScanner.scanWifi();
            }
        });

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

        // Set click listener to icons
        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean= (ImageDotLayout.IconBean) v.getTag();
                dataValues = wifiScanner.getMacRssi();
                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy + " Docid="+bean.getDbid(), Toast.LENGTH_SHORT).show();
            }
        });

        imageDotLayout.setOnIconLongClickListener(new ImageDotLayout.OnIconLongClickListener() {
            @Override
            public void onIconLongClick(View v) {
                ImageDotLayout.IconBean bean= (ImageDotLayout.IconBean) v.getTag();
                String beanid = bean.getDbid();
                removeicon(beanid);
            }
        });

        CollectionReference collectionReference = db.collection(currentmap);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                System.out.println("change in database detected");
                initIcon(currentmap);
            }
        }) ;

        confirmscanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataValues = wifiScanner.getMacRssi();
                documentName = x_coordinates + "," + y_coordinates;
                ArrayList<Float> coordarray = new ArrayList<>();

                coordarray.add(x_coordinates);
                coordarray.add(y_coordinates);
                dbdatapoint newdatapoint = new dbdatapoint(dataValues, coordarray);
                addicon(newdatapoint);
            }
        });
        return view;
    }

    // ==================================================== Start of custom methods ==============================================================================

    /**
     * Adds icon to the imageDotLayout.
     * @param dbdatapoint
     */
    private void addicon(dbdatapoint dbdatapoint){
        db.collection(currentmap).add(dbdatapoint).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(0, dbdatapoint.getCoordinates().get(0), dbdatapoint.getCoordinates().get(1), getResources().getDrawable(R.drawable.ic_baseline_location_on_24));
                bean.setDbid(documentReference.getId());
                iconBeanList.add(bean);
                imageDotLayout.removeAllIcon();
                imageDotLayout.addIcons(iconBeanList);
                initIcon(currentmap);
                Toast.makeText(getActivity(), "Position = " + x_coordinates + ", " + y_coordinates + " has been added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Removes an icon from the imageDotLayout
     * @param beanid The id of specific icon
     */
    private void removeicon(String beanid){
        int postoremove = -1;
        for(int i = 0; i< iconBeanList.size(); i++){
            if(iconBeanList.get(i).getDbid().equals(beanid)){
                postoremove = i;
            }
        }
        if(postoremove == -1){
            Toast.makeText(mcontext, "Bean has already been deleted", Toast.LENGTH_SHORT).show();
        }else{
            iconBeanList.remove(postoremove);
            imageDotLayout.removeAllIcon();
            imageDotLayout.addIcons(iconBeanList);
            db.collection(currentmap).document(beanid).delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void aVoid) {
                    initIcon(currentmap);
                }
            });

        }
    }

    /**
     * Initialize icons from database.
     * @param collectionname The collection name in database.
     */
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
                                dbdatapointFromDoc.setDocid(document.getId());
                                allData.add(dbdatapointFromDoc);
                            }
                            System.out.println(allData);
                            int count = 0;
                            for(int i = 0; i<allData.size(); i++){
                                dbdatapoint dbdatapoint = allData.get(i);
                                ImageDotLayout.IconBean bean;
                                    bean = new ImageDotLayout.IconBean(count, dbdatapoint.getCoordinates().get(0), dbdatapoint.getCoordinates().get(1), getResources().getDrawable(R.drawable.ic_baseline_location_on_24));
                                    bean.setDbid(dbdatapoint.getDocid());
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imageDotLayout.setImage(mapUrlList.get(position));
        currentmap = mapNameList.get(position);
        imageDotLayout.removeAllIcon();
        initIcon(currentmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initialize map list from database.
     */
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