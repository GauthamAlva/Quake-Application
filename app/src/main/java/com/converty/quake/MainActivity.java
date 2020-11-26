package com.converty.quake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.converty.quake.QueryUtils.LOG_TAG;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<mag>> {
    public static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private masadapter adapter = null;
    private ListView list = null;
private TextView  mEmptyStateTextView;
private ProgressBar cyclicprogress;
    private TextView  connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG,"called oncreate() method");
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        boolean isConnected = info != null &&
                info.isConnectedOrConnecting();
        connection=(TextView)findViewById(R.id.internet);
        if(!isConnected){
            cyclicprogress = (ProgressBar) findViewById(R.id.progressBar_cyclic);
            cyclicprogress.setVisibility(View.GONE);
            connection.setText("No Internet Available");
        }
        else {
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            cyclicprogress = (ProgressBar) findViewById(R.id.progressBar_cyclic);
            list = (ListView) findViewById(R.id.list);

            list.setEmptyView(mEmptyStateTextView);
            adapter = new masadapter(this, new ArrayList<mag>());
            list.setAdapter(adapter);
            Log.i(LOG_TAG, "loader manager called...");
            LoaderManager.getInstance(this).initLoader(EARTHQUAKE_LOADER_ID, null, this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mag ur = adapter.getItem(i);
                    Intent openlink = new Intent(Intent.ACTION_VIEW, Uri.parse(ur.geturl()));
                    startActivity(openlink);
                }
            });
        }

    }

    @NonNull
    @Override
    public Loader<List<mag>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG,"oncreate loader running ...");
        return new Eartthquakeasync(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<mag>> loader, List<mag> data) {
        adapter.clear();
        Log.i(LOG_TAG,"onLoadFinished is called ..");
cyclicprogress.setVisibility(View.GONE);
        if (data != null && !data.isEmpty())
            adapter.addAll(data);
   else mEmptyStateTextView.setText("no_earthquakes");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<mag>> loader) {
        Log.i(LOG_TAG,"onLoaderReset is called ..");
        adapter.clear();
    }
}


