package com.example.root.tvapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.tvapp.R;
import com.example.root.tvapp.adapter.ActorAdapter;
import com.example.root.tvapp.database.DAOActor;
import com.example.root.tvapp.database.DAOFavorites;
import com.example.root.tvapp.database.DAOSerie;
import com.example.root.tvapp.interfaces.IActorArrayListener;
import com.example.root.tvapp.interfaces.IBooleanListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.model.Actor;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.service.APIServices;
import com.example.root.tvapp.service.DownloadImageTask;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

/**
 * Created by root on 08/12/17.
 */

public class SerieActivity extends AppCompatActivity {

    private APIServices apiServices;
    private FloatingActionButton addFav;
    private FloatingActionButton delFav;
    private FloatingActionButton shareSerie;
    private TextView serieName;
    private TextView serieOverview;
    private TextView serieGenre;
    private TextView serieRating;
    private ImageView banner;
    private ListView actorList;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Long serieIdLong;


    public APIServices getApiServices() {
        if(apiServices == null){
            apiServices = new APIServices(getApplicationContext());
        }
        return apiServices;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("388756698237903");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_serie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // METTRE EN VAR
        this.shareSerie = (FloatingActionButton) findViewById(R.id.share_facabook);
        this.addFav = (FloatingActionButton) findViewById(R.id.add_fav_btn);
        this.delFav = (FloatingActionButton) findViewById(R.id.del_fav);
        this.addFav.setVisibility(View.GONE);
        this.delFav.setVisibility(View.GONE);


        this.serieName = (TextView) findViewById(R.id.serie_name);
        this.serieOverview = (TextView) findViewById(R.id.serie_overview);
        this.serieGenre = (TextView) findViewById(R.id.serie_genre);
        this.serieRating = (TextView) findViewById(R.id.serie_rating);
        this.banner = (ImageView) findViewById(R.id.banner);
        this.actorList = (ListView) findViewById(R.id.actor_list);
        this.serieIdLong = getIntent().getLongExtra("serieId", 0);

        if(serieIdLong != 0){
            if(isOnline()){
                callbackManager = CallbackManager.Factory.create();
                shareDialog = new ShareDialog(this);
                // METTRE API SERV EN VAR
                this.apiServices = new APIServices(getApplicationContext());
                this.apiServices.getSerieById(serieIdLong.intValue(), new ISeriesListener() {
                    @Override
                    public void onSuccess(Serie serie) {
                        // GET IMAGE BANNER ASYNCH
                        // METTRE LOG
                        new DownloadImageTask(banner).execute(apiServices.getSerieBanner(serie.getBanner()));
                        serieName.setText(serie.getName());
                        serieOverview.setText(serie.getOverview());
                        serieGenre.setText(getResources().getText(R.string.genre) + serie.getGenre());
                        serieRating.setText(getResources().getText(R.string.rating) + serie.getRating());

                        DAOFavorites daoFavorites = new DAOFavorites(getApplicationContext());
                        daoFavorites.open();
                        Long id = serie.getId();
                        if(daoFavorites.checkFav(id.intValue())){
                            addFav.setVisibility(View.GONE);
                            delFav.setVisibility(View.VISIBLE);
                        }else{
                            addFav.setVisibility(View.VISIBLE);
                            delFav.setVisibility(View.GONE);
                        }
                        daoFavorites.close();

                        apiServices.getSerieActors(serieIdLong.intValue(), new IActorArrayListener() {
                            @Override
                            public void onSuccess(Actor[] actors) {
                                final ArrayList<Actor> actorArrayList = new ArrayList<Actor>();
                                Log.d("ACTORS ARRAY", actors.toString());
                                for(int i = 0 ; i < actors.length ; i++){
                                    Log.d("Actors", actors[i].toString());
                                    actorArrayList.add(actors[i]);
                                }
                                ActorAdapter adapter = new ActorAdapter(getApplicationContext(), actorArrayList, isOnline());

                                actorList.setAdapter(adapter);
                            }
                        });
                    }
                });
            }else{
                delFav.setVisibility(View.GONE);
                addFav.setVisibility(View.GONE);
                this.shareSerie.setVisibility(View.GONE);

                DAOSerie daoSerie = new DAOSerie(getApplicationContext());
                daoSerie.open();
                Serie serie = daoSerie.selectSerie(serieIdLong.intValue());

                serieName.setText(serie.getName());
                serieOverview.setText(serie.getOverview());
                serieGenre.setText(getResources().getText(R.string.genre) + serie.getGenre());
                serieRating.setText(getResources().getText(R.string.rating) + serie.getRating());


                DAOActor daoActor = new DAOActor(getApplicationContext());
                daoActor.open();
                ArrayList<Actor> actors = daoActor.getSerieActors(serieIdLong.intValue());
                Log.d("FINAL ACTOR", "FINAL ACTOR");
                Log.d("FINAL", actors.get(0).toString());
                ActorAdapter adapterOff = new ActorAdapter(getApplicationContext(), actors, isOnline());

                actorList.setAdapter(adapterOff);

            }
        }

        addFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                apiServices.addFavToUser(serieIdLong.intValue(), new IBooleanListener() {
                    @Override
                    public void onSuccess(Boolean result) {
                        addFavFromDAO(serieIdLong.intValue());
                        addFav.setVisibility(View.GONE);
                        delFav.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.add_fav), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        actorList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        delFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                apiServices.delFavToUser(serieIdLong.intValue(), new IBooleanListener() {
                    @Override public void onSuccess(Boolean result) {
                        deleteFavFromDAO(serieIdLong.intValue());
                        addFav.setVisibility(View.VISIBLE);
                        delFav.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.del_fav), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        shareSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SHARE", "SHARE");
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://www.thetvdb.com/?tab=series&id=" + serieIdLong.toString()))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void configureToolbar(Toolbar toolbar) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void deleteFavFromDAO(int serieID){
        DAOFavorites daoFavorites = new DAOFavorites(getApplicationContext());
        daoFavorites.open();
        daoFavorites.deleteFavorite(serieID);
    }

    public void addFavFromDAO(int serieID){
        DAOFavorites daoFavorites = new DAOFavorites(getApplicationContext());
        daoFavorites.open();
        if(!daoFavorites.checkFav(serieID)){
            daoFavorites.addFavorite(serieID);
        }

    }


}
