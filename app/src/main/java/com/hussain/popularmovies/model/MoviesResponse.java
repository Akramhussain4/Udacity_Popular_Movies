package com.hussain.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {


    @SerializedName("results")
    private List<Movies> results;

    public MoviesResponse(List results) {

        this.results = results;
    }

    public List getResults() {
        return results;
    }
}
