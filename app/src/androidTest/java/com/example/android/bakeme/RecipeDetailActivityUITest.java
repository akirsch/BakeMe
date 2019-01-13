package com.example.android.bakeme;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakeme.activities.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityUITest {

    @Rule
    ActivityTestRule<RecipeDetailActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void checkRecipeDetailActivityDisplaysCorrectFragment(){
        onView(withId(R.id.recipe_detail_fragment_container)).check(matches(isDisplayed()));
    }
}
