package com.example.chadyeo.updatedbakingapp.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {
    public static final String AUTHORITY = "com.example.chadyeo.updatedbakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class RecipeEntry implements BaseColumns {

        public static Uri RECIPE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static Uri INGREDIENT_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static Uri STEPS_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        //recipes table info
        public static final String RECIPES_TABLE_NAME = "recipes";

        public static final String RECIPES_COLUMN_ID = "recipe_id";
        public static final String RECIPES_COLUMN_NAME = "name";
        public static final String RECIPES_COLUMN_SERVINGS = "servings";

        //ingredients table info
        public static final String INGREDIENTS_TABLE_NAME = "ingredients";

        public static final String INGREDIENTS_COLUMN_INGREDIENT = "ingredient";
        public static final String INGREDIENTS_COLUMN_QUANTITY = "quantity";
        public static final String INGREDIENTS_COLUMN_MEASURE = "measure";

        //steps table info
        public static final String STEPS_TABLE_NAME = "steps";

        public static final String STEPS_COLUMN_ID = "step_id";
        public static final String STEPS_COLUMN_SHORT_DESC = "short_desc";
        public static final String STEPS_COLUMN_DESC = "desc";
        public static final String STEPS_COLUMN_VIDEOURL = "videoURL";
        public static final String STEPS_COLUMN_THUMBNAILURL = "thumbnailURL";
    }
}
