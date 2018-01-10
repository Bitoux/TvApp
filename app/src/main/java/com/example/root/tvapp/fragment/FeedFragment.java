package com.example.root.tvapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.activity.HomeActivity;
import com.example.root.tvapp.activity.SerieActivity;
import com.example.root.tvapp.adapter.SerieAdapter;
import com.example.root.tvapp.interfaces.ISeriesIdListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;

import java.util.ArrayList;

public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        final ListView seriesView = (ListView) view.findViewById(R.id.update_series);

        final APIServices apiServices = new APIServices(getActivity().getApplicationContext());
        apiServices.getUpdateSeries(new ISeriesIdListener() {
            @Override
            public void onSuccess(int[] seriesIDs) {
                final ArrayList<Serie> seriesList = new ArrayList<Serie>();
                for(int i = 0; i < 20 ; i++){
                    apiServices.getSerieById(seriesIDs[i], new ISeriesListener() {
                        @Override
                        public void onSuccess(Serie serie) {
                            seriesList.add(serie);
                            SerieAdapter adapter = new SerieAdapter(getActivity().getApplicationContext(), seriesList);

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

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

}
