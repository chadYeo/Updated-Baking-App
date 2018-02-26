package com.example.chadyeo.updatedbakingapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.chadyeo.updatedbakingapp.data.RecipeContract.RecipeEntry;
import com.example.chadyeo.updatedbakingapp.data.RecipeContract.IngredientsEntry;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDB.db";

    private static final int VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECIPE_TABLE =
                "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        RecipeEntry.COLUMN_SERVINGS + " TEXT NOT NULL);";

        final String CREATE_INGREDIENT_TABLE =
                "CREATE TABLE " + IngredientsEntry.TABLE_NAME + " (" +
                        IngredientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry._ID + " INTEGER NOT NULL, " +
                        IngredientsEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                        IngredientsEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                        IngredientsEntry.COLUMN_INGREDIENT + " TEXT NOT NULL);";

        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientsEntry.TABLE_NAME);
        onCreate(db);
    }
}