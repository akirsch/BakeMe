package com.example.android.bakeme;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.bakeme.models.Ingredient;
import com.example.android.bakeme.models.PreparationStep;
import com.example.android.bakeme.models.Recipe;

import java.util.ArrayList;

public class Constants {

    // names of units in JSON source file
    public static final String GRAMS_JSON_NAME = "G";
    public static final String KILO_JSON_NAME = "K";
    public static final String OUNCE_JSON_NAME = "OZ";
    public static final String CUP_JSON_NAME = "CUP";
    public static final String TEA_SPOON_JSON_NAME = "TSP";
    public static final String TABLE_SPOON_JSON_NAME = "TBLSP";
    public static final String UNIT_JSON_NAME = "UNIT";

    // names of units to display in app
    public static final String GRAMS_DISPLAY_NAME = "Grams";
    public static final String KILO_DISPLAY_NAME = "Kilo(s)";
    public static final String OUNCE_DISPLAY_NAME = "Ounces";
    public static final String CUP_DISPLAY_NAME = "Cups(s)";

    // Base URL for accessing the recipe Json data
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static final long CONNECTION_TIMEOUT_IN_MILLIS = 15000;

    //Keys to pass data with intents
    public static final String SELECTED_RECIPE_KEY = "selected recipe";

}
