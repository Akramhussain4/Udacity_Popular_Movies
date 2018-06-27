package com.hussain.popularmovies.utils;

import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.model.MoviesResponse;
import com.hussain.popularmovies.model.ReviewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);


    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<DetailsResponse> getMovieDetails(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") long id, @Query("api_key") String apiKey);
}