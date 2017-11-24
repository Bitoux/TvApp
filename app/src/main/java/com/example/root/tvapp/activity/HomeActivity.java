package com.example.root.tvapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.interfaces.SeriesIdInterface;
import com.example.root.tvapp.service.APIServices;

/**
 * Created by root on 23/11/17.
 */

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView seriesView = (ListView) findViewById(R.id.update_series);

        APIServices apiServices = new APIServices(getApplicationContext());
        apiServices.getUpdateSeries(new SeriesIdInterface() {
            @Override
            public void onSuccess(int[] seriesIDs) {

            }
        });

    }
}
