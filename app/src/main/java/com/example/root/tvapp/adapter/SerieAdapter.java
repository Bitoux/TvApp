package com.example.root.tvapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.model.Serie;

import java.util.ArrayList;

/**
 * Created by root on 30/11/17.
 */

public class SerieAdapter extends ArrayAdapter<Serie> {

    private static class ViewHolder{
        private TextView itemView;
    }

    public SerieAdapter(Context context, ArrayList<Serie> items) {
        super(context, 0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Serie item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.serie_list, parent, false);
        }

        TextView serieName = (TextView) convertView.findViewById(R.id.serie_name);
        TextView serieRating = (TextView) convertView.findViewById(R.id.serie_rating);
        TextView serieGenre = (TextView) convertView.findViewById(R.id.serie_genre);

        if(item.getRating() == null && item.getGenre() == null){
            serieGenre.setVisibility(View.INVISIBLE);
            serieGenre.setHeight(0);
            serieRating.setVisibility(View.INVISIBLE);
            serieRating.setHeight(0);
            serieName.setText(item.getName());
            serieName.setGravity(Gravity.CENTER);
            serieName.setHeight(100);
        }else{
            serieName.setText(item.getName());
            serieRating.setText(item.getRating());
            serieGenre.setText(item.getGenre());
        }



        return convertView;
    }

}
