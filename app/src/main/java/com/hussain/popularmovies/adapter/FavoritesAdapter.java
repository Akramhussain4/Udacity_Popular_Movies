package com.hussain.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.glidepalette.GlidePalette;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.model.Favorites;
import com.hussain.popularmovies.ui.DetailsActivity;
import com.hussain.popularmovies.utils.CircularProgress;
import com.hussain.popularmovies.utils.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.RecyclerViewHolder> {
    private List<Favorites> movies;
    private Context context;

    public FavoritesAdapter(List<Favorites> movies, Context context) {
        this.movies = movies;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritesAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        return new FavoritesAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoritesAdapter.RecyclerViewHolder holder, int position) {
        CircularProgressDrawable cp = CircularProgress.circularProgress(context);
        holder.mTitle.setText(movies.get(position).getTitle());
        GlideApp.with(context)
                .load(movies.get(position).getThumb())
                .placeholder(cp)
                .listener(GlidePalette.with(movies.get(position).getThumb())
                        .intoCallBack(palette -> {
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch != null) {
                                holder.mLinearLayout.setBackgroundColor(swatch.getRgb());
                                holder.mTitle.setTextColor(swatch.getBodyTextColor());
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
            Context context = view.getContext();
            int position = getAdapterPosition();
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("movieDetail", movies.get(position).getId());
            context.startActivity(intent);
        }
    }
}
