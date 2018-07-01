package com.hussain.popularmovies.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.adapter.MoviesAdapter;
import com.hussain.popularmovies.database.AppDatabase;
import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.model.Movies;
import com.hussain.popularmovies.utils.GlideApp;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getName();

    @BindView(R.id.movie_overview)
    TextView mMovieOverview;
    @BindView(R.id.ratings)
    TextView mRating;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.movie_cover)
    ImageView mMovieCover;
    @BindView(R.id.movie_thumb)
    ImageView mMovieThumb;
    @BindView(R.id.movie_favorite_button)
    ImageButton mFavButton;
    @BindView(R.id.title_text)
    TextView mTitle;
    private int counter = 0;
    private String movieId;
    private AppDatabase mDb;
    private Movies detailsResponse1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        movieId = getIntent().getStringExtra("movieDetail");
        mDb = AppDatabase.getInstance(getApplicationContext());
        Log.d(TAG, String.valueOf(movieId));
        Call<DetailsResponse> call = moviesInterface.getMovieDetails(Long.parseLong(movieId), BuildConfig.ApiKey);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int counter = sharedPref.getInt("favoriteSelected",1);
        String movId = sharedPref.getString("movieId",null);
        if (counter == 0 && movId.equals(movieId)) {
            handleFavorite();
        }
        updateUI(call);
    }

    private void updateUI(Call call) {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", movieId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TrailersFragment trailersFragment = new TrailersFragment();
        trailersFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.trailerContainer, trailersFragment);
        fragmentTransaction.commit();

        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailsResponse> call, @NonNull Response<DetailsResponse> response) {
                DetailsResponse detailsResponse = response.body();
                if (detailsResponse != null) {
                    GlideApp.with(getApplicationContext())
                            .asBitmap()
                            .load(getString(R.string.Image_Base_URL) + detailsResponse.getBackdrop())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.loading).into(mMovieCover);
                }
                if (detailsResponse != null) {
                    GlideApp.with(getApplicationContext())
                            .asBitmap()
                            .load(getString(R.string.Image_Base_URL) + detailsResponse.getPoster_path())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.loading).into(mMovieThumb);
                }
                String overView = detailsResponse.getOverview();
                mMovieOverview.setText(overView);
                mTitle.setText(detailsResponse.getTitle());
                mReleaseDate.setText(detailsResponse.getRealDate());
                mRating.setText(detailsResponse.getVote_average());
                detailsResponse1 = new Movies(detailsResponse.getId(),
                        detailsResponse.getPoster_path(), detailsResponse.getTitle());
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public void handleFavorite() {
        mFavButton.setImageResource(R.drawable.ic_favorite_full);
    }

    public void handleUnFavorite() {
        MoviesAdapter moviesAdapter = new MoviesAdapter();
        List<Movies> movies = moviesAdapter.getMovies();
        //mDb.moviesDao().deleteMovie(movies.get(Integer.parseInt(movieId)));
        mFavButton.setImageResource(R.drawable.ic_favorite_border);
        counter = -1;
    }

    @OnClick(R.id.movie_favorite_button)
    public void onclick() {
        if (counter == 0) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("favoriteSelected", counter);
            editor.putString("movieId",movieId);
            editor.apply();
            mDb.moviesDao().insertMovie(detailsResponse1);
            handleFavorite();
        }
        if (counter == 1) {
            handleUnFavorite();
        }
        counter++;
    }
}
