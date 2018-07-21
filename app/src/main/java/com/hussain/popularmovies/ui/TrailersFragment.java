package com.hussain.popularmovies.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class TrailersFragment extends Fragment implements TrailerAdapter.OnTrailerClickListener {

    private static final String TAG = TrailersFragment.class.getName();

    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewRecycler;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView trailersRecycler;
    private Context mContext;
    private int movieID;
    private List<TrailerResponse.Trailers> trailers;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieID = Integer.parseInt(this.getArguments().getString("movieId"));
        mContext = getContext();
        setHasOptionsMenu(true);
        checkNetwork();
    }

    private void checkNetwork() {
        if (NetworkUtils.isNetworkAvailable(getContext())) {
            updateUI();
        } else {
            getActivity().getFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(mContext, res);
                reviewRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                reviewRecycler.setAdapter(reviewsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

        Call<TrailerResponse> trailerCall = moviesInterface.getTrailers(movieID, BuildConfig.ApiKey);
        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                TrailerResponse trailerResponse = response.body();
                if (trailerResponse != null) {
                    trailers = trailerResponse.getTrailersList();
                    TrailerAdapter trailerAdapter = new TrailerAdapter(mContext, trailers, TrailersFragment.this);
                    trailersRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                    trailersRecycler.setAdapter(trailerAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.checkout_movie)
                    + getString(R.string.youtube_url) + trailers.get(position).getKey());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClick(int position) {
        this.position = position;
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(position).getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.youtube_url) + trailers.get(position).getKey()));
        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }
}
