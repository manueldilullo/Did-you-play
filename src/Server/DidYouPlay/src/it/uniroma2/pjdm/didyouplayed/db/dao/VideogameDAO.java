package it.uniroma2.pjdm.didyouplayed.db.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.entity.Videogame;

public interface VideogameDAO extends DAO{
	public Videogame loadVideogameById(int id);
	public ArrayList<Videogame> loadAll();
	public ArrayList<Videogame> loadVideogamesByName(String name);
	public ArrayList<Videogame> loadPopular();
}
