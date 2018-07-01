package com.hussain.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hussain.popularmovies.model.Movies;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    List<Movies> loadAllMovies();

    @Insert
    void insertMovie(Movies movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movies movies);

    @Delete
    void deleteMovie(Movies movies);
}
