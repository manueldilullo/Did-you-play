package it.uniroma2.pjdm.didyouplayed.db.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.entity.Reccomendation;

public interface ReccomendationDAO extends DAO{
	public ArrayList<Reccomendation> loadReccomendationByReceiver(String username);
	public boolean insertReccomendation(Reccomendation rec);
}
