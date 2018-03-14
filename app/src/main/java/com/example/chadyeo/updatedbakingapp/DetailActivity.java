package com.example.chadyeo.updatedbakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chadyeo.updatedbakingapp.data.RecipeContract;
import com.example.chadyeo.updatedbakingapp.fragments.StepsDetailFragment;
import com.example.chadyeo.updatedbakingapp.fragments.StepsListFragment;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!mTwoPane) {
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String recipeName = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StepsListFragment stepsListFragment = new StepsListFragment();
        StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();

        Bundle bundle = new Bundle();

        if (findViewById(R.id.twoPane_linearLayout) != null) {
            mTwoPane = true;
            bundle.putBoolean("mTwoPane", true);

            if (savedInstanceState == null) {

                fragmentTransaction.add(R.id.twoPane_list_fragment, stepsListFragment);
                fragmentTransaction.add(R.id.twoPane_detail_fragment, stepsDetailFragment);
                fragmentTransaction.commit();
            }
        } else {
            mTwoPane = false;
            bundle.putBoolean("mTwoPane", false);

            fragmentTransaction.add(R.id.detailActivity_container, stepsListFragment);
            fragmentTransaction.commit();
        }
        stepsListFragment.setArguments(bundle);
        stepsDetailFragment.setArguments(bundle);

        Log.e(LOG_TAG, "Bundle twoPane is " + mTwoPane);
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }
}
