package com.example.android.bakeme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.models.PreparationStep;
import com.example.android.bakeme.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PreparationStep> mRecipeStepList;

    public RecipeStepListAdapter(){}


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
        int stepNumber = currentStep.getmStepId() + 1;

        String formattedDisplayDescription = stepNumber + ") " + stepDescription;

        viewHolder.stepShortDescriptionView.setText(formattedDisplayDescription);

    }

    @Override
    public int getItemCount() {
        if (mRecipeStepList == null) return 0;
        else return mRecipeStepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

    public void setStepList (ArrayList<PreparationStep> stepList){

        mRecipeStepList = stepList;
        notifyDataSetChanged();
    }
}
