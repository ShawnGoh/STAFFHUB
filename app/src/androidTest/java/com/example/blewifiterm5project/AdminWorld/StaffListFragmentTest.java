package com.example.blewifiterm5project.AdminWorld;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.blewifiterm5project.R;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class StaffListFragmentTest {
    private String emailAdmin = "admin@blewifi.com";
    private String passwordAdmin = "admin123";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            AdminHome.class);

    @Before
    public void setUp(){
        if(mAuth.getUid()==""){
            mAuth.signInWithEmailAndPassword(emailAdmin,passwordAdmin);
        }
        onView(withId(R.id.staff)).perform(click());
    }

    @Test
    public void testStaffPageByClickingItem() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.stafflist)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.employeereview)).check(matches(isDisplayed()));
        //onView(withId(R.id.employeereviewname)).check(matches(withText("user1")));
        onView(withId(R.id.employeereviewbackbutton)).perform(click());
        onView(withId(R.id.staffFragment)).check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.stafflist)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.employeereview)).check(matches(isDisplayed()));
        onView(withId(R.id.employeereviewbackbutton)).perform(click());
        onView(withId(R.id.staffFragment)).check(matches(isDisplayed()));
    }
}
