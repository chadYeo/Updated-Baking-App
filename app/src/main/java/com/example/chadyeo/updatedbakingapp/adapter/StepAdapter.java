package com.example.chadyeo.updatedbakingapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.model.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private Context context;
    private ArrayList<Step> steps;

    public StepAdapter(Context context, ArrayList<Step> steps) {
        this.context = context;
        this.steps = steps;
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.steps_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.steps_short_desc_Name.setText(String.valueOf(step.getShortDescription()));
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
