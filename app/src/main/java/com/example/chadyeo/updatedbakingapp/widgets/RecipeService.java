package com.example.chadyeo.updatedbakingapp.widgets;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.chadyeo.updatedbakingapp.data.RecipeContract;
import com.example.chadyeo.updatedbakingapp.model.Ingredient;
import com.example.chadyeo.updatedbakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeService extends IntentService {

    ArrayList<Recipe> recipes;
    ArrayList<Ingredient> ingredients;

    public RecipeService() {
        super("RecipeService");
    }

    public static void startActionRecipe(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            /**
            Cursor mRecipeCursor = getContentResolver().query(
                    RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            recipes = new ArrayList<>();
            if (mRecipeCursor != null && mRecipeCursor.moveToFirst()) {
                int mRecipeIdIndex = mRecipeCursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPES_COLUMN_ID);
                int mRecipeNameIndex = mRecipeCursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPES_COLUMN_NAME);
                do {
                    Recipe recipe = new Recipe();
                    recipe.setId(mRecipeCursor.getInt(mRecipeIdIndex));
                    recipe.setName(mRecipeCursor.getString(mRecipeNameIndex));
                    recipes.add(recipe);
                }
                while (mRecipeCursor.moveToNext());
                mRecipeCursor.close();
            }
            **/

            String[] selectionArgs = {"1"};

            Cursor mIngredientCursor = getContentResolver().query(
                    RecipeContract.RecipeEntry.INGREDIENT_CONTENT_URI,
                    null,
                    null,
                    selectionArgs,
                    RecipeContract.RecipeEntry.RECIPES_COLUMN_ID + " ASC");

            ingredients = new ArrayList<>();

            if (mIngredientCursor != null && mIngredientCursor.moveToFirst()) {
                int ingredientIndex = mIngredientCursor.getColumnIndex(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_INGREDIENT);
                int qtyIndex = mIngredientCursor.getColumnIndex(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_QUANTITY);
                int measureIndex = mIngredientCursor.getColumnIndex(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_MEASURE);

                do {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredient(mIngredientCursor.getString(ingredientIndex));
                    ingredient.setQuantity(mIngredientCursor.getFloat(qtyIndex));
                    ingredient.setMeasure(mIngredientCursor.getString(measureIndex));
                    ingredients.add(ingredient);
                } while (mIngredientCursor.moveToNext());
                mIngredientCursor.close();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
                String recipeName = "Nutella Pie";
                // String recipeName = recipes.get(0).getName();
                BakingWidgetProvider.updateRecipeWidgets(getApplicationContext(), appWidgetManager, appWidgetIds, ingredients, recipeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
