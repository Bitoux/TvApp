package com.example.root.tvapp.model;

/**
 * Created by bitoux on 20/01/18.
 */

public class Favorites {
    private int favoriteID;
    private int favoriteSerieID;

    public Favorites(int id, int serieID){
        this.favoriteID = id;
        this.favoriteSerieID = serieID;
    }

    public int getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    public int getFavoriteSerieID() {
        return favoriteSerieID;
    }

    public void setFavoriteSerieID(int favoriteSerieID) {
        this.favoriteSerieID = favoriteSerieID;
    }
}
