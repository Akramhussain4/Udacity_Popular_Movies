package com.hussain.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "movies")
public class Favorites {

    @PrimaryKey
    @NonNull
    private String id;
    private String poster;
    private String thumb;
    private String overview;
    private String rating;
    private String date;
    private String title;
    private boolean isfavorite;

    public Favorites(@NonNull String id, String poster, String thumb, String overview, String rating, String date, String title, boolean isfavorite) {
        this.id = id;
        this.poster = poster;
        this.thumb = thumb;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
        this.title = title;
        this.isfavorite = isfavorite;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = isfavorite;
    }

}
