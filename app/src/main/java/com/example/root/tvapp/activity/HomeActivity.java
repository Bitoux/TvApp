package com.example.root.tvapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.adapter.SerieAdapter;
import com.example.root.tvapp.interfaces.SeriesIdInterface;
import com.example.root.tvapp.interfaces.SeriesInterface;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;

import java.util.ArrayList;

/**
 * Created by root on 23/11/17.
 */

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ListView seriesView = (ListView) findViewById(R.id.update_series);

        final APIServices apiServices = new APIServices(getApplicationContext());
        apiServices.getUpdateSeries(new SeriesIdInterface() {
            @Override
            public void onSuccess(int[] seriesIDs) {
                final ArrayList<Serie> seriesList = new ArrayList<Serie>();
                for(int i = 0; i < 20 ; i++){
                    apiServices.getSeriesListView(seriesIDs[i], new SeriesInterface() {
                        @Override
                        public void onSuccess(Serie series) {
                            seriesList.add(series);
                            SerieAdapter adapter = new SerieAdapter(getApplicationContext(), seriesList);

                            seriesView.setAdapter(adapter);
                        }
                    });
                }
            }
        });

        seriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Serie serie = (Serie) adapterView.getItemAtPosition(i);
            }
        });

    }
}
