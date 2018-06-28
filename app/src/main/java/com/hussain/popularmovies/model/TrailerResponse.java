package com.hussain.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {
    @SerializedName("results")
    private List<Trailers> trailersList;

    public TrailerResponse(List<Trailers> trailersList) {
        this.trailersList = trailersList;
    }

    public List<Trailers> getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(List<Trailers> trailersList) {
        this.trailersList = trailersList;
    }

    public class Trailers {

        @SerializedName("name")
        private String name;
        @SerializedName("key")
        private String key;

        public Trailers(String name, String key) {
            this.name = name;
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
