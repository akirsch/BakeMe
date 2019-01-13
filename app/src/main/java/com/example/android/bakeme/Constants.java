package com.example.android.bakeme;


public class Constants {

    // names of units in JSON source file
    public static final String GRAMS_JSON_NAME = "G";
    public static final String KILO_JSON_NAME = "K";
    public static final String OUNCE_JSON_NAME = "OZ";
    public static final String CUP_JSON_NAME = "CUP";
    public static final String TEA_SPOON_JSON_NAME = "TSP";
    public static final String TABLE_SPOON_JSON_NAME = "TBLSP";

    // names of units to display in app
    public static final String GRAMS_DISPLAY_NAME = "Grams";
    public static final String KILO_DISPLAY_NAME = "Kilo(s)";
    public static final String OUNCE_DISPLAY_NAME = "Ounces";
    public static final String CUP_DISPLAY_NAME = "Cups(s)";
    public static final String TEA_SPOON_DISPLAY_NAME = "tsp";
    public static final String TABLE_SPOON_DISPLAY_NAME = "tblsp";

    // Base URL for accessing the recipe Json data
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static final long CONNECTION_TIMEOUT_IN_MILLIS = 15000;

    //Keys to pass data with intents
    public static final String SELECTED_RECIPE_KEY = "selected recipe";
    public static final String STEP_LONG_DESC_KEY = "long desc";
    public static final String STEP_NUM_KEY = "step number";
    public static final String RECIPE_NAME_KEY = "recipe name";
    public static final String VIDEO_URL_KEY = "video url";
    public static final String DEFAULT_IMAGE_KEY = "default image";
    public static final String BOOLEAN_IS_TWO_PANE_KEY = "isTwoPane";
    public static final String WIDGET_RECIPE_NAME_STRING = "widget recipe name";

    public static final int NO_IMAGE_ID = 333;

    //Key for video current position to save on configuration change
    public static final String CURRENT_VIDEO_POSITION = "current position";
    public static final String INGREDIENTS_KEY = "ingredients key";
    public static final String INSTRUCTIONS_KEY = "instructions key";

}
