package com.example.chadyeo.updatedbakingapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.chadyeo.updatedbakingapp.data.RecipeContract.RecipeEntry;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDB.db";

    private static final int VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECIPE_TABLE =
                "CREATE TABLE " + RecipeEntry.RECIPES_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.RECIPES_COLUMN_ID + " INTEGER NOT NULL, " +
                        RecipeEntry.RECIPES_COLUMN_NAME + " TEXT NOT NULL, " +
                        RecipeEntry.RECIPES_COLUMN_SERVINGS + " TEXT NOT NULL);";

        final String CREATE_INGREDIENT_TABLE =
                "CREATE TABLE " + RecipeEntry.INGREDIENTS_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.RECIPES_COLUMN_ID + " INTEGER NOT NULL, " +
                        RecipeEntry.INGREDIENTS_COLUMN_QUANTITY + " REAL NOT NULL, " +
                        RecipeEntry.INGREDIENTS_COLUMN_MEASURE + " TEXT NOT NULL, " +
                        RecipeEntry.INGREDIENTS_COLUMN_INGREDIENT + " TEXT NOT NULL);";

        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.RECIPES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.INGREDIENTS_TABLE_NAME);
        onCreate(db);
    }
}