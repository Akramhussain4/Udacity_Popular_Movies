package com.hussain.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class Movies {
    @SerializedName("poster_path")
    private String posterPath;
    @PrimaryKey @NonNull
    @SerializedName("id")
    private String id;
    @SerializedName("original_title")
    private String originalTitle;

    public Movies(String posterPath, String id, String originalTitle) {
        this.posterPath = posterPath;
        this.id = id;
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

}