package com.example.root.tvapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.root.tvapp.model.Favorites;

import java.util.ArrayList;

/**
 * Created by bitoux on 20/01/18.
 */

public class DAOFavorites extends DAOBase {

    public static final String favTable="FavTable";
    public static final String favId="FavID";
    public static final String favSerieID="FavSerieID";

    public DAOFavorites(Context context){
        super(context);
    }

    public void addFavorite(int serieID){
        ContentValues value = new ContentValues();
        value.put(DAOFavorites.favSerieID, serieID);
        mDB.insert(DAOFavorites.favTable, null, value);
    }

    public void deleteFavorite(int serieID){
        mDB.delete(DAOFavorites.favTable, DAOFavorites.favSerieID + " = ?", new String[] {String.valueOf(serieID)});
    }

    public boolean checkFav(int serieID){
        Cursor cursor = null;
        cursor = mDB.rawQuery("SELECT * FROM " + DAOFavorites.favTable + " WHERE " + DAOFavorites.favSerieID + " = " + serieID, null);
        if(cursor != null && cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Favorites> getFavorites(){
        Cursor cursor = null;
        ArrayList<Favorites> seriedIDs = new ArrayList<Favorites>();
        cursor = mDB.rawQuery("SELECT * FROM " + favTable, null);
        if(cursor != null){
            while (cursor.moveToNext()){
                Favorites fav = new Favorites(
                  cursor.getInt(cursor.getColumnIndex(DAOFavorites.favId)),
                  cursor.getInt(cursor.getColumnIndex(DAOFavorites.favSerieID))
                );
                seriedIDs.add(fav);
            }
            cursor.close();
        }
        return seriedIDs;
    }


}
