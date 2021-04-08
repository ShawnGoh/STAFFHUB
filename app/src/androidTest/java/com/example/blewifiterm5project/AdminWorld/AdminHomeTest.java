package com.example.blewifiterm5project.AdminWorld;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.blewifiterm5project.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.blewifiterm5project.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminHomeTest {

    private String emailAdmin = "admin@blewifi.com";
    private String passwordAdmin = "admin123";

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Before
    public void setUp(){
        if(mAuth.getUid()==""){
            mAuth.signInWithEmailAndPassword(emailAdmin,passwordAdmin);
        }
    }

    @Test
    public void testChangeFragment() {
        ActivityScenario scenario = ActivityScenario.launch(AdminHome.class);
        onView(withId(R.id.staff)).perform(click());
        onView(withId(R.id.staffFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.mapping)).perform(click());
        onView(withId(R.id.mappingFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testRandomChangeFragment() {
        ActivityScenario scenario = ActivityScenario.launch(AdminHome.class);
        Random random = new Random();
        for(int i=0;i<30;i++) {
            int randomNum = random.nextInt(3);
            switch(randomNum){
                case 0:
                    onView(withId(R.id.admin)).perform(click());
                    onView(withId(R.id.adminFragment)).check(matches(isDisplayed()));
                    break;
                case 1:
                    onView(withId(R.id.staff)).perform(click());
                    onView(withId(R.id.staffFragment)).check(matches(isDisplayed()));
                    break;
                case 2:
                    onView(withId(R.id.mapping)).perform(click());
                    onView(withId(R.id.mappingFragment)).check(matches(isDisplayed()));
                    break;
            }
        }
    }
}
