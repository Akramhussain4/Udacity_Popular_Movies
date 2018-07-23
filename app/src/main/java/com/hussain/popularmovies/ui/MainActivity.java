package com.hussain.popularmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.adapter.FavoritesAdapter;
import com.hussain.popularmovies.adapter.MoviesAdapter;
import com.hussain.popularmovies.database.MainViewModel;
import com.hussain.popularmovies.model.Movies;
import com.hussain.popularmovies.model.MoviesResponse;
import com.hussain.popularmovies.utils.GridAutofitLayoutManager;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onMovieItemClickListener {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private MoviesInterface moviesInterface;
    private MoviesAdapter mAdapter;
    private ArrayList<Movies> movies;
    @BindView(R.id.textView4)
    TextView noFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(this, 460);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        setMovies(savedInstanceState);
    }

    private void setMovies(Bundle savedInstanceState) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (savedInstanceState == null) {
                sortMovies(R.id.popular);
            } else {
                movies = savedInstanceState.getParcelableArrayList("moviesList");
                mAdapter = new MoviesAdapter(movies, getApplication(), MainActivity.this);
                recyclerView.setAdapter(mAdapter);
            }
        } else {
            sortMovies(R.id.favorite);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("scrollPos"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("moviesList", movies);
        outState.putParcelable("scrollPos",recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
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
        switch (orderSelected) {
            case R.id.top_rated:
                Call<MoviesResponse> getTopRatedMovies = moviesInterface.getTopRatedMovies(BuildConfig.ApiKey);
                getMovies(getTopRatedMovies);
                noFav.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.popular:
                Call<MoviesResponse> getPopularMovies = moviesInterface.getPopularMovies(BuildConfig.ApiKey);
                getMovies(getPopularMovies);
                noFav.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.favorite:
                MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
                viewModel.getFavorites().observe(this, favorites1 -> {
                    if(favorites1.size()!=0) {
                        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites1, MainActivity.this);
                        recyclerView.setAdapter(favoritesAdapter);
                    }
                    else {
                        noFav.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                });
                break;
        }
    }

    private void getMovies(Call call) {
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                movies = response.body().getResults();
                mAdapter = new MoviesAdapter(movies, getApplication(), MainActivity.this);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    public void onMovieItemClick(int clickIndex) {
        List<Movies> movies = mAdapter.getMovies();
        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        intent.putExtra("movieDetail", movies.get(clickIndex).getId());
        startActivity(intent);
    }
}
