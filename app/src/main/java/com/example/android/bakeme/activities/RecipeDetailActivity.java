package com.example.android.bakeme.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.fragments.RecipeDetailFragment;
import com.example.android.bakeme.fragments.StepInstructionsFragment;
import com.example.android.bakeme.fragments.VideoPlayerFragment;
import com.example.android.bakeme.models.Recipe;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private Bundle mSelectedRecipeBundle;
    private Recipe mSelectedRecipe;
    private FragmentManager mFragmentManager;
    private boolean isTwoPane;
    private int selectedRecipePicId = Constants.NO_IMAGE_ID;

    @BindView(R.id.toolbar) Toolbar myToolbar;
    ImageView selectedRecipeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        // get data for selected recipe passed by intent from MainActivity
        if (getIntent().getExtras() != null) {
            mSelectedRecipeBundle = getIntent().getExtras();
            mSelectedRecipe = mSelectedRecipeBundle.getParcelable(Constants.SELECTED_RECIPE_KEY);

        }

        // if activity is being displayed on a phone, set isTwoPane to be false
        if (findViewById(R.id.phone_detail_main_layout) != null) {
            isTwoPane = false;
        // else if activity is being displayed on tablet, set ti to be true.
        } else if (findViewById(R.id.tablet_detail_main_layout) != null){
            isTwoPane = true;
        }

        // If using phone, set picture to appear in CollapsingToolbarLayout
        if (!isTwoPane) {
            // select correct picture for selected recipe
            selectedRecipeImageView = findViewById(R.id.recipe_detail_image_view);
            selectedRecipeImageView
                    .setImageResource(getSelectedRecipePic(mSelectedRecipe.getmRecipeName()));
        }

        // Set the Toolbar as Action Bar
        setSupportActionBar(myToolbar);
        // Set title of action bar to appropriate label for this Activity
        if (mSelectedRecipe != null && !TextUtils.isEmpty(mSelectedRecipe.getmRecipeName())){
            Objects.requireNonNull(getSupportActionBar()).setTitle(mSelectedRecipe.getmRecipeName());
        } else Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        // enable up navigation to parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on phone display only, set status bar to be transparent.
        if (!isTwoPane) {
            // set status bar to be transparent
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // Set the padding to match the Status Bar height (to avoid title being cut off by
        // transparent toolbar
        myToolbar.setPadding(0, 25, 0, 0);

        // create instance of fragment manager to enable Fragment transactions
        mFragmentManager = getSupportFragmentManager();

        // only create new Fragments when no previously saved state exists
        if (savedInstanceState == null){

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

            // add the boolean value tracking whether the layout is being shown on phone or tablet to
            // the bundle sent to the RecipeDetailFragment
            mSelectedRecipeBundle.putBoolean(Constants.BOOLEAN_IS_TWO_PANE_KEY, isTwoPane);

            // pass the data received by this activity with the intent that created it to the Fragment
            // contained within it.
            recipeDetailFragment.setArguments(mSelectedRecipeBundle);

            // replace the container in this Activity's layout with a RecipeDetailFragment
            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment_container, recipeDetailFragment)
                    .commit();

            // if Activity is being displayed on a tablet, initialize the video player and
            // instruction fragments as well, as all 3 fragments can be displayed on one screen

            if (isTwoPane){

                setUpVideoFragment();

                setUpInstructionsFragment();
            }

        }



    }

    /***
     * This method facilitates the function of Back navigation using Button in Toolbar on older versions
     * of Android
     * @param item the menu item that was clicked on
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper Method to select the correct image to display for the selected recipe
     * @param recipeName name of selected recipe
     * @return the Id for correct image to display
     */
    private int getSelectedRecipePic (String recipeName){

        if (recipeName.equals(getString(R.string.brownies))){
            selectedRecipePicId = R.drawable.brownies;
        } else if (recipeName.equals(getString(R.string.nutella_pie))){
            selectedRecipePicId = R.drawable.nutellapie;
        } else if (recipeName.equals(getString(R.string.cheesecake))){
            selectedRecipePicId = R.drawable.cheesecake;
        } else if (recipeName.equals(getString(R.string.yellow_cake))){
            selectedRecipePicId = R.drawable.vanillacake;
        }

        return selectedRecipePicId;
    }

    private void setUpInstructionsFragment(){

        StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();

        // Get the short description for the first step to be initially displayed in the
        // instruction fragment.
        String initialInstructionString = mSelectedRecipe
                .getmPreparationSteps()
                .get(0)
                .getmShortDescription();

        // create bundle just for storing the step description
        Bundle instructionBundle = new Bundle();
        instructionBundle.putString(Constants.STEP_LONG_DESC_KEY, initialInstructionString);

        // pass the data received by this activity with the intent that created it to the Fragment
        // contained within it.
        stepInstructionsFragment.setArguments(instructionBundle);

        mFragmentManager.beginTransaction()
                .replace(R.id.step_description_container, stepInstructionsFragment)
                .commit();

    }

    private void setUpVideoFragment(){

        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();

        // create bundle just for storing the video URL or relevant image
        Bundle videoURLBundle = new Bundle();

        // get correct image and the video URL for the first step of the selected recipe to be initially
        // displayed in the video fragment.
        int initialImageViewId = getSelectedRecipePic(mSelectedRecipe.getmRecipeName());
        String initialVideoURL = mSelectedRecipe.getmPreparationSteps().get(0).getmVideoURL();

        //if the video URL isn't not null or an empty string, pass it to the Fragment, else, pass the
        // image Id if it exists
        if (initialVideoURL != null && !TextUtils.isEmpty(initialVideoURL)){
            videoURLBundle.putString(Constants.VIDEO_URL_KEY, initialVideoURL);
        } else if (initialImageViewId != Constants.NO_IMAGE_ID) {
            videoURLBundle.putInt(Constants.DEFAULT_IMAGE_KEY, initialImageViewId);
        }

        // pass the data received by this activity with the intent that created it to the Fragment
        // contained within it.
        videoPlayerFragment.setArguments(videoURLBundle);

        mFragmentManager.beginTransaction()
                .replace(R.id.video_player_container, videoPlayerFragment)
                .commit();


    }




}
