package com.example.chadyeo.updatedbakingapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.chadyeo.updatedbakingapp.data.RecipeContract.RecipeEntry;

public class RecipeContentProvider extends ContentProvider {
    public static final int RECIPE = 100;
    public static final int INGREDIENTS = 101;
    public static final int STEPS = 102;

    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES, RECIPE);
        //uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGREDIENTS, INGREDIENTS);
        //uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEPS, STEPS);
        return uriMatcher;
    }

    private RecipeDbHelper mRecipeDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeDbHelper = new RecipeDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPE:
                long id = db.insert(RecipeEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case RECIPE:
                cursor = db.query(RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Curosr
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case RECIPE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
