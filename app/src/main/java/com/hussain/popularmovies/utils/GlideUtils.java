package com.hussain.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;

public class GlideUtils {

    public static void getImage(Context context, ImageView imageView, @NonNull String imageUrl,
                                CircularProgressDrawable placeholderDrawable) {
        if (context == null || imageView == null) {
            return;
        }
        GlideApp.with(context)
                .load(imageUrl)
                .placeholder(placeholderDrawable)
                .into(imageView);
    }
}
