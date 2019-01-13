package com.example.android.bakeme.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.android.bakeme.utils.Utils;

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

        if (savedInstanceState != null){
            mRecipeIngredients = savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS_KEY);
        }

        if (getArguments() != null){
            mSelectedRecipe = getArguments().getParcelable(Constants.SELECTED_RECIPE_KEY);

            if (mSelectedRecipe.getmIngredients() != null){

                mRecipeIngredients = (ArrayList<Ingredient>) mSelectedRecipe.getmIngredients();

                // for each ingredient, create string of text to contain all the details
                for (int i = 0; i < mRecipeIngredients.size(); i++ ){

                    String formattedIngredientString  = Utils.formatIngredientString(mRecipeIngredients.get(i));
                    // add each ingredient into the textView containing the ingredient list,
                    // starting a new line for each ingredient
                    mIngredientsListView.append(formattedIngredientString);
                }

            }

            if (mSelectedRecipe.getmPreparationSteps() != null) {

                mRecipePreparationSteps = (ArrayList<PreparationStep>) mSelectedRecipe.getmPreparationSteps();

                // initialize RecyclerView properties
                RecyclerView.LayoutManager recipeStepsLayoutManager = new LinearLayoutManager(getActivity());
                mRecipeStepsRecyclerView.setLayoutManager(recipeStepsLayoutManager);
                mRecipeStepsRecyclerView.hasFixedSize();

                // add a divider between each line for visual polish
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecipeStepsRecyclerView.getContext(),
                        ((LinearLayoutManager) recipeStepsLayoutManager).getOrientation());
                mRecipeStepsRecyclerView.addItemDecoration(dividerItemDecoration);

                // determine whether fragment is being displayed on phone or tablet.
                boolean isTwoPane = getArguments().getBoolean(Constants.BOOLEAN_IS_TWO_PANE_KEY);

                // create instance of RecipeListAdapter
                mRecipeStepListAdapter = new RecipeStepListAdapter(getActivity(), isTwoPane);
                mRecipeStepListAdapter.setStepList(mRecipePreparationSteps);
                mRecipeStepListAdapter.setRecipe(mSelectedRecipe);

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.INGREDIENTS_KEY, mRecipeIngredients);
    }




}
