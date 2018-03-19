package com.example.chadyeo.updatedbakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.MainActivity;
import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Ingredient> ingredients, String recipeName) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        // views.setTextViewText(R.id.recipe_name_appwidget_text, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);
        insertIngredientsData(views, ingredients, context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.recipe_name_appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, ArrayList<Ingredient> ingredients, String recipeName) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredients, recipeName);
        }
    }

    private static void insertIngredientsData(RemoteViews remoteView, ArrayList<Ingredient> ingredients, Context context) {
        for (Ingredient ingredient : ingredients) {
            float qty;
            String measure;
            String actualIngredient;
            RemoteViews mIngredientView = new RemoteViews(context.getPackageName(), R.layout.baking_widget_ingredients);
            qty = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            actualIngredient = ingredient.getIngredient();

            String fullDesc = "  - " + String.valueOf(qty) + " " + measure + " of " + actualIngredient + "\n";
            mIngredientView.setTextViewText(R.id.widget_ingredients, fullDesc);
            mIngredientView.addView(R.id.widget_ingredients_container, remoteView);
        }
    }
    @Override
    public void onEnabled(Context context) {
        RecipeService.startActionRecipe(context);
        super.onEnabled(context);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

