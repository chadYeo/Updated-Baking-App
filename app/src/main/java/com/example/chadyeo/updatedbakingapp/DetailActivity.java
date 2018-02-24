package com.example.chadyeo.updatedbakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chadyeo.updatedbakingapp.fragments.StepsListFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StepsListFragment stepsListFragment = new StepsListFragment();

        Bundle bundle = new Bundle();

        fragmentTransaction.add(R.id.detailActivity_container, stepsListFragment);
        fragmentTransaction.commit();

        stepsListFragment.setArguments(bundle);
    }
}
