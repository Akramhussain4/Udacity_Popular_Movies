package com.hussain.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hussain.popularmovies.model.Favorites;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    List<Favorites> loadAllMovies();

    @Insert
    void insertMovie(Favorites movies);

    @Query("SELECT * FROM movies WHERE id = :id AND isfavorite = 1")
    List<Favorites> getFavorites(String id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Favorites movies);

    @Query("DELETE FROM movies WHERE id = :id")
    void deleteMovie(String id);
}
