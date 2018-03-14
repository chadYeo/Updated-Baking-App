package com.example.chadyeo.updatedbakingapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.example.chadyeo.updatedbakingapp.adapter.RecipeAdapter;
import com.example.chadyeo.updatedbakingapp.api.BakingRetrofitClient;
import com.example.chadyeo.updatedbakingapp.api.BakingRetrofitService;
import com.example.chadyeo.updatedbakingapp.data.RecipeContentResolver;
import com.example.chadyeo.updatedbakingapp.data.RecipeContract;
import com.example.chadyeo.updatedbakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayList<Recipe> recipes;
    private RecyclerView mRecyclerView;
    private BakingRetrofitService mService;
    private RecipeAdapter mRecipeAdapter;
    private boolean twoPane;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = BakingRetrofitClient.getClient().create(BakingRetrofitService.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.recipes_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        if (dpWidth >= 600) {
            twoPane = true;
        } else {
            twoPane = false;
        }

        displayData();
        getIdlingResource();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        try {
            return new CursorLoader(this, RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
                    null,
                    null,
                    null,
                    RecipeContract.RecipeEntry._ID + " ASC");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() ==0) {
            return;
        }
        recipes = new ArrayList<>();
        recipes.clear();
        if (data != null && data.moveToFirst()) {
            int mRecipeIdIndex = data.getColumnIndex(RecipeContract.RecipeEntry.RECIPES_COLUMN_ID);
            int mRecipeNameIndex = data.getColumnIndex(RecipeContract.RecipeEntry.RECIPES_COLUMN_NAME);
            int mRecipeServingsIndex = data.getColumnIndex(RecipeContract.RecipeEntry.RECIPES_COLUMN_SERVINGS);
            do {
                Recipe recipe = new Recipe();
                recipe.setId(data.getInt(mRecipeIdIndex));
                recipe.setName(data.getString(mRecipeNameIndex));
                recipe.setServings(data.getInt(mRecipeServingsIndex));
            }
            while (data.moveToNext());
        }
        populateRecipeList();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void displayData() {
        if (recipes != null) {
            populateRecipeList();
        } else {
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            Call<List<Recipe>> call = mService.getRecipes();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipes = new ArrayList<>(response.body());
                        populateRecipeList();
                        RecipeContentResolver.insertContentResolver(getApplicationContext(), recipes);
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                        Log.v(LOG_TAG, "displayData: onResponse is successful");
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.d(LOG_TAG, "Error Loading from Api: " + t);
                }
            });
        }
    }
    private void populateRecipeList() {
        mRecipeAdapter = new RecipeAdapter(this, recipes);
        mRecyclerView.setAdapter(mRecipeAdapter);

        Log.v(LOG_TAG, "populateRecipeList initiated");
    }

    public boolean isTwoPane() {
        return twoPane;
    }
}
