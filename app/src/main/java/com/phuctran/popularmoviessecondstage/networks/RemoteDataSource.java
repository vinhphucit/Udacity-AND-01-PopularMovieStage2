package com.phuctran.popularmoviessecondstage.networks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phuctran.popularmoviessecondstage.BuildConfig;
import com.phuctran.popularmoviessecondstage.models.MovieResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieReviewResponseWrapper;
import com.phuctran.popularmoviessecondstage.models.MovieTrailerResponseWrapper;
import com.phuctran.popularmoviessecondstage.networks.retrofit.RetrofitApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phuctran on 9/11/17.
 */

public class RemoteDataSource {

    private static RemoteDataSource INSTANCE;
    private final RetrofitApi mApi;

    public RemoteDataSource() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        Interceptor apiInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                addInterceptor(logging).
                addInterceptor(apiInterceptor).build();

//        client.networkInterceptors().add(chain -> {
//            if (UserManager.getInstance().getToken() != null && !UserManager.getInstance().getToken().equals("")) {
//                Request request = chain.request().newBuilder().addHeader("Token", UserManager.getInstance().getToken()).build();
//                return chain.proceed(request);
//            }
//            Request request = chain.request().newBuilder().build();
//            return chain.proceed(request);
//        });

        Gson customGsonInstance = new GsonBuilder()
//                .registerTypeAdapter(new TypeToken<List<MarvelCharacter>>() {}.getType(),
//                        new MarvelResultsDeserializer<MarvelCharacter>())
//
//                .registerTypeAdapter(new TypeToken<List<CollectionItem>>() {}.getType(),
//                        new MarvelResultsDeserializer<CollectionItem>())

                .create();

        Retrofit retrofitApiAdapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
                .client(client)
                .build();

        mApi = retrofitApiAdapter.create(RetrofitApi.class);
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    public Call<MovieResponseWrapper> getMovies(String sortType) {
        return mApi.getMovies(sortType);
    }

    public Call<MovieTrailerResponseWrapper> getTrailers(String id) {
        return mApi.getMovieTrailers(id);
    }

    public Call<MovieReviewResponseWrapper> getReviews(String id) {
        return mApi.getMovieReviews(id);
    }
}
