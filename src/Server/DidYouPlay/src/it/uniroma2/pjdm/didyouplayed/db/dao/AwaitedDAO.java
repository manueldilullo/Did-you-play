package it.uniroma2.pjdm.didyouplayed.db.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.entity.Awaited;

public interface AwaitedDAO extends DAO{
	public ArrayList<Awaited> loadAwaitedByUser(String username);
	public boolean insertAwaited(Awaited aw);
	public boolean deleteAwaited(Awaited aw);
}
