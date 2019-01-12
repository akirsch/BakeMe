package com.example.android.bakeme.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.activities.PreparationStepActivity;
import com.example.android.bakeme.fragments.StepInstructionsFragment;
import com.example.android.bakeme.fragments.VideoPlayerFragment;
import com.example.android.bakeme.models.PreparationStep;
import com.example.android.bakeme.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PreparationStep> mRecipeStepList;
    private Recipe mCurrentRecipe;
    private FragmentManager mFragmentManager;
    private boolean mIsTwoPane;

    public RecipeStepListAdapter(Context context, boolean isTwoPane) {

        this.mContext = context;
        this.mIsTwoPane = isTwoPane;
    }


    @NonNull
    @Override
    public RecipeStepListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View stepItemView = inflater.inflate(R.layout.recipe_step_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(stepItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepListAdapter.ViewHolder viewHolder, final int position) {


        // Get the data model based on position
        final PreparationStep currentStep = mRecipeStepList.get(position);

        String stepDescription = currentStep.getmShortDescription();

        // as steps are indexed starting at 0, the actual step number is one more than the step index
        final int stepNumber = currentStep.getmStepId();

        String formattedDisplayDescription = stepNumber + ") " + stepDescription;

        viewHolder.stepShortDescriptionView.setText(formattedDisplayDescription);

        // get data to pass in Intent to open up PreparationStepActivity
        final String currentStepLongDescription = currentStep.getmLongDescription();
        final String currentStepVideoURL = currentStep.getmVideoURL();
        final String currentRecipeName = mCurrentRecipe.getmRecipeName();


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // if user is using app in phone, open a new Activity when step is pressed on.
                if (!mIsTwoPane) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.RECIPE_NAME_KEY, currentRecipeName);
                    bundle.putInt(Constants.STEP_NUM_KEY, stepNumber);
                    bundle.putString(Constants.STEP_LONG_DESC_KEY, currentStepLongDescription);

                    // only put the URL string in the bundle if it is not null or empty, else pass the
                    // Id for the correct image to display in the ExoPlayerView
                    if (currentStepVideoURL != null && !TextUtils.isEmpty(currentStepVideoURL)) {
                        bundle.putString(Constants.VIDEO_URL_KEY, currentStepVideoURL);
                    } else {
                        final int defaultRecipeDisplayImage = setRecipeImageId(mContext, currentRecipeName);
                        bundle.putInt(Constants.DEFAULT_IMAGE_KEY, defaultRecipeDisplayImage);
                    }

                    Intent selectedRecipeStepIntent = new Intent(context, PreparationStepActivity.class);
                    selectedRecipeStepIntent.putExtras(bundle);

                    if (selectedRecipeStepIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(selectedRecipeStepIntent);
                    }

                    // if user is using app on a tablet, instead of opening a new activity when recipe
                    // step is clicked on, display the correct video and description
                    // in the relevant Fragments.
                } else {

                    mFragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();

                    // set data in instructionFragment
                    StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();

                    // create bundle just for storing the step description
                    Bundle instructionBundle = new Bundle();
                    instructionBundle.putString(Constants.STEP_LONG_DESC_KEY, currentStepLongDescription);

                    // pass the data received by this activity with the intent that created it to the Fragment
                    // contained within it.
                    stepInstructionsFragment.setArguments(instructionBundle);

                    mFragmentManager.beginTransaction()
                            .replace(R.id.step_description_container, stepInstructionsFragment)
                            .commit();

                    // set data in video player fragment
                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();

                    // create bundle just for storing the video URL or relevant image
                    Bundle videoURLBundle = new Bundle();

                    int recipeImageId = setRecipeImageId(mContext, currentRecipeName);

                    //if the video URL isn't not null or an empty string, pass it to the Fragment, else, pass the
                    // image Id if it exists
                    if (currentStepVideoURL != null && !TextUtils.isEmpty(currentStepVideoURL)) {
                        videoURLBundle.putString(Constants.VIDEO_URL_KEY, currentStepVideoURL);
                    } else if (recipeImageId != Constants.NO_IMAGE_ID) {
                        videoURLBundle.putInt(Constants.DEFAULT_IMAGE_KEY, recipeImageId);
                    }

                    // pass the data received by this activity with the intent that created it to the Fragment
                    // contained within it.
                    videoPlayerFragment.setArguments(videoURLBundle);

                    mFragmentManager.beginTransaction()
                            .replace(R.id.video_player_container, videoPlayerFragment)
                            .commit();


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        if (mRecipeStepList == null) return 0;
        else return mRecipeStepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        @BindView(R.id.recipe_step_short_desc)
        TextView stepShortDescriptionView;

        @BindView(R.id.play_video_for_step_button)
        ImageView playVideoForStepButton;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);

        }

    }

    public void setStepList(ArrayList<PreparationStep> stepList) {

        mRecipeStepList = stepList;
        notifyDataSetChanged();
    }

    public void setRecipe(Recipe currentRecipe) {

        mCurrentRecipe = currentRecipe;
    }

    public int setRecipeImageId(Context context, String name) {

        int resourceId;

        if (name.equals(context.getString(R.string.nutella_pie))) {
            resourceId = R.drawable.nutellapie;
        } else if (name.equals(context.getString(R.string.brownies))) {
            resourceId = R.drawable.brownies;
        } else if (name.equals(context.getString(R.string.yellow_cake))) {
            resourceId = R.drawable.vanillacake;
        } else if (name.equals(context.getString(R.string.cheesecake))) {
            resourceId = R.drawable.cheesecake;
        } else resourceId = R.drawable.ic_launcher_background;

        return resourceId;
    }
}
