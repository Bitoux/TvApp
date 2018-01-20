package com.example.root.tvapp.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.model.Actor;
import com.example.root.tvapp.service.APIServices;
import com.example.root.tvapp.service.DownloadImageTask;

import java.util.ArrayList;

/**
 * Created by root on 14/01/18.
 */

public class ActorAdapter extends ArrayAdapter<Actor> {
    private boolean online;

    public ActorAdapter(Context context, ArrayList<Actor> actors, boolean online){
        super(context, 0, actors);
        this.online = online;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Actor actor = getItem(position);

        if(this.online){
            if(convertView == null){
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.actor_list, parent, false);
            }
            ImageView actorImg = (ImageView) convertView.findViewById(R.id.actor_img);
            APIServices apiServices = new APIServices(this.getContext());
            new DownloadImageTask(actorImg).execute(apiServices.getSerieBanner(actor.getImg()));
        }else{
            if(convertView == null){
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.actor_offline_list, parent, false);
            }
        }




        TextView actorName = (TextView) convertView.findViewById(R.id.actor_name);
        TextView actorRole = (TextView) convertView.findViewById(R.id.actor_role);


        actorName.setText(actor.getName());
        actorRole.setText(actor.getRole());


        return convertView;

    }
}
