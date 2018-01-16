package com.example.root.tvapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.activity.SerieActivity;
import com.example.root.tvapp.adapter.SerieAdapter;
import com.example.root.tvapp.interfaces.ISeriesArrayListener;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState){
        final EditText serieName = (EditText) view.findViewById(R.id.search_edit);

        final Button searchBtn = (Button) view.findViewById(R.id.search_button);

        final ListView searchList = (ListView) view.findViewById(R.id.search_list);

        final APIServices apiServices = new APIServices(getActivity().getApplicationContext());

        searchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, serieName.getText().toString());
                if(serieName.getText().toString() != ""){
                    final ArrayList<Serie> serieNameList = new ArrayList<Serie>();
                    apiServices.searchSerieByName(serieName.getText().toString(), new ISeriesArrayListener() {
                        @Override
                        public void onSuccess(Serie[] series) {
                            for (int i = 0; i < series.length; i++){
                                serieNameList.add(series[i]);
                                SerieAdapter adapter = new SerieAdapter(getActivity().getApplicationContext(), serieNameList);

                                searchList.setAdapter(adapter);
                            }
                        }
                    });
                }
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Serie serie = (Serie) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), SerieActivity.class);
                intent.putExtra("serieId", serie.getId());
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

}
