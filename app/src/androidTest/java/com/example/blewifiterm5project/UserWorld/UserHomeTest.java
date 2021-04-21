package com.example.blewifiterm5project.UserWorld;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UserHomeTest {

    private String emailUser = "user@blewifi.com";
    private String passwordUser = "user123";

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(UserHome.class);

    @Before
    public void setUp(){
        if(mAuth.getUid()==""){
            mAuth.signInWithEmailAndPassword(emailUser,passwordUser);
        }
    }

    @Test
    public void testChangeFragment() {
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profileFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.navigation)).perform(click());
        onView(withId(R.id.navigationFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testRandomChangeFragment() {
        ActivityScenario scenario = ActivityScenario.launch(UserHome.class);
        Random random = new Random();
        for(int i=0;i<30;i++) {
            int randomNum = random.nextInt(3);
            switch(randomNum){
                case 0:
                    onView(withId(R.id.profile)).perform(click());
                    onView(withId(R.id.profileFragment)).check(matches(isDisplayed()));
                    break;
                case 1:
                    onView(withId(R.id.navigation)).perform(click());
                    onView(withId(R.id.navigationFragment)).check(matches(isDisplayed()));
                    break;
                case 2:
                    onView(withId(R.id.checkin)).perform(click());
                    onView(withId(R.id.checkincheckoutFragment)).check(matches(isDisplayed()));
                    break;
            }
        }
    }
}
