package com.hussain.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.Movies;
import com.hussain.popularmovies.utils.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.RecyclerViewHolder> {

    private final List<Movies> movies;
    private final Context context;
    private final onMovieItemClickListener onMovieItemClickListener;

    public interface onMovieItemClickListener {
        void onMovieItemClick(int clickIndex);
    }

    public MoviesAdapter(List<Movies> movies, Context context, onMovieItemClickListener clickListener) {
        this.movies = movies;
        this.context = context;
        this.onMovieItemClickListener = clickListener;
        notifyDataSetChanged();
    }

    public List<Movies> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        holder.mTitle.setText(movies.get(position).getOriginalTitle());
        GlideApp.with(context)
                .load(context.getString(R.string.Image_Base_URL) + movies.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(GlidePalette.with(movies.get(position).getPosterPath())
                        .intoCallBack(palette -> {
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch != null) {
                                holder.mLinearLayout.setBackgroundColor(swatch.getRgb());
                                holder.mTitle.setTextColor(swatch.getBodyTextColor());
                                // holder.mFavButton.setColorFilter(swatch.getBodyTextColor(), PorterDuff.Mode.MULTIPLY);
                            }
                        })).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_thumbnail)
        ImageView mThumbnail;
        @BindView(R.id.movie_title)
        TextView mTitle;
        @BindView(R.id.movie_title_holder)
        LinearLayout mLinearLayout;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMovieItemClickListener.onMovieItemClick(getAdapterPosition());
        }
    }
}
