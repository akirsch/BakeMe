package com.example.android.bakeme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.models.Ingredient;
import com.example.android.bakeme.models.Recipe;
import com.example.android.bakeme.utils.Utils;
import com.example.android.bakeme.widget.RecipeWidgetService;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetRecipeSelectorActivity extends AppCompatActivity {

    private static final int RADIO_BUTTON_ARRAY_LENGTH = 4;
    private static final int NO_RECIPE_SELECTED = -1;
    private ArrayList<Recipe> mRecipeList;
    private RadioButton[] mRadioButtonArray = new RadioButton[4];
    private Recipe mSelectedRecipe;
    private String mSelectedRecipeName;
    private ArrayList<Ingredient> mSelectedRecipeIngredients;
    private String selectedRecipeFromWidget = null;


    @BindView(R.id.recipes_selection_group)
    RadioGroup mRecipesRadioGroup;
    @BindView(R.id.radio_button_recipe_one)
    RadioButton mRadioButtonRecipe1;
    @BindView(R.id.radio_button_recipe_two)
    RadioButton mRadioButtonRecipe2;
    @BindView(R.id.radio_button_recipe_three)
    RadioButton mRadioButtonRecipe3;
    @BindView(R.id.radio_button_recipe_four)
    RadioButton mRadioButtonRecipe4;
    @BindView(R.id.confirm_selection_button)
    FloatingActionButton mConfirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_recipe_selector);

        ButterKnife.bind(this);

        // Set App Bar title to name of project
        Toolbar myToolbar = findViewById(R.id.toolbar);
        // Set the Toolbar as Action Bar
        setSupportActionBar(myToolbar);
        // Set title of action bar to appropriate label for this Activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        // Set the padding to match the Status Bar height (to avoid title being cut off by
        // transparent toolbar
        myToolbar.setPadding(0, 25, 0, 0);

        mRadioButtonArray[0] = mRadioButtonRecipe1;
        mRadioButtonArray[1] = mRadioButtonRecipe2;
        mRadioButtonArray[2] = mRadioButtonRecipe3;
        mRadioButtonArray[3] = mRadioButtonRecipe4;


        if (MainActivity.sRecipeList == null) {
            // if activity has not yet been opened, start the Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        mRecipeList = MainActivity.sRecipeList;


        // if recipe list has been downloaded, set names of each radio button to match each recipe
        for (int i = 0; i < RADIO_BUTTON_ARRAY_LENGTH; i++) {
            mRadioButtonArray[i].setText(mRecipeList.get(i).getmRecipeName());
        }

        Intent intent = getIntent();

        if(intent != null){
            selectedRecipeFromWidget = intent.getStringExtra(Constants.WIDGET_RECIPE_NAME_STRING);
            // if recipe has already been selected in widget, display this option as selected
            for (int i = 0; i < RADIO_BUTTON_ARRAY_LENGTH; i++) {
                if (!selectedRecipeFromWidget.equals(getString(R.string.app_name))){
                    if (mRadioButtonArray[i].getText().equals(selectedRecipeFromWidget)){
                        mRadioButtonArray[i].setChecked(true);
                    }
                }
            }
        }




    }


    public void onConfirmButtonClicked(View view) {

        if (mRecipeList == null) {

            // if no recipe list is available, close the Activity
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            // If recipe lists exists, but no recipe is selected, just exit this activity
        } else if (mRecipesRadioGroup.getCheckedRadioButtonId() == NO_RECIPE_SELECTED) {

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);


        } else {

            // get Id of checked radio button
            int selectedRadioButtonId = mRecipesRadioGroup.getCheckedRadioButtonId();

            // get the recipe that corresponds to the checked radio button
            for (int i = 0; i < RADIO_BUTTON_ARRAY_LENGTH; i++) {
                if(mRadioButtonArray[i].getId() == selectedRadioButtonId){
                    mSelectedRecipe = mRecipeList.get(i);
                }
            }
            // get the name and list of ingredients for the selected recipe
            mSelectedRecipeName = mSelectedRecipe.getmRecipeName();
            mSelectedRecipeIngredients = (ArrayList<Ingredient>) mSelectedRecipe.getmIngredients();

            // As there is no method to append a text view in a widget to which this data will be passed,
            // use a StringBuilder to create a long string made up of all the ingredients for this recipe.
            StringBuilder ingredientStringBuilder = new StringBuilder();

            for (int i = 0; i < mSelectedRecipeIngredients.size(); i++) {

                String formattedIngredientString = Utils.formatIngredientString(mSelectedRecipeIngredients.get(i));
                ingredientStringBuilder.append(formattedIngredientString);
            }

            String completeIngredientDetailsArray = ingredientStringBuilder.toString();

            ArrayList<String> widgetDisplayStringList = new ArrayList<>();
            widgetDisplayStringList.add(mSelectedRecipeName);
            widgetDisplayStringList.add(completeIngredientDetailsArray);

            // force a widget update that will display the selected recipe ingredients in the app widget
            RecipeWidgetService.startActionUpdateWidget(this, widgetDisplayStringList);
            // exit activity
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }


}
