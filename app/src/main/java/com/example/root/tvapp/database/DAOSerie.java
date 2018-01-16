package com.example.root.tvapp.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.root.tvapp.model.Serie;

/**
 * Created by root on 16/01/18.
 */

public class DAOSerie extends DAOBase {

    public static final String serieTable="SerieTable";
    public static final String serieId="SerieID";
    public static final String serieName="SerieName";
    public static final String serieOverview="SerieOverview";
    public static final String serieRating="SerieRating";
    public static final String serieBanner="SerieBanner";
    public static final String serieGenre="SerieGenre";

    public static final String TABLE_CREATE = "CREATE TABLE "+serieTable+" ("+serieId+ " INTEGER PRIMARY KEY , "+
        serieName+ " TEXT, " + serieOverview + " TEXT, " + serieRating + " TEXT, " + serieBanner + " TEXT);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + serieTable + ";";

    public DAOSerie(Context context){
        super(context);
    }

    public void addSerie(Serie s){
        ContentValues value = new ContentValues();
        Long id = s.getId();
        value.put(DAOSerie.serieId, id.intValue());
        value.put(DAOSerie.serieName, s.getName());
        value.put(DAOSerie.serieBanner, s.getBanner());
        value.put(DAOSerie.serieGenre, s.genreToString());
        value.put(DAOSerie.serieRating, s.getRating());
        value.put(DAOSerie.serieOverview, s.getOverview());
        mDB.insert(DAOSerie.serieTable, null, value);
    }

    public void deleteSerie(int serieId){
        mDB.delete(DAOSerie.serieTable, serieId + " = ?", new String[] {String.valueOf(serieId)});
    }

    public void editSerie(Serie s){
        ContentValues value = new ContentValues();
        value.put(DAOSerie.serieName, s.getName());
        value.put(DAOSerie.serieBanner, s.getBanner());
        value.put(DAOSerie.serieGenre, s.genreToString());
        value.put(DAOSerie.serieRating, s.getRating());
        value.put(DAOSerie.serieOverview, s.getOverview());
        mDB.update(DAOSerie.serieTable, value, serieId  + " = ?", new String[] {String.valueOf(m.getId())});
    }

    public void selectSerie(int serieId){

    }

}
