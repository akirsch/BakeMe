package com.example.android.bakeme.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;

import com.example.android.bakeme.Constants;
import com.example.android.bakeme.models.Ingredient;

import java.util.ArrayList;

public class RecipeWidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET =
            "com.example.android.bakeme.widget.action.update_widget";

    public RecipeWidgetService(){
        super("RecipeWidgetService");
    }

    @Override
    protected void onHandleIntent( @Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                final ArrayList<String> ingredients =
                        intent.getStringArrayListExtra(Constants.INGREDIENTS_KEY);
                handleActionUpdateWidget(ingredients);
            }
        }

    }

    /**
     * Starts this service to perform UpdateWidget action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidget(Context context, ArrayList<String> ingredientDetails) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(Constants.INGREDIENTS_KEY, ingredientDetails);
        context.startService(intent);
    }


    private void handleActionUpdateWidget(ArrayList<String> ingredientDetails){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds,ingredientDetails);


    }
}
