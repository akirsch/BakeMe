package com.example.android.bakeme.activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.fragments.StepInstructionsFragment;
import com.example.android.bakeme.fragments.VideoPlayerFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreparationStepActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar myToolbar;

    // initialize tool bar title to display name of app
    private String mToolBarTitle;
    private Bundle mIntentBundle;
    private String mStepDescription;
    private String mStepVideoURL;
    private int mDefaultImageId;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_step);

        ButterKnife.bind(this);

        mToolBarTitle = getString(R.string.app_name);

        // get data for selected recipe passed by intent from MainActivity
        if (getIntent().getExtras() != null) {
            mIntentBundle = getIntent().getExtras();

        // set correct String for toolbar title
            // for first step, indexed as 0, display "Introduction"
            if (mIntentBundle.getInt(Constants.STEP_NUM_KEY) == 0){
                mToolBarTitle = mIntentBundle.getString(Constants.RECIPE_NAME_KEY) +": "
                        + getString(R.string.introduction);
                // else display step with correct number
            } else {
                mToolBarTitle = mIntentBundle.getString(Constants.RECIPE_NAME_KEY) +": "
                        + getString(R.string.step) + " "
                        + mIntentBundle.getInt(Constants.STEP_NUM_KEY);
            }


            mStepDescription = mIntentBundle.getString(Constants.STEP_LONG_DESC_KEY);

            // if the intent which opened this Activity contains a video URL, initialize its variable,
            // and set variable for image Id constant denoting no id is present.
            if (mIntentBundle.containsKey(Constants.VIDEO_URL_KEY)){
                mStepVideoURL = mIntentBundle.getString(Constants.VIDEO_URL_KEY);
                mDefaultImageId = Constants.NO_IMAGE_ID;
                // if no URL is included in the Intent, set its variable to null and instead get the
                // image id instead.
            } else if (mIntentBundle.containsKey(Constants.DEFAULT_IMAGE_KEY)){
                mDefaultImageId = mIntentBundle.getInt(Constants.DEFAULT_IMAGE_KEY);
                mStepVideoURL = null;
            }


        }


        // Set the Toolbar as Action Bar
        setSupportActionBar(myToolbar);
        // Set title of action bar to appropriate label for this Activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(mToolBarTitle);

        // enable up navigation to parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragmentManager = getSupportFragmentManager();

        // only display the text of the instruction steps in portrait mode, but in horizontal mode,
        // show only the video on full screen.
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setUpInstructionsFragment();
        }


        setUpVideoFragment();


    }

    private void setUpInstructionsFragment(){

        StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();

        // create bundle just for storing the step description
        Bundle instructionBundle = new Bundle();
        instructionBundle.putString(Constants.STEP_LONG_DESC_KEY, mStepDescription);

        // pass the data received by this activity with the intent that created it to the Fragment
        // contained within it.
        stepInstructionsFragment.setArguments(instructionBundle);

        mFragmentManager.beginTransaction()
                .replace(R.id.step_description_container, stepInstructionsFragment)
                .commit();

    }

    private void setUpVideoFragment(){

        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();

        // create bundle just for storing the step description
        Bundle videoURLBundle = new Bundle();


        //if the video URL isn't not null or an empty string, pass it to the Fragment, else, pass the
        // image Id if it exists
        if (mStepVideoURL != null && !TextUtils.isEmpty(mStepVideoURL)){
            videoURLBundle.putString(Constants.VIDEO_URL_KEY, mStepVideoURL);
        } else if (mDefaultImageId != Constants.NO_IMAGE_ID) {
            videoURLBundle.putInt(Constants.DEFAULT_IMAGE_KEY, mDefaultImageId);
        }

        // pass the data received by this activity with the intent that created it to the Fragment
        // contained within it.
        videoPlayerFragment.setArguments(videoURLBundle);

        mFragmentManager.beginTransaction()
                .replace(R.id.video_player_container, videoPlayerFragment)
                .commit();


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


}
