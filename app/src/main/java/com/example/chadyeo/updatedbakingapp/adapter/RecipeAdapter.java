package com.example.chadyeo.updatedbakingapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        String recipeName = recipe.getName();
        int recipeServings = recipe.getServings();

        holder.mNameTextView.setText(recipeName);
        holder.mServingTextView.setText(String.valueOf(recipeServings));
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mServingTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView)itemView.findViewById(R.id.name);
            mServingTextView = (TextView)itemView.findViewById(R.id.serving);
        }
    }
}
