package com.hussain.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hussain.popularmovies.model.Favorites;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Favorites>> loadAllMovies();

    @Insert
    void insertMovie(Favorites movies);

    @Query("SELECT * FROM movies WHERE id = :id AND isfavorite = 1")
    LiveData<Favorites> getFavorites(String id);

    @Query("DELETE FROM movies WHERE id = :id")
    void deleteMovie(String id);
}
