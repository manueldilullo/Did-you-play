package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Played{
    private int score;
    private Videogame videogame;

    public Played(int score, Videogame videogame){
        this.score = score;
        this.videogame = videogame;
    }

    public Played(int id, String name, ArrayList<String> genres, HashMap<String, String> companies, HashMap<String, String> platforms, int rating, String pegi, String cover, String storyline, String summary, int score) {
        this.score = score;
        this.videogame = new Videogame(id, name, genres, companies, platforms, rating, pegi, cover, storyline, summary);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Videogame getVideogame(){
        return videogame;
    }

    public void setVideogame(Videogame videogame){
        this.videogame = videogame;
    }

    @Override
    public String toString() {
        return "Played{" +
                "score=" + score +
                "} " + videogame.toString();
    }
}
