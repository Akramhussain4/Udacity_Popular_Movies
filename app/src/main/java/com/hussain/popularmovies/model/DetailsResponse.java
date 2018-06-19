package com.hussain.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class DetailsResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("original_title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("vote_average")
    private String vote_average;
    @SerializedName("release_date")
    private String realDate;

    public DetailsResponse(String id, String adult, String title, String overview, String poster_path, String vote_average, String realDate) {
        this.id = id;
        this.backdrop = adult;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.realDate = realDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRealDate() {
        return realDate;
    }
}
