package com.phuctran.popularmoviessecondstage.networks.retrofit;

import com.phuctran.popularmoviessecondstage.models.MovieResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieReviewResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieTrailerResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by phuctran on 9/11/17.
 */

public interface RetrofitApi {
    @GET("/3/movie/{sort}")
    Call<MovieResponseWrapper> getMovies(@Path("sort") String sort);

    @GET("/3/movie/{id}/videos")
    Call<MovieTrailerResponseWrapper> getMovieTrailers(@Path("id") String id);

    @GET("/3/movie/{id}/reviews")
    Call<MovieReviewResponseWrapper> getMovieReviews(@Path("id") String id);
}
