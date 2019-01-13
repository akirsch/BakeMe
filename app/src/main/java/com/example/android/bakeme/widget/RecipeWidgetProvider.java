package com.example.android.bakeme.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.activities.WidgetRecipeSelectorActivity;


import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<String> ingredientDetails) {

        Intent intent = new Intent(context, WidgetRecipeSelectorActivity.class);
        // pass name of recipe to Widget Activity to display chosen recipe as selected
        intent.putExtra(Constants.WIDGET_RECIPE_NAME_STRING, ingredientDetails.get(0));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        if (ingredientDetails.get(0).equals(context.getString(R.string.app_name))){
            views.setTextViewText(R.id.ingredients_title_tv, context.getString(R.string.app_name));
            views.setTextViewText(R.id.ingredient_list_tv, context.getString(R.string.initial_widget_message));
        } else {
            views.setTextViewText(R.id.ingredients_title_tv, ingredientDetails.get(0));
            views.setTextViewText(R.id.ingredient_list_tv, ingredientDetails.get(1));
        }

        views.setOnClickPendingIntent(R.id.ingredient_list_tv, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, ArrayList<String> ingredientDetails) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredientDetails);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ArrayList<String> initialWidgetDisplayDetails = new ArrayList<>();
        initialWidgetDisplayDetails.add(context.getString(R.string.app_name));
        initialWidgetDisplayDetails.add(context.getString(R.string.initial_widget_message));

        updateRecipeWidgets(context, appWidgetManager, appWidgetIds, initialWidgetDisplayDetails);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

