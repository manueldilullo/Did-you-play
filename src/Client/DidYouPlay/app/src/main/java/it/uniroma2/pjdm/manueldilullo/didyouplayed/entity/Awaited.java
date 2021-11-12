package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Awaited extends Videogame{
    private String created_at;
    private Videogame videogame;

    public Awaited(Videogame videogame, String created_at){
        this.videogame = videogame;
        this.created_at = created_at;
    }

    public Awaited(int id, String name, ArrayList<String> genres, HashMap<String, String> companies, HashMap<String, String> platforms, int rating, String pegi, String cover, String storyline, String summary, String created_at) {
        this.videogame = new Videogame(id, name, genres, companies, platforms, rating, pegi, cover, storyline, summary);
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Videogame getVideogame(){
        return videogame;
    }

    public void setVideogame(Videogame videogame){
        this.videogame = videogame;
    }

    @Override
    public String toString() {
        return "Awaited{" +
                "created_at='" + created_at + '\'' +
                "}" + videogame.toString();
    }
}
