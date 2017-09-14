package com.phuctran.popularmoviessecondstage.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.phuctran.popularmoviessecondstage.database.MovieContract;
import com.phuctran.popularmoviessecondstage.database.MovieDatabaseHelper;

/**
 * Created by phuctran on 9/12/17.
 */

public class MovieContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {

            case MOVIES: {

                return mDb.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            }
            case MOVIE_WITH_ID: {
                String id = uri.getPathSegments().get(1);

                String mSelection = MovieContract.MovieEntry.COLUMN_NAME_ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                return mDb.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID: {
                SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
                long id = mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID: {
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                String id = uri.getPathSegments().get(1);

                String mSelection = MovieContract.MovieEntry.COLUMN_NAME_ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                int numRowDeleted = mDb.delete(MovieContract.MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                if (numRowDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numRowDeleted;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/*", MOVIE_WITH_ID);
        return uriMatcher;
    }

}
