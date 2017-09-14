package com.phuctran.popularmoviessecondstage.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by phuctran on 9/12/17.
 */

public class MovieContract {
    public static String AUTHORITY = "com.phuctran.popularmoviessecondstage";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static String PATH_MOVIES = "movies";

    public static class MovieEntry {
        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_TIMESTAMP = "insert_timestamp";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
