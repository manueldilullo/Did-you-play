package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;

public class Videogame {

    private int id;
    private String name;
    private ArrayList<String> genres;
    private HashMap<String, String> companies;
    private HashMap<String, String> platforms;
    private int rating;
    private String pegi;
    private String cover;
    private String storyline;
    private String summary;

    public Videogame() {
    }

    public Videogame(int id, String name, ArrayList<String> genres, HashMap<String, String> companies, HashMap<String, String> platforms,
                     int rating, String pegi, String cover, String storyline, String summary) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.companies = companies;
        this.platforms = platforms;
        this.rating = rating;
        this.pegi = pegi;
        this.cover = cover;
        this.storyline = storyline;
        this.summary = summary;
    }

    public int getId(){return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public HashMap<String, String> getCompanies() {
        return companies;
    }

    public void setCompanies(HashMap<String, String> companies) {
        this.companies = companies;
    }

    public HashMap<String, String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(HashMap<String, String> platforms) {
       this.platforms = platforms;
    }

    public int getRating() {
        return rating;
    }

    public String getPegi() {
        return pegi;
    }

    public String getCover() {
        return cover;
    }

    public String getStoryline() {
        return storyline;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isReleased(){
        if(platforms.isEmpty()){
            return true;
        }

        long today = System.currentTimeMillis();
        for(String dateString : platforms.values()){
           java.sql.Date date = Date.valueOf(dateString);
           if((date.getTime() - today) <= 0)
               return true;
        }
        return false;
    }

    public int getMissingDays(){
        if(this.isReleased()){
            return -1;
        }

        long max = 0;

        long today = System.currentTimeMillis();
        for(String dateString : platforms.values()){
            java.sql.Date date = Date.valueOf(dateString);
            long missing_time = date.getTime() - today;
            if(max < missing_time)
                max = missing_time;
        }
        return (int) (max / (24 * 60 * 60 * 1000));
    }

    @Override
    public String toString() {
        return "Videogame [name=" + name + ", genres=" + genres.toString() + ", companies=" + companies
                + ", platforms=" + platforms + ", rating=" + rating + ", pegi=" + pegi + ", cover=" + cover
                + ", storyline=" + storyline + ", summary=" + summary + "]";
    }
}