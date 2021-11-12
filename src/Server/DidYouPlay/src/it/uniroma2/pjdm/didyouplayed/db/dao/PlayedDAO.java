package it.uniroma2.pjdm.didyouplayed.db.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.entity.Played;

public interface PlayedDAO extends DAO{
	public ArrayList<Played> loadPlayedByUser(String username);
	public boolean insertPlayed(Played p);
	public boolean deletePlayed(Played p);
}
