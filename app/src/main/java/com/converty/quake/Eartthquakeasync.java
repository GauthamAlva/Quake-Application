package com.converty.quake;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class Eartthquakeasync extends AsyncTaskLoader<List<mag>> {
    private  final String LOG_TAG = Eartthquakeasync.class.getName();
    private String mUrl;
    public Eartthquakeasync(Context context, String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<mag> loadInBackground() {
        Log.i(LOG_TAG,"loading in background ...");
        if(mUrl==null) return null;
        List<mag> quakelist=QueryUtils.fetchdata(mUrl);
        return quakelist;
    }
}
