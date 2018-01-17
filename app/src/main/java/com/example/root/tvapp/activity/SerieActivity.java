package com.example.root.tvapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.adapter.ActorAdapter;
import com.example.root.tvapp.interfaces.IActorArrayListener;
import com.example.root.tvapp.interfaces.IBooleanListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Actor;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;
import com.example.root.tvapp.service.DownloadImageTask;

import java.util.ArrayList;

/**
 * Created by root on 08/12/17.
 */

public class SerieActivity extends AppCompatActivity {

    private APIServices apiServices;

    public APIServices getApiServices() {
        if(apiServices == null){
            apiServices = new APIServices(getApplicationContext());
        }
        return apiServices;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // METTRE EN VAR
        final Button addFav = (Button) findViewById(R.id.add_fav_btn);
        final Button delFav = (Button) findViewById(R.id.del_fav);
        addFav.setVisibility(View.INVISIBLE);
        delFav.setVisibility(View.INVISIBLE);


        final TextView serieName = (TextView) findViewById(R.id.serie_name);
        final TextView serieOverview = (TextView) findViewById(R.id.serie_overview);
        final TextView serieGenre = (TextView) findViewById(R.id.serie_genre);
        final TextView serieRating = (TextView) findViewById(R.id.serie_rating);
        final ImageView banner = (ImageView) findViewById(R.id.banner);
        final ListView actorList = (ListView) findViewById(R.id.actor_list);
        final Long serieIdLong = getIntent().getLongExtra("serieId", 0);
        if(serieIdLong != 0){
            // METTRE API SERV EN VAR
            final APIServices apiServices = new APIServices(getApplicationContext());
            apiServices.getSerieById(serieIdLong.intValue(), new ISeriesListener() {
                @Override
                public void onSuccess(Serie serie) {
                    // GET IMAGE BANNER ASYNCH
                    // METTRE LOG
                    new DownloadImageTask(banner).execute(apiServices.getSerieBanner(serie.getBanner()));
                    serieName.setText(serie.getName());
                    serieOverview.setText(serie.getOverview());
                    serieGenre.setText(serie.getGenre());
                    serieRating.setText(serie.getRating());

                    apiServices.checkUserFavorites(String.valueOf(serie.getId()), new IBooleanListener() {
                        @Override
                        public void onSuccess(Boolean result) {
                            if(result){
                                addFav.setVisibility(View.INVISIBLE);
                                delFav.setVisibility(View.VISIBLE);
                            }else{
                                addFav.setVisibility(View.VISIBLE);
                                delFav.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    apiServices.getSerieActors(serieIdLong.intValue(), new IActorArrayListener() {
                        @Override
                        public void onSuccess(Actor[] actors) {
                            final ArrayList<Actor> actorArrayList = new ArrayList<Actor>();
                            for(int i = 0 ; i < actors.length ; i++){
                                Log.d("Actors", actors[i].toString());
                                actorArrayList.add(actors[i]);
                            }
                            ActorAdapter adapter = new ActorAdapter(getApplicationContext(), actorArrayList);

                            actorList.setAdapter(adapter);
                        }
                    });
                }
            });
        }

        addFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                APIServices apiServices = new APIServices(getApplicationContext());
                Long serieIdLong = getIntent().getLongExtra("serieId", 0);

                apiServices.addFavToUser(serieIdLong.intValue(), new IBooleanListener() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if(result){
                            addFav.setVisibility(View.INVISIBLE);
                            delFav.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        delFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                APIServices apiServices = new APIServices(getApplicationContext());
                Long serieIdLong = getIntent().getLongExtra("serieId", 0);

                apiServices.delFavToUser(serieIdLong.intValue(), new IBooleanListener() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if(result){
                            addFav.setVisibility(View.INVISIBLE);
                            delFav.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    private void configureToolbar(Toolbar toolbar) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


}
