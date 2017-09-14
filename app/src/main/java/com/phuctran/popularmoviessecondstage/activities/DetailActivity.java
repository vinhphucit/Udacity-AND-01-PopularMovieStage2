package com.phuctran.popularmoviessecondstage.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.phuctran.popularmoviessecondstage.BuildConfig;
import com.phuctran.popularmoviessecondstage.R;
import com.phuctran.popularmoviessecondstage.adapters.MovieAdapter;
import com.phuctran.popularmoviessecondstage.adapters.ReviewAdapter;
import com.phuctran.popularmoviessecondstage.adapters.TrailerAdapter;
import com.phuctran.popularmoviessecondstage.database.MovieContract;
import com.phuctran.popularmoviessecondstage.models.MovieModel;
import com.phuctran.popularmoviessecondstage.models.MovieResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieReviewResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieTrailerResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.ReviewModel;
import com.phuctran.popularmoviessecondstage.models.TrailerModel;
import com.phuctran.popularmoviessecondstage.networks.RemoteDataSource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;
import static com.phuctran.popularmoviessecondstage.database.MovieContract.MovieEntry.CONTENT_URI;
import static com.phuctran.popularmoviessecondstage.database.MovieContract.MovieEntry.buildMovieUri;

/**
 * Created by phuctran on 9/9/17.
 */

public class DetailActivity extends BaseActivity implements TrailerAdapter.TrailerListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_MODEL = "EXTRA_MOVIE_MODEL";
    private static final int MOVIE_LOADER_ID = 1;
    @BindView(R.id.btnFavourite)
    FloatingActionButton btnFavourite;
    @BindView(R.id.ivDetailThumbnail)
    ImageView ivDetailThumbnail;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @BindView(R.id.tvVoteAverage)
    TextView tvVoteAverage;
    @BindView(R.id.rvTrailer)
    RecyclerView rvTrailer;
    @BindView(R.id.rvReview)
    RecyclerView rvReview;
    private MovieModel mMovieModel;
    private boolean isFavourite = false;
    private TrailerAdapter mMovieAdapter;
    private List<TrailerModel> mMovies = new ArrayList<>();
    private ReviewAdapter mReviewAdapter;
    private List<ReviewModel> mReviews = new ArrayList<>();

    @Override
    protected void updateFollowingViewBinding(Bundle savedInstanceState) {
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE_MODEL)) {
            mMovieModel = intentThatStartedThisActivity.getParcelableExtra(EXTRA_MOVIE_MODEL);
        }

        tvTitle.setText(mMovieModel.getTitle());
        tvOverview.setText(mMovieModel.getOverview());
        tvReleaseDate.setText(mMovieModel.getRelease_date());
        tvVoteAverage.setText(mMovieModel.getVote_average());

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + mMovieModel.getBackdrop_path()).into(ivDetailThumbnail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mMovieModel.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupRecyclerView();
        getTrailers();
        getReviews();

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTrailer.setLayoutManager(layoutManager);
        rvTrailer.setHasFixedSize(true);
        LinearLayoutManager rvlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReview.setLayoutManager(rvlayoutManager);
        rvReview.setHasFixedSize(true);

    }

    private void getTrailers() {
        RemoteDataSource.getInstance().getTrailers(mMovieModel.getId()).enqueue(new Callback<MovieTrailerResponseWrapper>() {
            @Override
            public void onResponse(Call<MovieTrailerResponseWrapper> call, Response<MovieTrailerResponseWrapper> response) {
                mMovieAdapter = new TrailerAdapter(DetailActivity.this, response.body().getResults(),
                        DetailActivity.this);
                rvTrailer.setAdapter(mMovieAdapter);
            }

            @Override
            public void onFailure(Call<MovieTrailerResponseWrapper> call, Throwable t) {

            }
        });
    }

    private void getReviews() {
        RemoteDataSource.getInstance().getReviews(mMovieModel.getId()).enqueue(new Callback<MovieReviewResponseWrapper>() {
            @Override
            public void onResponse(Call<MovieReviewResponseWrapper> call, Response<MovieReviewResponseWrapper> response) {
                mReviewAdapter = new ReviewAdapter(DetailActivity.this, response.body().getResults());
                rvReview.setAdapter(mReviewAdapter);
            }

            @Override
            public void onFailure(Call<MovieReviewResponseWrapper> call, Throwable t) {

            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerListItemClick(TrailerModel movieModel) {
        String url = String.format(BuildConfig.YOUTUBE_WATCH, movieModel.getKey());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.btnFavourite)
    void onFavouriteClick() {
        if (!isFavourite) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_ID, mMovieModel.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, mMovieModel.getBackdrop_path());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, mMovieModel.getOriginal_title());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, mMovieModel.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POPULARITY, mMovieModel.getPopularity());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, mMovieModel.getPoster_path());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, mMovieModel.getRelease_date());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, mMovieModel.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, mMovieModel.getVote_average());
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, mMovieModel.getVote_count());

            Uri uri = getContentResolver().insert(buildMovieUri(Long.parseLong(mMovieModel.getId())), contentValues);
        } else {
            Uri uri = CONTENT_URI;
            uri = buildMovieUri(Long.parseLong(mMovieModel.getId()));
            getContentResolver().delete(uri, null, null);
        }

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private void setFavouriteButton() {
        if (isFavourite) {
            btnFavourite.setImageResource(btn_star_big_on);
        } else {
            btnFavourite.setImageResource(btn_star_big_off);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            public Cursor loadInBackground() {

                Uri uri = ContentUris.withAppendedId(CONTENT_URI, Long.parseLong(mMovieModel.getId()));

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                if (cursor.getCount() > 0) {
                    isFavourite = true;
                } else {
                    isFavourite = false;
                }

                return null;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        setFavouriteButton();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}
