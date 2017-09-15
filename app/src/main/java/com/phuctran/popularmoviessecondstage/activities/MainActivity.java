package com.phuctran.popularmoviessecondstage.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phuctran.popularmoviessecondstage.R;
import com.phuctran.popularmoviessecondstage.adapters.MovieAdapter;
import com.phuctran.popularmoviessecondstage.database.MovieContract;
import com.phuctran.popularmoviessecondstage.enums.MovieSortType;
import com.phuctran.popularmoviessecondstage.models.MovieModel;
import com.phuctran.popularmoviessecondstage.models.MovieResponseWrapper;
import com.phuctran.popularmoviessecondstage.networks.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<List<MovieModel>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    private static final String ON_SAVE_INSTANCE_STATE_TYPE = "ON_SAVE_INSTANCE_STATE_TYPE";
    private static final String ON_SAVE_INSTANCE_STATE_MOVIES = "ON_SAVE_INSTANCE_STATE_MOVIES";
    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;

    private MovieSortType mSortType = MovieSortType.MOST_POPULART;
    private MovieAdapter mMovieAdapter;
    private int mSpanCount = 2;
    private ArrayList<MovieModel> mMovies;

    @Override
    protected void updateFollowingViewBinding(Bundle savedInstanceState) {
        setupRecyclerView();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ON_SAVE_INSTANCE_STATE_TYPE)) {
                mSortType = (MovieSortType) savedInstanceState.getSerializable(ON_SAVE_INSTANCE_STATE_TYPE);
            }
            if (savedInstanceState.containsKey(ON_SAVE_INSTANCE_STATE_MOVIES)) {
                mMovies = savedInstanceState.getParcelableArrayList(ON_SAVE_INSTANCE_STATE_MOVIES);
            }
        }

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        if (mSortType == MovieSortType.FAVOURITE) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        } else {
            if (mMovies != null) {
                mMovieAdapter = new MovieAdapter(MainActivity.this, mMovies,
                        MainActivity.this);
                mRvMovies.setAdapter(mMovieAdapter);
            } else {
                getMovieData(mSortType);
            }
        }

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        switch (mSortType) {
            case FAVOURITE:
                menu.getItem(2).setChecked(true);
                break;
            case HIGHEST_RATED:
                menu.getItem(1).setChecked(true);
                break;
            case MOST_POPULART:
                menu.getItem(0).setChecked(true);
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ON_SAVE_INSTANCE_STATE_TYPE, mSortType);
        if (mMovies != null)
            outState.putParcelableArrayList(ON_SAVE_INSTANCE_STATE_MOVIES, mMovies);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(ON_SAVE_INSTANCE_STATE_TYPE, mSortType);
        if (mMovies != null)
            outState.putParcelableArrayList(ON_SAVE_INSTANCE_STATE_MOVIES, mMovies);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemThatWasSelected = item.getItemId();
        switch (menuItemThatWasSelected) {
            case R.id.action_most_popular:
                mSortType = MovieSortType.MOST_POPULART;
                getMovieData(MovieSortType.MOST_POPULART);
                item.setChecked(true);
                return true;
            case R.id.action_highest_rated:
                mSortType = MovieSortType.HIGHEST_RATED;
                getMovieData(MovieSortType.HIGHEST_RATED);
                item.setChecked(true);
                return true;
            case R.id.action_favourite:
                mSortType = MovieSortType.FAVOURITE;
                getFavouriteData(MovieSortType.FAVOURITE);
                item.setChecked(true);
                return true;
            default:
                return false;
        }

    }

    private void getFavouriteData(MovieSortType favourite) {
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSortType == MovieSortType.FAVOURITE) {
            getFavouriteData(mSortType);
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, mSpanCount);
        mRvMovies.setLayoutManager(layoutManager);
        mRvMovies.setHasFixedSize(true);


    }

    private void getMovieData(MovieSortType movieSortType) {
        RemoteDataSource.getInstance().getMovies(movieSortType.toString()).enqueue(new Callback<MovieResponseWrapper>() {
            @Override
            public void onResponse(Call<MovieResponseWrapper> call, Response<MovieResponseWrapper> response) {
                mMovies = response.body().getResults();
                mMovieAdapter = new MovieAdapter(MainActivity.this, mMovies,
                        MainActivity.this);
                mRvMovies.setAdapter(mMovieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponseWrapper> call, Throwable t) {

            }
        });
    }

    @Override
    public void onListItemClick(MovieModel movieModel) {
        Intent startChildActivityIntent = new Intent(this, DetailActivity.class);
        startChildActivityIntent.putExtra(DetailActivity.EXTRA_MOVIE_MODEL, movieModel);
        startActivity(startChildActivityIntent);
    }

    @Override
    public Loader<List<MovieModel>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<List<MovieModel>>(this) {
            List<MovieModel> mMovieData = null;

            @Override
            public List<MovieModel> loadInBackground() {
                List<MovieModel> movies = new ArrayList<>();
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " DESC");
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            MovieModel movieModel = new MovieModel(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ID)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POPULARITY)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)),
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH))
                            );
                            movies.add(movieModel);
                        } while (cursor.moveToNext());
                    }
                }
                return movies;
            }

            @Override
            public void deliverResult(List<MovieModel> data) {
                mMovieData = data;
                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<List<MovieModel>> loader, List<MovieModel> movieModels) {
        if (mSortType != MovieSortType.FAVOURITE) return;
        mMovieAdapter = new MovieAdapter(MainActivity.this, movieModels,
                MainActivity.this);
        mRvMovies.setAdapter(mMovieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieModel>> loader) {

    }
}
