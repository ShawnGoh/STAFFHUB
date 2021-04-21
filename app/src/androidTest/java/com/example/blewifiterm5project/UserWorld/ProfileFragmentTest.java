package com.example.blewifiterm5project.UserWorld;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

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
        onView(withId(R.id.profile)).perform(click());
    }

    @Test
    public void testLogout(){
        onView(withId(R.id.signoutbutton)).perform(click());
        onView(withId(R.id.signinscreen)).check(matches(isDisplayed()));
    }
}
