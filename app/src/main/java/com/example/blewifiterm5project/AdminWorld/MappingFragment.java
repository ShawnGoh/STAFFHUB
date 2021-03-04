package com.example.blewifiterm5project.AdminWorld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.blewifiterm5project.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Components
    PhotoView photoView;
    Button button;
    Bitmap bitmap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Message handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1234) {
                photoView.setImageBitmap(bitmap);
            }
        }
    };


    public MappingFragment() {
        // Required empty public constructor
    }

    // Working thread to download the image from url and apply to photoview
    Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                // Define URL
                URL url = new URL("https://firebasestorage.googleapis.com/v0/b/floorplan-dc25f.appspot.com/o/download.jpg?alt=media&token=be62b98e-dff1-4135-b234-8951a4b0d66d");
                // Open the input stream
                InputStream is = url.openStream();
                // Get image bitmap from inputstream
                bitmap = BitmapFactory.decodeStream(is);
                // Send message to handler
                handler.sendEmptyMessage(0x1234);
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MappingFragment newInstance(String param1, String param2) {
        MappingFragment fragment = new MappingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Need to test this component
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                System.out.println(x+", "+y);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChooseImageActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //Test intent with extra value
        Intent intent = getActivity().getIntent();
        System.out.println("Test intent: "+intent.toString());
        if (intent!=null){
            boolean test = intent.getBooleanExtra("Test", false);
            if (test==true){
                thread.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapping, container, false);
    }
}