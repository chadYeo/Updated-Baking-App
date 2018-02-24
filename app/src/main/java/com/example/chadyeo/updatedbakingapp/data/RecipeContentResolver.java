package com.example.chadyeo.updatedbakingapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chadyeo.updatedbakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeContentResolver {

    private static final String LOG_TAG = RecipeContentResolver.class.getSimpleName();

    public static void insertContentResolver(Context context, ArrayList<Recipe> recipes) {
        insertRecipeCR(context, recipes);
        Log.v(LOG_TAG, "Inserting data thru ContentResolver is initiated");
    }

    private static void insertRecipeCR(final Context context, final ArrayList<Recipe> recipes) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    ContentValues[] recipeValues = getRecipeContentValuesFromArray(context, recipes);
                    if (recipeValues != null && recipeValues.length != 0) {
                        ContentResolver recipeContentResolver = context.getContentResolver();
                        recipeContentResolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private static ContentValues[] getRecipeContentValuesFromArray(Context context, ArrayList<Recipe> recipes) {

        ContentValues[] recipeContentValues = new ContentValues[recipes.size()];

        for (int i=0; i<recipes.size(); i++) {
            Recipe data = recipes.get(i);
            ContentValues mRecipeValue = new ContentValues();
            mRecipeValue.put(RecipeContract.RecipeEntry.COLUMN_NAME, data.getName());
            mRecipeValue.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, data.getServings());
            recipeContentValues[i] = mRecipeValue;
        }
        return recipeContentValues;
    }
}
