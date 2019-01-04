package com.example.android.bakeme.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.adapters.RecipeStepListAdapter;
import com.example.android.bakeme.models.Ingredient;
import com.example.android.bakeme.models.PreparationStep;
import com.example.android.bakeme.models.Recipe;
import com.google.android.exoplayer2.C;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    private Unbinder mUnbinder;
    private Recipe mSelectedRecipe;
    private RecipeStepListAdapter mRecipeStepListAdapter;
    private ArrayList<Ingredient> mRecipeIngredients;
    private ArrayList<PreparationStep> mRecipePreparationSteps;


    @BindView(R.id.recipe_steps_recycler_view)
    RecyclerView mRecipeStepsRecyclerView;

    @BindView(R.id.ingredient_list_tv)
    TextView mIngredientsListView;

    // mandatory empty constructor
    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null){
            mSelectedRecipe = getArguments().getParcelable(Constants.SELECTED_RECIPE_KEY);

            if (mSelectedRecipe.getmIngredients() != null){

                mRecipeIngredients = (ArrayList<Ingredient>) mSelectedRecipe.getmIngredients();

                for (int i = 0; i < mRecipeIngredients.size(); i++ ){

                    String formattedIngredientString  = formatIngredientString(mRecipeIngredients.get(i));
                    // add each ingredient into the textView containing the ingredient list,
                    // starting a new line for each ingredient
                    mIngredientsListView.append(formattedIngredientString + "\n");
                }

            }

            if (mSelectedRecipe.getmPreparationSteps() != null) {

                mRecipePreparationSteps = (ArrayList<PreparationStep>) mSelectedRecipe.getmPreparationSteps();

                // initialize RecyclerView properties
                mRecipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecipeStepsRecyclerView.hasFixedSize();

                // create instance of RecipeListAdapter
                mRecipeStepListAdapter = new RecipeStepListAdapter();
                mRecipeStepListAdapter.setStepList(mRecipePreparationSteps);

                // connect the RecyclerView with the Adapter
                mRecipeStepsRecyclerView.setAdapter(mRecipeStepListAdapter);
            }



        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * Helper method to format the list of ingredients to be displayed
     * @param ingredient the ingredient to be formatted
     * @return the formatted ingredient string
     */
    private String formatIngredientString (Ingredient ingredient){

        double quantity = ingredient.getmQuantity();
        String unitOfMeasure = ingredient.getmUnit();
        String ingredientName = ingredient.getmIngredientName();

        String unitOfMeasureToDisplay = getDisplayMeasure(unitOfMeasure);

        return "\u2022 " + quantity + " " + unitOfMeasureToDisplay + " " + ingredientName;

    }

    /**
     * Helper method to select a user-friendly version of the unit measurement stored in the recipe data
     * @param unitOfMeasure the String for the unit of measure stored in the Ingredient data
     * @return the correct string to display
     */
    private String getDisplayMeasure (String unitOfMeasure){

        String unitToDisplay;

        switch (unitOfMeasure){
            case Constants.GRAMS_JSON_NAME:
                unitToDisplay = Constants.GRAMS_DISPLAY_NAME;
                break;
            case Constants.CUP_JSON_NAME:
                unitToDisplay = Constants.CUP_DISPLAY_NAME;
                break;
            case Constants.OUNCE_JSON_NAME:
                unitToDisplay = Constants.OUNCE_DISPLAY_NAME;
                break;
            case Constants.KILO_JSON_NAME:
                unitToDisplay = Constants.KILO_DISPLAY_NAME;
                break;
            case Constants.TEA_SPOON_JSON_NAME:
                unitToDisplay = Constants.TEA_SPOON_JSON_NAME;
                break;
            case Constants.TABLE_SPOON_JSON_NAME:
                unitToDisplay = Constants.TABLE_SPOON_JSON_NAME;
                break;
            // if unit is of type UNIT, don't display anything for the unit - as in "3 eggs"
            default: unitToDisplay = "";


        }
        return unitToDisplay;
    }
}
