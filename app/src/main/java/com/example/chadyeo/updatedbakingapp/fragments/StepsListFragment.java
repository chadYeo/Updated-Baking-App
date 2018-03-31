package com.example.chadyeo.updatedbakingapp.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private TextView mIngredientTextView;
    private RecyclerView mStepsRecyclerView;
    private StepAdapter mStepAdapter;
    private int currentVisiblePosition;

    public StepsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        mIngredientTextView = (TextView)view.findViewById(R.id.ingredients);
        mStepsRecyclerView = (RecyclerView)view.findViewById(R.id.detailView_steps_recyclerView);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        ((LinearLayoutManager) mStepsRecyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = 0;
        currentVisiblePosition =
                ((LinearLayoutManager)mStepsRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
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
