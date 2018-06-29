package com.hussain.popularmovies.ui;

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
import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.utils.GlideApp;
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
    private int counter = 0;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        movieId = getIntent().getIntExtra("movieDetail", 0);
        Log.d(TAG, String.valueOf(movieId));
        Call<DetailsResponse> call = moviesInterface.getMovieDetails(movieId, BuildConfig.ApiKey);
        updateUI(call);
    }

    private void updateUI(Call call) {
        Bundle bundle = new Bundle();
        bundle.putInt("movieId", movieId);
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
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(getString(R.string.Image_Base_URL) + detailsResponse.getPoster_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.loading).into(mMovieThumb);
                String overView = detailsResponse.getOverview();
                mMovieOverview.setText(overView);
                mTitle.setText(detailsResponse.getTitle());
                mReleaseDate.setText(detailsResponse.getRealDate());
                mRating.setText(detailsResponse.getVote_average());
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @OnClick(R.id.movie_favorite_button)
    public void onclick() {
        if (counter == 0) {
            mFavButton.setImageResource(R.drawable.ic_favorite_full);
        }
        if (counter == 1) {
            mFavButton.setImageResource(R.drawable.ic_favorite_border);
            counter = -1;
        }
        counter++;
    }
}
