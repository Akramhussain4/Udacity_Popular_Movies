package com.hussain.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hussain.popularmovies.BuildConfig;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.adapter.ReviewsAdapter;
import com.hussain.popularmovies.adapter.TrailerAdapter;
import com.hussain.popularmovies.model.ReviewsResponse;
import com.hussain.popularmovies.model.TrailerResponse;
import com.hussain.popularmovies.utils.MoviesInterface;
import com.hussain.popularmovies.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersFragment extends Fragment {

    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewRecycler;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView trailersRecycler;

    private int movieID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieID = this.getArguments().getInt("movieId");
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void updateUI() {
        MoviesInterface moviesInterface = NetworkUtils.buildUrl().create(MoviesInterface.class);
        Call<ReviewsResponse> reviewCall = moviesInterface.getReviews(movieID, BuildConfig.ApiKey);
        reviewCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                ReviewsResponse reviewsResponse = response.body();
                List<ReviewsResponse.Reviews> res = reviewsResponse.getReviews();
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), res);
                reviewRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                reviewRecycler.setAdapter(reviewsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call call, Throwable t) {

            }
        });

        Call<TrailerResponse> trailerCall = moviesInterface.getTrailers(movieID, BuildConfig.ApiKey);
        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                TrailerResponse trailerResponse = response.body();
                List<TrailerResponse.Trailers> trailers = trailerResponse.getTrailersList();
                TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(),trailers);
                trailersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                trailersRecycler.setAdapter(trailerAdapter);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

            }
        });
    }
}
