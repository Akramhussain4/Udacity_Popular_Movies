package com.hussain.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Movies {

    @SerializedName("poster_path")
    private final String posterPath;
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private final String originalTitle;


    public Movies(String posterPath, Integer id, String originalTitle) {
        this.posterPath = posterPath;
        this.id = id;
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

}