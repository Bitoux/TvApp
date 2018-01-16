package com.example.root.tvapp.model;

/**
 * Created by root on 14/01/18.
 */

public class Actor {
    private int id;
    private String img;
    private String name;
    private String role;
    private int serie;

    public Actor(int id, String img, String name, String role, int serie){
        this.id = id;
        this.img = img;
        this.name = name;
        this.role = role;
        this.serie = serie;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public String toString(){
        return "Id: " + this.id + ", img:" + this.img + ", name: " + this.name + ", role: " + this.role + ", serie: " + this.serie;
    }
}
