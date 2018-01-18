package com.example.root.tvapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.tvapp.R;
import com.example.root.tvapp.activity.SerieActivity;
import com.example.root.tvapp.adapter.SerieAdapter;
import com.example.root.tvapp.database.DAOSerie;
import com.example.root.tvapp.interfaces.ISeriesIdListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;

import java.util.ArrayList;

public class UpdateFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView seriesView;


    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        this.seriesView = (ListView) view.findViewById(R.id.update_series);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        final APIServices apiServices = new APIServices(getActivity().getApplicationContext());
        if(isOnline()){
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
        }else{
            DAOSerie daoSerie = new DAOSerie(getActivity().getApplicationContext());
            daoSerie.open();
            Serie[] series = new Serie[20];
            series = daoSerie.getLastSeries();
            final ArrayList<Serie> seriesList = new ArrayList<Serie>();
            for(int i = 0; i < series.length; i++){
                seriesList.add(series[i]);
            }
            SerieAdapter adapter = new SerieAdapter(getActivity().getApplicationContext(), seriesList);
            seriesView.setAdapter(adapter);
        }


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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isOnline()){
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
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "You have no connection. Retry later", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
