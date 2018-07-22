package com.hussain.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movies implements Parcelable {
    @SerializedName("poster_path")
    private String posterPath;
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


    private Movies(Parcel in) {
        posterPath = in.readString();
        id = in.readString();
        originalTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(id);
        dest.writeString(originalTitle);
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}