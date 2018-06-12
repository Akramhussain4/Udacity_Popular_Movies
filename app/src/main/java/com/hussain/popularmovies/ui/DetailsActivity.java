package com.hussain.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.DetailsResponse;
import com.hussain.popularmovies.utils.GlideApp;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView moverView = findViewById(R.id.movie_overview);
        TextView realDate = findViewById(R.id.release_date);
        TextView rating = findViewById(R.id.ratings);
        ImageView cover = findViewById(R.id.movie_cover);
        ImageView thumb = findViewById(R.id.thumb);
        ActionBar ab = getSupportActionBar();
        MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        Call<DetailsResponse> call = moviesInterface.getMovieDetails(getIntent().getIntExtra("id",0),NetworkUtils.API_KEY);
        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailsResponse> call, @NonNull Response<DetailsResponse> response) {
                DetailsResponse response1 = response.body();
                String movieTitle = response.body().getTitle();
                ab.setTitle(movieTitle);
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load("http://image.tmdb.org/t/p/w500/" + response.body().getBackdrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.colorAccent).into(cover);
                GlideApp.with(getApplicationContext())
                        .asBitmap()
                        .load("http://image.tmdb.org/t/p/w500/" + response.body().getPoster_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.colorAccent).into(thumb);
                String overView = response.body().getOverview();
               // moverView.setText(overView);
                realDate.setText(response1.getRealDate());
                rating.setText(response1.getVote_average());
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
                Log.d("----",t.toString());
               // title.setText(t.toString());
            }
        });
    }
}
