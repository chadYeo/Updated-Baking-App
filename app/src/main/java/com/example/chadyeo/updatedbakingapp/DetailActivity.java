package com.example.chadyeo.updatedbakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.chadyeo.updatedbakingapp.fragments.StepsListFragment;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            selectPopBackStackDetailTag();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        selectPopBackStackDetailTag();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StepsListFragment stepsListFragment = new StepsListFragment();

        fragmentTransaction.add(R.id.detailActivity_container, stepsListFragment);
        fragmentTransaction.commit();
    }

    private void selectPopBackStackDetailTag() {
        if (getSupportFragmentManager().findFragmentByTag("Steps_List_TAG") != null) {
            getSupportFragmentManager().popBackStack("Steps_List_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }
}
