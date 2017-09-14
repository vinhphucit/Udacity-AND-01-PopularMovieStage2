package com.phuctran.popularmoviessecondstage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by phuctran on 9/12/17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 2;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITE_TALBE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_POPULARITY + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " DATETIME DEFAULT (datetime('now','localtime'))) ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TALBE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
