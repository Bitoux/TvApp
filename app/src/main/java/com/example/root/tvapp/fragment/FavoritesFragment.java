package com.example.root.tvapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.activity.SerieActivity;
import com.example.root.tvapp.adapter.SerieAdapter;
import com.example.root.tvapp.interfaces.ISeriesIdListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;

import java.util.ArrayList;

/**
 * Created by root on 11/01/18.
 */

public class FavoritesFragment extends Fragment {

    private static final String TAG = "MyActivity";

    public FavoritesFragment(){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        final ListView fabsView = (ListView) view.findViewById(R.id.favs_series);

        Log.d(TAG, "In Favorites");

        final APIServices apiServices = new APIServices(getActivity().getApplicationContext());

        apiServices.getUserFavorites(new ISeriesIdListener() {
            @Override
            public void onSuccess(int[] seriesIDs) {
                Log.d(TAG, "Got the favs");
                final ArrayList<Serie> seriesList = new ArrayList<Serie>();
                for(int i = 0; i < seriesIDs.length; i++){
                    Log.d(TAG, String.valueOf(seriesIDs[i]));
                    apiServices.getSerieById(seriesIDs[i], new ISeriesListener() {
                        @Override
                        public void onSuccess(Serie serie) {
                            System.out.println(serie.toString());
                            seriesList.add(serie);
                            SerieAdapter adapter = new SerieAdapter(getActivity().getApplicationContext(), seriesList);

                            fabsView.setAdapter(adapter);
                        }
                    });
                }

            }
        });

        fabsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Serie serie = (Serie) adapterView.getItemAtPosition(i);
                System.out.println(serie.toString());

                Intent intent =  new Intent(getActivity(), SerieActivity.class);
                intent.putExtra("serieId", serie.getId());
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
}
