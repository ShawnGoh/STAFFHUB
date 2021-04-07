package com.example.blewifiterm5project.SignInSignup;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import android.content.Context;
import android.widget.EditText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.blewifiterm5project.AdminWorld.AdminHome;
import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

public class SignInTest {

    private String emailtobetyped;
    private String passwordtobetyped;

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthstatelistner;


    @Rule
    public ActivityScenarioRule<SignIn> mSigninTestRul = new ActivityScenarioRule<>(SignIn.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();

        //check if all views are init in their correct states before starting tests
        onView((withId(R.id.signinscreen))).check(matches(isDisplayed()));
        onView((withId(R.id.Emailsignin))).check(matches(isDisplayed()));
        onView((withId(R.id.Passwordsignin))).check(matches(isDisplayed()));
        onView((withId(R.id.signinconfirmbutton))).check(matches(isDisplayed()));
        onView((withId(R.id.errormessage))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView((withId(R.id.attemptsleft))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }



    @After
    public void tearDown() throws Exception {
        Intents.release();
        emailtobetyped = "";
        passwordtobetyped = "";
        if(mAuth.getUid()!=""){
            mAuth.signOut();}
    }

    @Test
    public void valid_user_login_test() throws InterruptedException {
        emailtobetyped = "usertest@blewifi.com";
        passwordtobetyped = "user123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.userhomeactivity))).check(matches(isDisplayed()));
    }

    @Test
    public void valid_admin_login_test() throws InterruptedException {
        emailtobetyped = "admintest@blewifi.com";
        passwordtobetyped = "admin123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.adminhomeactivity))).check(matches(isDisplayed()));
    }

    @Test
    public void valid_user_email_invalid_password_test() throws InterruptedException {
        emailtobetyped = "usertest@blewifi.com";
        passwordtobetyped = "123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.errormessage))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.attemptsleft))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.errormessage)).check(matches(withText("Incorrect Password")));
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 4")));
    }

    @Test
    public void valid_admin_email_invalid_password_test() throws InterruptedException {
        emailtobetyped = "admintest@blewifi.com";
        passwordtobetyped = "123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.errormessage))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.attemptsleft))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.errormessage)).check(matches(withText("Incorrect Password")));
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 4")));
    }

    @Test
    public void invalid_email_test() throws InterruptedException {
        emailtobetyped = "admin1@blewifi.com";
        passwordtobetyped = "123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.errormessage))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.attemptsleft))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.errormessage)).check(matches(withText("Account not in system. Contact Admin")));
    }

    @Test
    public void valid_user_email_invalid_password_five_times_test() throws InterruptedException {
        emailtobetyped = "usertest@blewifi.com";
        passwordtobetyped = "123";
        onView(withId(R.id.Emailsignin)).perform(typeText(emailtobetyped));
        onView(withId(R.id.Passwordsignin)).perform(typeText(passwordtobetyped));
        onView(withId(R.id.Emailsignin)).check(matches(withText(emailtobetyped)));
        onView(withId(R.id.Passwordsignin)).check(matches(withText(passwordtobetyped)));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView((withId(R.id.errormessage))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.attemptsleft))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.errormessage)).check(matches(withText("Incorrect Password")));
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 4")));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 3")));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 2")));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.attemptsleft)).check(matches(withText("Attempts left: 1")));
        onView(withId(R.id.signinconfirmbutton)).perform(click());
        Thread.sleep(1500);
    }
}