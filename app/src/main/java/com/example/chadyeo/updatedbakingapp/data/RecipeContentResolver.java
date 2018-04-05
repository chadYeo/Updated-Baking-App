package com.example.chadyeo.updatedbakingapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chadyeo.updatedbakingapp.model.Ingredient;
import com.example.chadyeo.updatedbakingapp.model.Recipe;
import com.example.chadyeo.updatedbakingapp.model.Step;

import org.json.JSONException;

import java.util.ArrayList;

public class RecipeContentResolver {

    private static final String LOG_TAG = RecipeContentResolver.class.getSimpleName();

    public static void insertContentResolver(Context context, ArrayList<Recipe> recipes) {
        insertRecipeCR(context, recipes);
        insertIngredientsCR(context, recipes);
        insertStepsCR(context, recipes);
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
                        recipeContentResolver.delete(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI, null, null);
                        recipeContentResolver.bulkInsert(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI, recipeValues);
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
            mRecipeValue.put(RecipeContract.RecipeEntry.RECIPES_COLUMN_ID, data.getId());
            mRecipeValue.put(RecipeContract.RecipeEntry.RECIPES_COLUMN_NAME, data.getName());
            mRecipeValue.put(RecipeContract.RecipeEntry.RECIPES_COLUMN_SERVINGS, data.getServings());
            recipeContentValues[i] = mRecipeValue;
        }
        return recipeContentValues;
    }

    private static void insertIngredientsCR(final Context context, final ArrayList<Recipe> data) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    ContentValues[] ingredientValues = getIngredientsContentValuesFromArray(context, data);
                    if (ingredientValues != null && ingredientValues.length != 0) {
                        ContentResolver ingredientsContentResolver = context.getContentResolver();
                        ingredientsContentResolver.delete(RecipeContract.RecipeEntry.INGREDIENT_CONTENT_URI, null, null);
                        ingredientsContentResolver.bulkInsert(RecipeContract.RecipeEntry.INGREDIENT_CONTENT_URI, ingredientValues);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private static ContentValues[] getIngredientsContentValuesFromArray(Context context, ArrayList<Recipe> recipes) {

        ArrayList<ContentValues> ingredientContentValuesArrayList = new ArrayList<>();

        for (int i=0; i<recipes.size(); i++) {
            Recipe data = recipes.get(i);
            ArrayList<Ingredient> ingredients = data.getIngredients();

            for (int j=0; j < ingredients.size(); j++) {
                Ingredient currentIngredient = ingredients.get(j);

                ContentValues mIngredientValues = new ContentValues();
                mIngredientValues.put(RecipeContract.RecipeEntry.RECIPES_COLUMN_ID, data.getId());
                mIngredientValues.put(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_INGREDIENT, currentIngredient.getIngredient());
                mIngredientValues.put(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_QUANTITY, currentIngredient.getQuantity());
                mIngredientValues.put(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN_MEASURE, currentIngredient.getMeasure());
                ingredientContentValuesArrayList.add(mIngredientValues);

            }
        }
        ContentValues[] ingredientContentValue = new ContentValues[ingredientContentValuesArrayList.size()];
        ingredientContentValue = ingredientContentValuesArrayList.toArray(ingredientContentValue);

        return ingredientContentValue;
    }

    private static void insertStepsCR(final Context context, final ArrayList<Recipe> data) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    ContentValues[] stepsValues = getStepsContentValuesFromArray(context, data);
                    if (stepsValues != null && stepsValues.length != 0) {
                        ContentResolver stepsContentResolver = context.getContentResolver();
                        stepsContentResolver.delete(RecipeContract.RecipeEntry.STEPS_CONTENT_URI, null, null);
                        stepsContentResolver.bulkInsert(RecipeContract.RecipeEntry.STEPS_CONTENT_URI, stepsValues);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private static ContentValues[] getStepsContentValuesFromArray(Context context, ArrayList<Recipe> recipes) {
        ArrayList<ContentValues> stepsContentValuesArrayList = new ArrayList<>();

        for (int i=0; i<recipes.size(); i++) {
            Recipe data = recipes.get(i);
            ArrayList<Step> steps = data.getSteps();

            for(int j=0; j<steps.size(); j++) {
                Step currentStep = steps.get(j);

                ContentValues mStepValues = new ContentValues();
                mStepValues.put(RecipeContract.RecipeEntry.RECIPES_COLUMN_ID, data.getId());
                mStepValues.put(RecipeContract.RecipeEntry.STEPS_COLUMN_ID, currentStep.getId());
                mStepValues.put(RecipeContract.RecipeEntry.STEPS_COLUMN_SHORT_DESC, currentStep.getShortDescription());
                mStepValues.put(RecipeContract.RecipeEntry.STEPS_COLUMN_DESC, currentStep.getDescription());
                mStepValues.put(RecipeContract.RecipeEntry.STEPS_COLUMN_VIDEOURL, currentStep.getVideoURL());
                mStepValues.put(RecipeContract.RecipeEntry.STEPS_COLUMN_THUMBNAILURL, currentStep.getThumbnailURL());
                stepsContentValuesArrayList.add(mStepValues);
            }
        }
        ContentValues[] stepContentValues = new ContentValues[stepsContentValuesArrayList.size()];
        stepContentValues = stepsContentValuesArrayList.toArray(stepContentValues);

        return stepContentValues;
    }
}
