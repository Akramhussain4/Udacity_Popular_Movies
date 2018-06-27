package com.hussain.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.model.ReviewsResponse;
import com.hussain.popularmovies.utils.GlideApp;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.author_name)
    TextView author;
    @BindView(R.id.review)
    TextView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        int movieId = getIntent().getIntExtra("movieDetail", 0);
        Log.d(TAG, String.valueOf(movieId));
        Call<DetailsResponse> call = moviesInterface.getMovieDetails(movieId, BuildConfig.ApiKey);
        Call<ReviewsResponse> reviewCall = moviesInterface.getReviews(movieId,BuildConfig.ApiKey);
        updateUI(call,reviewCall);
    }

    public void updateUI(Call call, Call reviewCall){
        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailsResponse> call, @NonNull Response<DetailsResponse> response) {
                DetailsResponse response1 = response.body();
                String movieTitle = response1.getTitle();
                ActionBar ab = getSupportActionBar();
                ab.setTitle(movieTitle);
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(getString(R.string.Image_Base_URL) + response1.getBackdrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.loading).into(mMovieCover);
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load(getString(R.string.Image_Base_URL) + response1.getPoster_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.loading).into(mMovieThumb);
                String overView = response1.getOverview();
                mMovieOverview.setText(overView);
                mReleaseDate.setText(response1.getRealDate());
                mRating.setText(response1.getVote_average());
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

        reviewCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call <ReviewsResponse> call, Response<ReviewsResponse> response) {
                ReviewsResponse reviewsResponse = response.body();
                List<ReviewsResponse.Reviews> res = reviewsResponse.getReviews();
                for(ReviewsResponse.Reviews reviews : res ) {
                    author.setText("Author:" + reviews.getAuthor());
                    review.setText(reviews.getContent());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void onFavClick(View view) {

    }
}
