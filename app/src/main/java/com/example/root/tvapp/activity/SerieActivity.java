package com.example.root.tvapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.interfaces.IBooleanListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;
import com.example.root.tvapp.service.DownloadImageTask;

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


        // METTRE EN VAR
        final Button addFav = (Button) findViewById(R.id.addFavBtn);
        final Button delFav = (Button) findViewById(R.id.delFav);
        addFav.setVisibility(View.INVISIBLE);
        delFav.setVisibility(View.INVISIBLE);
        final TextView serieName = (TextView) findViewById(R.id.serieName);
        final TextView serieOverview = (TextView) findViewById(R.id.serieOverview);
        final TextView serieGenre = (TextView) findViewById(R.id.serieGenre);
        final TextView serieRating = (TextView) findViewById(R.id.serieRating);
        final ImageView banner = (ImageView) findViewById(R.id.banner);
        Long serieIdLong = getIntent().getLongExtra("serieId", 0);
        if(serieIdLong != 0){
            // METTRE API SERV EN VAR
            final APIServices apiServices = new APIServices(getApplicationContext());
            apiServices.getSerieById(serieIdLong.intValue(), new ISeriesListener() {
                @Override
                public void onSuccess(Serie serie) {
                    // GET IMAGE BANNER ASYNCH
                    // METTRE LOG
                    System.out.println(serie.toString());
                    System.out.println(apiServices.getSerieBanner(serie.getBanner()));
                    new DownloadImageTask(banner).execute(apiServices.getSerieBanner(serie.getBanner()));
                    serieName.setText(serie.getName());
                    serieOverview.setText(serie.getOverview());
                    serieGenre.setText(serie.genreToString());
                    serieRating.setText(serie.getRating());

                    apiServices.getUserFavorites(String.valueOf(serie.getId()), new IBooleanListener() {
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


}
