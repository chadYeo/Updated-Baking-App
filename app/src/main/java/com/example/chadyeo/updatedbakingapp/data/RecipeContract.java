package com.example.chadyeo.updatedbakingapp.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {
    public static final String AUTHORITY = "com.example.chadyeo.bakingapp2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class RecipeEntry implements BaseColumns {

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
    }

    public static final class IngredientsEntry implements BaseColumns {

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
    }

    public static final class StepsEntry implements BaseColumns {

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_VIDEOURL = "videoURL";
    }
}
