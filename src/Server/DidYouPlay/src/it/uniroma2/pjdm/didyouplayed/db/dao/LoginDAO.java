package it.uniroma2.pjdm.didyouplayed.db.dao;

import it.uniroma2.pjdm.didyouplayed.entity.Login;

public interface LoginDAO extends DAO{
	public Login getLoginByCredentials(String username, String password);
	public boolean insertLogin(Login login);
	public boolean updateLogin(Login login);
}
