package com.example.blewifiterm5project.AdminWorld;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.WifiScanner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MappingFunctionTest {

    private WifiScanner wifiScanner;

    private String emailAdmin = "admin@blewifi.com";
    private String passwordAdmin = "admin123";

    private String[] spinnerData = new String[]{"Building 2 Level 1","Building 2 Level 2"};

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //firestore reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList coordinates;

    private HashMap<String, ArrayList<Double>> dataValues;

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(AdminHome.class);

    @Before
    public void setUp() {
        coordinates = new ArrayList();
        dataValues = new HashMap<>();

        if(mAuth.getUid()==""){
            mAuth.signInWithEmailAndPassword(emailAdmin,passwordAdmin);
        }
        // enter mapping fragment
        wifiScanner = Mockito.mock(WifiScanner.class);
        onView(withId(R.id.mapping)).perform(click());

        // select map Building 2 Level 2 to generate test data points
        onView(withId(R.id.map_dropdown)).perform(click());
        onData(is(spinnerData[1])).perform(click());
    }

    @After
    public void reset() {
        // remove data point added to database during test

        db.collection("Building 2 Level 2").whereEqualTo("accesspoints", dataValues)
                .whereEqualTo("coordinates", coordinates).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mActivityRule.finishActivity();
    }

    // expected result: datapoint created at 0,0 with empty accesspoint hashmap (wifi scan did not return)
    @Test
    public void testEmptyWifiScan() throws InterruptedException {
        // click on the map at coordinates 0,0
        onView(withId(R.id.mappingimage)).perform(clickXY(0,0));

        coordinates.add(0);
        coordinates.add(0);

        Thread.sleep(500);
        when(wifiScanner.getMacRssi()).thenReturn(new HashMap<>());
        ChildMappingFragment.setWifiScanner(wifiScanner);

        onView(withId((R.id.confirmlocation_button))).perform(click());

        db.collection("Building 2 Level 2").whereEqualTo("accesspoints", dataValues)
                .whereEqualTo("coordinates", coordinates).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            // 1 point containing empty AP list and coordinates (0,0) exists
                            assertEquals(1, count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    // expected result: datapoint created at 0,0 with null accesspoint hashmap since WifiScan
    // did not commence
    // (not tapping automatically generates a point at 0,0 to indicate to the admin that
    // he did not tap the map - he can easily remove the wrong point at 0,0 using a long press)
    @Test
    public void testWithoutTappingMap() {

        // initialised values for point to be deleted in reset()
        coordinates.add(0);
        coordinates.add(0);

        when(wifiScanner.getMacRssi()).thenReturn(null);
        dataValues = null;

        ChildMappingFragment.setWifiScanner(wifiScanner);

        onView(withId((R.id.confirmlocation_button))).perform(click());

        db.collection("Building 2 Level 2").whereEqualTo("accesspoints", dataValues)
                .whereEqualTo("coordinates", coordinates).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            // 1 point containing empty AP list and coordinates (0,0) exists
                            assertEquals(1, count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    // expected result: datapoint created at the location tapped by user
    // in this case, location tapped is coordinates (0.5, 0,5)
    // and WiFi scan did occur, thus accesspoints hashmap should contain results of the scan
    // which here contains data for the AP AndroidWifi (02:15:b2:00:01:00) from the emulator
    @Test
    public void testTapandScanMapping() throws InterruptedException {
        // click on the map at coordinates 0.5,0,5
        onView(withId(R.id.mappingimage)).perform(clickXY(0.5f,0.5f));

        coordinates.add(0.5);
        coordinates.add(0.5);

        Thread.sleep(500);

        String wifiName = "AndroidWifi (02:15:b2:00:01:00)";
        ArrayList<Double> wifiValues = new ArrayList();
        wifiValues.add((double) -30);
        wifiValues.add((double) 0);

        dataValues.put(wifiName, wifiValues);

        when(wifiScanner.getMacRssi()).thenReturn(dataValues);
        ChildMappingFragment.setWifiScanner(wifiScanner);

        onView(withId((R.id.confirmlocation_button))).perform(click());

        db.collection("Building 2 Level 2").whereEqualTo("accesspoints", dataValues)
                .whereEqualTo("coordinates", coordinates).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            // 1 point containing Wifi AP Data and coordinates (0.5,0.5) exists
                            assertEquals(1, count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // custom defined espresso click method to set coordinates of click
    public static ViewAction clickXY(final float pctX, final float pctY){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        int w = view.getWidth();
                        int h = view.getHeight();

                        float x = w * pctX;
                        float y = h * pctY;

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    public static ViewAction longClickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.LONG,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }



}
