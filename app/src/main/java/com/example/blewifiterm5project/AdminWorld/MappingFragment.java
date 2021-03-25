package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blewifiterm5project.Adapter.ChooseMapRecycleViewAdapter;
import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MappingFragment extends Fragment {

    // Components
    ImageDotLayout imageDotLayout;
    PhotoView photoView;
    Button button;
    Button confirmscanbutton;
    Spinner mapDropdown;

    String url;

    private Context mcontext;
    private WifiScanner wifiScanner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<Float> coordinates;
    private String documentName;
    private HashMap<String, ArrayList<Double>> dataValues;
    private HashMap<ArrayList<Float>, HashMap<String, ArrayList<Double>>> dataPoint;
    private ChooseMapRecycleViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List mList;

    float x_coordinates = 0;
    float y_coordinates = 0;
    ImageDotLayout.IconBean moving_bean = null;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MappingFragment() {
        // Empty public constructor
    }

    //TODO: in the final project, it may contain 2 arguments: 1.boolean 2.url in string
    public MappingFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create fragment and views' instance
        View view = inflater.inflate(R.layout.fragment_mapping, container, false);
        imageDotLayout = view.findViewById(R.id.map);
        button = view.findViewById(R.id.mappingbutton);
        confirmscanbutton = view.findViewById(R.id.confirmlocation_button);
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);

//        mList = new ArrayList();
//        mAdapter = new ChooseMapRecycleViewAdapter(mList, getActivity());
//
//        mapDropdown = view.findViewById(R.id.map_dropdown);
//        mapDropdown.setAdapter((SpinnerAdapter) mAdapter);
//
//        mList.add("Building 2 Level 1");
//        mList.add("Building 2 Level 2");
//
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mList);
//
//        mapDropdown.setAdapter();

        imageDotLayout.setImage("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/Floor_WAP_1.png?alt=media&token=778a33c4-f7a3-4f8b-8b14-b3171df3bdc2");

        // Set click listener to imageDotLayout
        imageDotLayout.setOnImageClickListener(new ImageDotLayout.OnImageClickListener() {
            @Override
            public void onImageClick(ImageDotLayout.IconBean bean) {
                // Can add some other functions here
                if (moving_bean != null){
                    imageDotLayout.removeIcon(moving_bean);
                }
                imageDotLayout.addIcon(bean);
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
//                coordinates.add(bean.sx);
//                coordinates.add(bean.sy);
                documentName = bean.sx + "," + bean.sy;
//                db.collection("datapoints").document(documentName)
//                        .set(dataValues);
                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy, Toast.LENGTH_SHORT).show();
            }
        });

        // Set image of photoView from url
        if (url!=null){
            imageDotLayout.setImage(url);
        }

        // Initialize icons
        initIcon();

        // Button to choose image from ChooseImageActivity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChooseImageActivity.class);
                getActivity().startActivityForResult(intent, ChooseImageActivity.REQUEST_APPLY);
            }
        });

        confirmscanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageDotLayout.IconBean bean = (ImageDotLayout.IconBean) v.getTag();
                dataValues = wifiScanner.getMacRssi();
                documentName = x_coordinates + "," + y_coordinates;
                db.collection("datapoints").document(documentName)
                        .set(dataValues);
//                Toast.makeText(getActivity(),"Id="+bean.id+" Position="+bean.sx+", "+bean.sy, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Position = "+x_coordinates+", "+y_coordinates+" has been added!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initIcon() {
        final List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();

        // Initialize
        ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(0, 0.3f, 0.4f, null);
        iconBeanList.add(bean);
        bean = new ImageDotLayout.IconBean(1, 0.5f, 0.4f, null);
        iconBeanList.add(bean);

        // Check the image is ready or not
        imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {
            @Override
            public void onLayoutReady() {
                imageDotLayout.addIcons(iconBeanList);
            }
        });
    }
}