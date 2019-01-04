package com.example.android.bakeme.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.fragments.RecipeDetailFragment;
import com.example.android.bakeme.models.Recipe;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private Bundle mSelectedRecipeBundle;
    private Recipe mSelectedRecipe;
    private FragmentManager mFragmentManager;

    @BindView(R.id.toolbar) Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        // get data for selected recipe passed by intent from MainActivity
        if (getIntent().getExtras() != null){
            mSelectedRecipeBundle = getIntent().getExtras();
            mSelectedRecipe = mSelectedRecipeBundle.getParcelable(Constants.SELECTED_RECIPE_KEY);
        }

        // Set the Toolbar as Action Bar
        setSupportActionBar(myToolbar);
        // Set title of action bar to appropriate label for this Activity
        if (mSelectedRecipe != null && !TextUtils.isEmpty(mSelectedRecipe.getmRecipeName())){
            Objects.requireNonNull(getSupportActionBar()).setTitle(mSelectedRecipe.getmRecipeName());
        } else Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        // Set the padding to match the Status Bar height (to avoid title being cut off by
        // transparent toolbar
        myToolbar.setPadding(0, 25, 0, 0);

        // create instance of fragment manager to enable Fragment transactions
        mFragmentManager = getSupportFragmentManager();

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

        // pass the data received by this activity with the intent that created it to the Fragment
        // contained within it.
        recipeDetailFragment.setArguments(mSelectedRecipeBundle);

        // replace the container in this Activity's layout with a RecipeDetailFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, recipeDetailFragment)
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
