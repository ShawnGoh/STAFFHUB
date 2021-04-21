package com.example.blewifiterm5project.AdminWorld;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.openLinkWithUri;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.is;

public class MiscAdminFragmentTest {
    private String emailAdmin = "admin@blewifi.com";
    private String passwordAdmin = "admin123";

    private String[] spinnerData = new String[]{"Building 2 Level 1","Building 2 Level 2"};

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Before
    public void setUp(){
        if(mAuth.getUid()==""){
            mAuth.signInWithEmailAndPassword(emailAdmin,passwordAdmin);
        }
    }

    @Test
    public void testLogout(){
        ActivityScenario scenario = ActivityScenario.launch(AdminHome.class);
        onView(withId(R.id.adminsignoutbutton)).perform(click());
        onView(withId(R.id.signinscreen)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinner() throws InterruptedException {
        ActivityScenario scenario = ActivityScenario.launch(AdminHome.class);
        onView(withId(R.id.admin_dropdown)).perform(click());
        onData(is(spinnerData[0])).perform(click());
        onView(withId(R.id.admin_dropdown)).check(matches(withSpinnerText(spinnerData[0])));
        Thread.sleep(500);
        onView(withId(R.id.admin_dropdown)).perform(click());
        onData(is(spinnerData[1])).perform(click());
        onView(withId(R.id.admin_dropdown)).check(matches(withSpinnerText(spinnerData[1])));
    }
}
