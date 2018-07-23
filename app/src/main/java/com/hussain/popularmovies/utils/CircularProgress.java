package com.hussain.popularmovies.utils;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;

public class CircularProgress {

    public static CircularProgressDrawable circularProgress(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(15f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}
