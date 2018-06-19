package com.hussain.popularmovies.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.model.Movies;
import com.hussain.popularmovies.model.MoviesResponse;
import com.hussain.popularmovies.adapter.MoviesAdapter;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesInterface moviesInterface;
    private int selectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpan());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        if (savedInstanceState != null) {
            sortMovies(savedInstanceState.getInt("orderSelected",selectedOrder));
        }
            Call<MoviesResponse> call = moviesInterface.getPopularMovies(BuildConfig.ApiKey);
            getMovies(call);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("orderSelected", selectedOrder);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private int getSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 4;
        }
        return 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sortMovies(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void sortMovies(int orderSelected) {
        this.selectedOrder = orderSelected;
        switch (orderSelected) {
            case R.id.top_rated:
                Call<MoviesResponse> getTopRatedMovies = moviesInterface.getTopRatedMovies(BuildConfig.ApiKey);
                getMovies(getTopRatedMovies);
                break;
            case R.id.popular:
                Call<MoviesResponse> getPopularMovies = moviesInterface.getPopularMovies(BuildConfig.ApiKey);
                getMovies(getPopularMovies);
                break;
        }
    }

    public void getMovies(Call call) {
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                List<Movies> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, getApplicationContext()));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
