package com.example.blewifiterm5project;

import androidx.test.core.app.ActivityScenario;

import com.example.blewifiterm5project.SignInSignup.SignIn;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class SplashScreenTest {

    private String emailtobetyped;
    private String passwordtobetyped;

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthstatelistner;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        if(mAuth.getUid()!=""){
            mAuth.signOut();}
    }

    @Test
    public void no_account_logged_in_test() throws InterruptedException {
        emailtobetyped = "usertest@blewifi.com";
        passwordtobetyped = "user123";
        ActivityScenario newscenario = ActivityScenario.launch(SplashScreen.class);
        Thread.sleep(3000);
        onView((withId(R.id.signinscreen))).check(matches(isDisplayed()));
    }

    @Test
    public void admin_account_logged_in_test() throws InterruptedException {
        emailtobetyped = "admintest@blewifi.com";
        passwordtobetyped = "admin123";
        ActivityScenario currentscenario = ActivityScenario.launch(SignIn.class);
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.adminhomeactivity))).check(matches(isDisplayed()));
        ActivityScenario newscenario = ActivityScenario.launch(SplashScreen.class);
        Thread.sleep(3000);
        onView((withId(R.id.adminhomeactivity))).check(matches(isDisplayed()));
    }

    @Test
    public void user_account_logged_in_test() throws InterruptedException {
        emailtobetyped = "usertest@blewifi.com";
        passwordtobetyped = "user123";
        ActivityScenario currentscenario = ActivityScenario.launch(SignIn.class);
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.userhomeactivity))).check(matches(isDisplayed()));
        ActivityScenario newscenario = ActivityScenario.launch(SplashScreen.class);
        Thread.sleep(3000);
        onView((withId(R.id.userhomeactivity))).check(matches(isDisplayed()));
    }
}