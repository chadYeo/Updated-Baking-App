package com.example.chadyeo.updatedbakingapp.adapter;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.DetailActivity;
import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.fragments.StepsDetailFragment;
import com.example.chadyeo.updatedbakingapp.model.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    public ArrayList<Step> steps;

    public StepAdapter(ArrayList<Step> steps) {
        this.steps = steps;
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.steps_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {
        Step step = steps.get(position);
        holder.steps_short_desc_Name.setText(String.valueOf(step.getShortDescription()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity detailActivity = (DetailActivity)view.getContext();
                StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();

                detailActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailActivity_container, stepsDetailFragment)
                        .addToBackStack("Steps_List_TAG")
                        .commit();

                Bundle args= new Bundle();
                args.putInt("stepsPosition", position);
                stepsDetailFragment.setArguments(args);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (steps != null && !steps.isEmpty()) {
            return steps.size();
        } else {
            return 0;
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        TextView steps_short_desc_Name;

        public StepViewHolder(View itemView) {
            super(itemView);
            steps_short_desc_Name = (TextView) itemView.findViewById(R.id.steps_item_textView);
        }
    }
}
