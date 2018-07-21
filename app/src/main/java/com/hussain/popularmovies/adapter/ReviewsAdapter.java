package com.hussain.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.ReviewsResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ReviewsResponse.Reviews> reviewList;
    private final Context context;

    static class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author_name)
        TextView mAuthor;
        @BindView(R.id.review)
        TextView mReview;

        private ReviewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ReviewsAdapter(Context context, List<ReviewsResponse.Reviews> reviewList) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.layout_reviews, null);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final ReviewHolder reviewHolder = (ReviewHolder) holder;
        final ReviewsResponse.Reviews reviewModel = reviewList.get(position);
        reviewHolder.mAuthor.setText(reviewModel.getAuthor());
        reviewHolder.mReview.setText(reviewModel.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
