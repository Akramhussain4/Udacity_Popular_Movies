package com.hussain.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.TrailerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<TrailerResponse.Trailers> trailersList;
    private final Context context;

    static class TrailerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_title)
        TextView mTrailerTitle;

        private TrailerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TrailerAdapter(Context context, List<TrailerResponse.Trailers> trailersList) {
        this.trailersList = trailersList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.layout_trailer, null);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TrailerHolder trailerHolder = (TrailerHolder) holder;
        final TrailerResponse.Trailers trailers = trailersList.get(position);
        trailerHolder.mTrailerTitle.setText(trailers.getName());
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }
}
