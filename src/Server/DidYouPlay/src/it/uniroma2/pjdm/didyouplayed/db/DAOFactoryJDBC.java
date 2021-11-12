package it.uniroma2.pjdm.didyouplayed.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import it.uniroma2.pjdm.didyouplayed.db.dao.AwaitedDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.LoginDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.PlayedDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.ReccomendationDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.UserDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.AwaitedDAOImpl;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.JdbcDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.LoginDAOImpl;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.PlayedDAOImpl;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.ReccomendationDAOImpl;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.UserDAOImpl;
import it.uniroma2.pjdm.didyouplayed.db.dao.impl.VideogameDAOImpl;

public class DAOFactoryJDBC {
	
	// Attributes
	private Connection conn;
	private HashMap<String, JdbcDAO> daos;

	// Singleton instance
	private static DAOFactoryJDBC instance;
	
	// DAOs LIST
	private static final String LOGIN_DAO = "login.dao";
	private static final String USER_DAO = "user.dao";
	private static final String VIDEOGAME_DAO = "videogame.dao";
	private static final String PLAYED_DAO = "played.dao";
	private static final String AWAITED_DAO = "awaited.dao";
	private static final String RECCOMENDATION_DAO = "reccomendation.dao";
	
	private DAOFactoryJDBC(String ip, String port, String dbName, String userName, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + "/" + dbName
							+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		daos = new HashMap<String, JdbcDAO>();	
	}
	
	
	// If factory doesn't exists create a new Instance
	// otherwise return the current instance
	public static DAOFactoryJDBC getInstance(String ip, String port, String dbName, String userName, String pwd) throws SQLException {
		if (instance == null) {
			instance = new DAOFactoryJDBC(ip, port, dbName, userName, pwd);
		}

		return instance;
	}
	
	public LoginDAO getLoginDao() throws SQLException{
		if(!daos.containsKey(LOGIN_DAO)) {
			daos.put(LOGIN_DAO, new LoginDAOImpl(conn));
		}
		
		return (LoginDAO) daos.get(LOGIN_DAO);
	}
	
	public UserDAO getUserDao() throws SQLException{
		if(!daos.containsKey(USER_DAO)) {
			daos.put(USER_DAO, new UserDAOImpl(conn));
		}
		
		return (UserDAO) daos.get(USER_DAO);
	}
	
	public VideogameDAO getVideogameDao() throws SQLException{
		if(!daos.containsKey(VIDEOGAME_DAO)) {
			daos.put(VIDEOGAME_DAO, new VideogameDAOImpl(conn));
		}
		
		return (VideogameDAO) daos.get(VIDEOGAME_DAO);
	}
	
	public ReccomendationDAO getReccomendationDao() throws SQLException{
		if(!daos.containsKey(RECCOMENDATION_DAO)) {
			daos.put(RECCOMENDATION_DAO, new ReccomendationDAOImpl(conn));
		}
		
		return (ReccomendationDAO) daos.get(RECCOMENDATION_DAO);
	}
	
	public AwaitedDAO getAwaitedDao() throws SQLException{
		if(!daos.containsKey(AWAITED_DAO)) {
			daos.put(AWAITED_DAO, new AwaitedDAOImpl(conn));
		}
		
		return (AwaitedDAO) daos.get(AWAITED_DAO);
	}
	
	public PlayedDAO getPlayedDao() throws SQLException{
		if(!daos.containsKey(PLAYED_DAO)) {
			daos.put(PLAYED_DAO, new PlayedDAOImpl(conn));
		}
		
		return (PlayedDAO) daos.get(PLAYED_DAO);
	}
	
	public void closeConnection() {

		Collection<JdbcDAO> dao_coll = daos.values();
		Iterator<JdbcDAO> daoIterator = dao_coll.iterator();
		while (daoIterator.hasNext()) {
			daoIterator.next().closeConnection();
		}

		try {
			this.conn.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection to database");
			e.printStackTrace();
		}
	}
	
}