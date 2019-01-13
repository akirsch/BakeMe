package com.example.android.bakeme;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakeme.activities.PreparationStepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class PreparationStepActivityUITest {


    @Rule
    public ActivityTestRule<PreparationStepActivity> mActivityTestRule =
            new ActivityTestRule<>(PreparationStepActivity.class);

    @Test
    public void checkPreparationStepActivityDisplaysStepDescriptionFragment(){
        onView(withId(R.id.step_description_container)).check(matches(isDisplayed()));
    }

    @Test
    public void checkPreparationStepActivityDisplaysVideoFragment(){
        onView(withId(R.id.video_player_container)).check(matches(isDisplayed()));
    }
}
