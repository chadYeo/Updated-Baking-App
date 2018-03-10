package com.example.chadyeo.updatedbakingapp;

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

import com.example.chadyeo.updatedbakingapp.data.RecipeContract;
import com.example.chadyeo.updatedbakingapp.fragments.StepsDetailFragment;
import com.example.chadyeo.updatedbakingapp.fragments.StepsListFragment;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (findViewById(R.id.twoPane_linearLayout) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                StepsListFragment stepsListFragment = new StepsListFragment();
                fragmentTransaction.add(R.id.twoPane_list_fragment, stepsListFragment);
                fragmentTransaction.commit();

                StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
                fragmentTransaction.add(R.id.twoPane_detail_fragment, stepsDetailFragment);
                fragmentTransaction.commit();
            }
        } else {
            mTwoPane = false;
            StepsListFragment stepsListFragment = new StepsListFragment();
            fragmentTransaction.add(R.id.detailActivity_container, stepsListFragment);
            fragmentTransaction.commit();
        }
    }
}
