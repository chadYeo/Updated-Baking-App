package com.example.chadyeo.updatedbakingapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chadyeo.updatedbakingapp.DetailActivity;
import com.example.chadyeo.updatedbakingapp.MainActivity;
import com.example.chadyeo.updatedbakingapp.R;
import com.example.chadyeo.updatedbakingapp.model.Ingredient;
import com.example.chadyeo.updatedbakingapp.model.Recipe;
import com.example.chadyeo.updatedbakingapp.model.Step;
import com.example.chadyeo.updatedbakingapp.widgets.RecipeService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
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
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        String recipeName = recipe.getName();
        int recipeServings = recipe.getServings();
        String image = recipe.getImage();

        if (!image.isEmpty()) {
            Picasso.get().load(image).fit().into(holder.mRecipeImageView);
        }
        holder.mNameTextView.setText(recipeName);
        holder.mServingTextView.setText(String.valueOf(recipeServings));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, DetailActivity.class);
                if (recipe.getIngredients() != null && recipe.getSteps() != null) {
                    ArrayList<Ingredient> ingredients = recipe.getIngredients();
                    ArrayList<Step> steps = recipe.getSteps();

                    detailIntent.putExtra("steps", steps);
                    detailIntent.putExtra("ingredients", ingredients);
                    detailIntent.putExtra("name", recipe.getName());
                }
                context.startActivity(detailIntent);

                // SharedPreferences to show last viewed recipe ingredients in Widget
                SharedPreferences sharedPreferences = context.getSharedPreferences("LAST_VIEWED_RECIPE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("LAST_VIEWED_RECIPE", holder.getAdapterPosition());
                editor.apply();
                RecipeService.startActionRecipe(context);

                Log.e(LOG_TAG, "Clicked Position from the Recipe List for Widget: " + String.valueOf(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView mRecipeImageView;
        private TextView mNameTextView;
        private TextView mServingTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImageView = (ImageView)itemView.findViewById(R.id.dessert_imageView);
            mNameTextView = (TextView)itemView.findViewById(R.id.name);
            mServingTextView = (TextView)itemView.findViewById(R.id.serving);
        }
    }
}
