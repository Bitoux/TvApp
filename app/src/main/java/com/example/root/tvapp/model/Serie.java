package com.example.root.tvapp.model;

/**
 * Created by root on 23/11/17.
 */

public class Serie {
    private long id;
    private String name;
    private String[] genre;
    private String overview;
    private String rating;
    private String banner;

    public Serie(long id, String name, String[] genre, String overview, String rating, String banner){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.overview = overview;
        this.rating = rating;
        this.banner = banner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Override
    public String toString(){
        return "id: " + this.id + ", name: " + this.name + ", genre: " + this.genre + ", overview: " + this.overview + ", rating: " + this.rating + ", banner: " + this.banner;
    }

    public String genreToString(){
        String genreString = "";
        for(int i=0; i<this.genre.length; i++){
            if(i == 0){
                genreString = this.genre[i];
            }else{
                genreString = genreString + ", " + this.genre[i];
            }
        }
        return genreString;
    }
}
