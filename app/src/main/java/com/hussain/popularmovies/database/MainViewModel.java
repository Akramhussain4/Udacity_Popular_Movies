package com.hussain.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hussain.popularmovies.model.Favorites;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Favorites>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.moviesDao().loadAllMovies();
    }

    public LiveData<List<Favorites>> getFavorites() {
        return favorites;
    }
}
