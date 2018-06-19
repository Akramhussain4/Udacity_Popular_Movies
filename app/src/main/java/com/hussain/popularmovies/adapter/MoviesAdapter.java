package com.hussain.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hussain.popularmovies.model.Movies;
import com.hussain.popularmovies.R;
import com.hussain.popularmovies.utils.GlideApp;
import com.hussain.popularmovies.ui.DetailsActivity;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.RecyclerViewHolder> {

    private List<Movies> movies;
    private Context context;

    public MoviesAdapter(List<Movies> movies, Context context) {
        this.movies = movies;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        holder.title.setText(movies.get(position).getOriginalTitle());
        GlideApp.with(context)
                .asBitmap()
                .load(context.getString(R.string.Image_Base_URL) + movies.get(position).getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            Palette p = Palette.from(resource).generate();
                            Palette.Swatch swatch = p.getVibrantSwatch();
                            if (swatch != null) {
                                holder.constraintLayout.setBackgroundColor(swatch.getRgb());
                                holder.title.setTextColor(swatch.getBodyTextColor());
                            }
                        }
                        return false;
                    }
                })
                .into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView thumb;
        private TextView title;
        private LinearLayout constraintLayout;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.movie_thumbnail);
            title = itemView.findViewById(R.id.movie_title);
            constraintLayout = itemView.findViewById(R.id.movie_title_holder);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra("id", movies.get(getAdapterPosition()).getId());
            view.getContext().startActivity(intent);
        }
    }
}
