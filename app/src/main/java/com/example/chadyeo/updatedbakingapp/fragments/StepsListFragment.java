package com.example.chadyeo.updatedbakingapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.adapter.StepAdapter;
import com.example.chadyeo.updatedbakingapp.model.Ingredient;
import com.example.chadyeo.updatedbakingapp.model.Step;

import java.util.ArrayList;

public class StepsListFragment extends Fragment {

    private static final String LOG_TAG = StepsListFragment.class.getSimpleName();

    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    private NestedScrollView mNestedScrollView;
    private TextView mIngredientTextView;
    private RecyclerView mStepsRecyclerView;
    private StepAdapter mStepAdapter;

    private int scrollPositionX;
    private int scrollPositionY;

    public StepsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        mIngredientTextView = (TextView)view.findViewById(R.id.ingredients);
        mStepsRecyclerView = (RecyclerView)view.findViewById(R.id.detailView_steps_recyclerView);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNestedScrollView = (NestedScrollView)view.findViewById(R.id.nestedScrollView);
        if (mNestedScrollView != null) {
            mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollPositionX = scrollX;
                    scrollPositionY = scrollY;
                }
            });
        }

        ingredients = new ArrayList<>();
        ingredients = (ArrayList<Ingredient>)getActivity().getIntent().getExtras()
                .getSerializable("ingredients");

        insertIngredientsData(mIngredientTextView, ingredients);

        steps = new ArrayList<>();
        steps = (ArrayList<Step>)getActivity().getIntent().getExtras().getSerializable("steps");

        mStepsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mStepsRecyclerView.setLayoutManager(linearLayoutManager);
        mStepAdapter = new StepAdapter(steps);
        mStepsRecyclerView.setAdapter(mStepAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNestedScrollView.scrollTo(scrollPositionX, scrollPositionY);
    }

    private void insertIngredientsData(TextView ingredientTextView, ArrayList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            float qty;
            String measure;
            String actualIngredient;

            qty = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            actualIngredient = ingredient.getIngredient();

            String fullDesc = "  - " + String.valueOf(qty) + " " + measure + " of " + actualIngredient + "\n";

            mIngredientTextView.append(fullDesc);
        }
    }

}
