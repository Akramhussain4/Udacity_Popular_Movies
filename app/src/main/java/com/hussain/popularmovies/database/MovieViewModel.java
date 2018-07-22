package com.hussain.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hussain.popularmovies.model.Favorites;

public class MovieViewModel extends ViewModel {

    private LiveData<Favorites> favorite;

    public MovieViewModel(AppDatabase database, String id){
        favorite = database.moviesDao().getFavorites(id);
    }

    public LiveData<Favorites> getFavorite() {
        return favorite;
    }
}
