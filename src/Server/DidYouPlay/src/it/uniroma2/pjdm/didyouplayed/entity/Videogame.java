package it.uniroma2.pjdm.didyouplayed.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Videogame {
	
	private int id;
	private String name;
	private ArrayList<String> genres;
 	private HashMap<String, String> companies;
 	private HashMap<String, String> platforms;
	private int rating;
	private int pegi;
	private String cover;
	private String storyline;
	private String summary;
	
	public Videogame() {
	}

	public Videogame(String name, ArrayList<String> genres, HashMap<String, String> companies, HashMap<String, String> platforms,
			int rating, int pegi, String cover, String storyline, String summary) {
		super();
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() { return id;}
	
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

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getPegi() {
		return pegi;
	}

	public void setPegi(int pegi) {
		this.pegi = pegi;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getStoryline() {
		return storyline;
	}

	public void setStoryline(String storyline) {
		this.storyline = storyline;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "Videogame [name=" + name + ", genres=" + genres.toString() + ", companies=" + companies
				+ ", platforms=" + platforms + ", rating=" + rating + ", pegi=" + pegi + ", cover=" + cover
				+ ", storyline=" + storyline + ", summary=" + summary + "]";
	}	
	
}
