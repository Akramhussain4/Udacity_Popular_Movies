package com.hussain.popularmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.database.AppDatabase;
import com.hussain.popularmovies.database.MovieViewModel;
import com.hussain.popularmovies.database.ViewModelFactory;
import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.model.Favorites;
import com.hussain.popularmovies.utils.AppExecutors;
import com.hussain.popularmovies.utils.GlideUtils;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

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
    private String movieId, Thumbnail, Cover, Overview, Rating, Date, Title;
    private AppDatabase mDb;
    private boolean isFav = false;
    private CircularProgressDrawable circularProgressDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());
        movieId = getIntent().getStringExtra("movieDetail");
        circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(15f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();
        setDetails();
    }

    public void setDetails(){
        if(NetworkUtils.isNetworkAvailable(this)){
            MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
            Call<DetailsResponse> call = moviesInterface.getMovieDetails(Long.parseLong(movieId), BuildConfig.ApiKey);
            updateUI(call);
        }
        else {
            setFavorite();
        }
    }


    private void setFavorite() {
        ViewModelFactory factory = new ViewModelFactory(mDb, movieId);
        final MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        viewModel.getFavorite().observe(this, favorites -> {
            if (favorites.size() > 0) {
                mFavButton.setImageResource(R.drawable.ic_favorite_full);
                GlideUtils.getImage(getApplicationContext(), mMovieCover, favorites.get(0).getPoster(), circularProgressDrawable);
                GlideUtils.getImage(getApplicationContext(), mMovieThumb, favorites.get(0).getThumb(), circularProgressDrawable);
                mMovieOverview.setText(favorites.get(0).getOverview());
                mTitle.setText(favorites.get(0).getTitle());
                mReleaseDate.setText(favorites.get(0).getDate());
                mRating.setText(favorites.get(0).getRating());
                isFav = true;
            }
        });
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
                    Thumbnail = getString(R.string.Image_Base_URL) + detailsResponse.getBackdrop();
                    Cover = getString(R.string.Image_Base_URL) + detailsResponse.getPoster_path();
                    Overview = detailsResponse.getOverview();
                    Title = detailsResponse.getTitle();
                    Date = detailsResponse.getRealDate();
                    Rating = detailsResponse.getVote_average();
                    GlideUtils.getImage(getApplicationContext(), mMovieCover, Thumbnail, circularProgressDrawable);
                    GlideUtils.getImage(getApplicationContext(), mMovieThumb, Cover, circularProgressDrawable);
                    mMovieOverview.setText(Overview);
                    mTitle.setText(Title);
                    mReleaseDate.setText(Date);
                    mRating.setText(Rating);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public void handleFavorite() {
        mFavButton.setImageResource(R.drawable.ic_favorite_full);
        Favorites favorites = new Favorites(movieId, Thumbnail, Cover, Overview, Rating, Date, Title, true);
        AppExecutors.getInstance().getDiskIO().execute(() -> mDb.moviesDao().insertMovie(favorites));

    }

    public void handleUnFavorite() {
        mFavButton.setImageResource(R.drawable.ic_favorite_border);
        AppExecutors.getInstance().getDiskIO().execute(() -> mDb.moviesDao().deleteMovie(movieId));
    }

    @OnClick(R.id.movie_favorite_button)
    public void onclick() {
        if (isFav) {
            handleUnFavorite();
        } else {
            handleFavorite();
        }
    }
}
