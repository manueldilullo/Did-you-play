package it.uniroma2.pjdm.didyouplayed.db.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.entity.User;

public interface UserDAO extends DAO{
	public User loadUserByUsername(String username); 
	public ArrayList<User> loadAllUsers();
	public ArrayList<User> loadUsersByUsername(String username);
	public boolean insertUser(User usr);
	public boolean deleteUser(String username);
}
