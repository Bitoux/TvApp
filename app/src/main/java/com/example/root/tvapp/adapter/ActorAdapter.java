package com.example.root.tvapp.adapter;

import android.content.Context;
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

    public ActorAdapter(Context context, ArrayList<Actor> actors){
        super(context, 0, actors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Actor actor = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.actor_list, parent, false);
        }

        APIServices apiServices = new APIServices(this.getContext());

        TextView actorName = (TextView) convertView.findViewById(R.id.actor_name);
        TextView actorRole = (TextView) convertView.findViewById(R.id.actor_role);
        ImageView actorImg = (ImageView) convertView.findViewById(R.id.actor_img);

        actorName.setText(actor.getName());
        actorRole.setText(actor.getRole());
        new DownloadImageTask(actorImg).execute(apiServices.getSerieBanner(actor.getImg()));

        return convertView;

    }
}
