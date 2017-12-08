package com.example.root.tvapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.model.Serie;

import org.w3c.dom.Text;

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

        TextView serieName = (TextView) convertView.findViewById(R.id.serieName);
        TextView serieRating = (TextView) convertView.findViewById(R.id.serieRating);
        TextView serieGenre = (TextView) convertView.findViewById(R.id.serieGenre);

        serieName.setText(item.getName());
        serieRating.setText(item.getRating());
        serieGenre.setText(item.genreToString());

        return convertView;
    }

}
