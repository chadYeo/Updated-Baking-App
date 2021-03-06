package com.example.chadyeo.updatedbakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.chadyeo.updatedbakingapp.fragments.StepsDetailFragment;
import com.example.chadyeo.updatedbakingapp.fragments.StepsListFragment;
import com.example.chadyeo.updatedbakingapp.widgets.BakingWidgetProvider;

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
        StepsListFragment stepsListFragment = (StepsListFragment)getSupportFragmentManager()
                .findFragmentByTag(StepsListFragment.class.getCanonicalName());
        if(stepsListFragment==null){
            stepsListFragment = new StepsListFragment();
        }

        StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();

        Bundle bundle = new Bundle();

        if (savedInstanceState == null) {
            if (findViewById(R.id.twoPane_linearLayout) != null) {
                mTwoPane = true;
                bundle.putBoolean("mTwoPane", true);

                fragmentTransaction.replace(R.id.twoPane_list_fragment, stepsListFragment);
                fragmentTransaction.replace(R.id.twoPane_detail_fragment, stepsDetailFragment);

                stepsListFragment.setArguments(bundle);
                stepsDetailFragment.setArguments(bundle);
                fragmentTransaction.commit();
            } else {
                mTwoPane = false;
                bundle.putBoolean("mTwoPane", false);

                fragmentTransaction.replace(R.id.detailActivity_container, stepsListFragment,StepsListFragment.class.getCanonicalName());

                stepsListFragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        }

        Log.e(LOG_TAG, "Bundle twoPane is " + mTwoPane);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }
}
