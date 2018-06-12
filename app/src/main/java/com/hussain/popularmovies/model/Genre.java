package com.hussain.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String genreName;

    public Genre(String id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
