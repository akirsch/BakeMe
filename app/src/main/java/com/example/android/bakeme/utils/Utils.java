package com.example.android.bakeme.utils;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.models.Ingredient;

public class Utils {

    /**
     * Helper method to format the list of ingredients to be displayed
     * @param ingredient the ingredient to be formatted
     * @return the formatted ingredient string
     */
    public static String formatIngredientString (Ingredient ingredient){

        double quantity = ingredient.getmQuantity();

        // remove the trailing .0 from the double value
        String quantityToDisplay = String.format("%.0f", quantity);

        String unitOfMeasure = ingredient.getmUnit();
        String ingredientName = ingredient.getmIngredientName();

        String unitOfMeasureToDisplay = getDisplayMeasure(unitOfMeasure);

        // show each ingredient in list with bullet point at beginning of line
        return "\u2022 " + quantityToDisplay + " " + unitOfMeasureToDisplay + " " + ingredientName + "\n";

    }

    /**
     * Helper method to select a user-friendly version of the unit measurement stored in the recipe data
     * @param unitOfMeasure the String for the unit of measure stored in the Ingredient data
     * @return the correct string to display
     */
    private static String getDisplayMeasure (String unitOfMeasure){

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
                unitToDisplay = Constants.TEA_SPOON_DISPLAY_NAME;
                break;
            case Constants.TABLE_SPOON_JSON_NAME:
                unitToDisplay = Constants.TABLE_SPOON_DISPLAY_NAME;
                break;
            // if unit is of type UNIT, don't display anything for the unit - as in "3 eggs"
            default: unitToDisplay = "";

        }
        return unitToDisplay;
    }
}
