package com.hussain.popularmovies.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = "99a9c34b5be8877ef752d3f7d071f124";
    private static Retrofit retrofit = null;

    public static Retrofit buildUrl() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
