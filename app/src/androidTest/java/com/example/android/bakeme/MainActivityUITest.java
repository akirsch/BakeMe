package com.example.android.bakeme;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakeme.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before
    // the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkScrollingFunctionOfRecyclerView(){
        onView(withId(R.id.recipe_list_recycler_view)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.recipe_list_recycler_view)).perform(RecyclerViewActions.scrollToPosition(2));
    }

    // check functioning of app that onClicks take user to correct activities
    @Test
    public void checkClickFunctioningOfRecyclerView(){

        onView(withId(R.id.recipe_list_recycler_view)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.recipe_list_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));

        // check onClick action on 2nd recipe in the list
        onView(withId(R.id.recipe_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // check that new activity is opened which displays the ingredient list text view
        onView(withId(R.id.ingredient_list_tv)).check(matches(isDisplayed()));

        // check scrolling on recipe steps recycler view
        onView(withId(R.id.recipe_steps_recycler_view)).perform(RecyclerViewActions.scrollToPosition(2));

        // check that the correct text appears for this step of the recipe elected
        onView(withText("2) Melt butter and bittersweet chocolate.")).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_steps_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

    }

    // Unregister resources when not needed to avoid malfunction
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }

    }


}
