package com.hussain.popularmovies.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class ConnectivityService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
