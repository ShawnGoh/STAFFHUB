package com.example.blewifiterm5project.AdminWorld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.blewifiterm5project.Layout.ImageDotLayout;
import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class MappingFragment extends Fragment {

    // Components
    ImageDotLayout imageDotLayout;
    PhotoView photoView;
    Button button;

    String url;

    private Context mcontext;
    private WifiScanner wifiScanner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        mcontext = getActivity();
        wifiScanner = new WifiScanner(mcontext);

        // Set click listener to imageDotLayout
        imageDotLayout.setOnImageClickListener(new ImageDotLayout.OnImageClickListener() {
            @Override
            public void onImageClick(ImageDotLayout.IconBean bean) {
                // Can add some other functions here
                imageDotLayout.addIcon(bean);
            }
        });

        // Set click listener to icons
        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean= (ImageDotLayout.IconBean) v.getTag();
                wifiScanner.scanWifi();
                // wifiDataAPs = wifiScanner.sortWiFiData(wifiDataAPs);
                db.collection("datapoints").document()
                        .set("Testing");
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