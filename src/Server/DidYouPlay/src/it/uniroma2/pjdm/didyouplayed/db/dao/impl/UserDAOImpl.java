package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;

import it.uniroma2.pjdm.didyouplayed.db.dao.UserDAO;
import it.uniroma2.pjdm.didyouplayed.entity.User;

public class UserDAOImpl extends JdbcDAO implements UserDAO {

	public UserDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}
	
	@Override
	public ArrayList<User> loadAllUsers() {
		String query = "SELECT * FROM user";

		try {
			ArrayList<User> users = new ArrayList<User>();

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				User usr = new User();

				usr.setName(rset.getString("name"));
				usr.setSurname(rset.getString("surname"));
				usr.setUsername(rset.getString("username"));
				usr.setEmail(rset.getString("email"));
				usr.setBirthdate(rset.getString("birthdate"));
				usr.setGender(rset.getString("birthdate"));

				users.add(usr);
			}
			rset.close();
			stmt.close();

			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public User loadUserByUsername(String username) {
		String query = "SELECT * FROM user WHERE username = ?";

		try {
			User usr = null;

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);

			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				usr = new User();

				usr.setName(rset.getString("name"));
				usr.setSurname(rset.getString("surname"));
				usr.setUsername(rset.getString("username"));
				usr.setEmail(rset.getString("email"));
				usr.setBirthdate(rset.getString("birthdate"));
				usr.setGender(rset.getString("birthdate"));

				break;
			}
			rset.close();
			ps.close();

			return usr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load all the users that contains the String "username" inside their username
	 * field.
	 */
	@Override
	public ArrayList<User> loadUsersByUsername(String username) {
		String query = "SELECT * FROM user WHERE username LIKE ?";

		try {
			ArrayList<User> users = new ArrayList<User>();

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + username + "%");

			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				User usr = new User();

				usr.setName(rset.getString("name"));
				usr.setSurname(rset.getString("surname"));
				usr.setUsername(rset.getString("username"));
				usr.setEmail(rset.getString("email"));
				usr.setBirthdate(rset.getString("birthdate"));
				usr.setGender(rset.getString("birthdate"));

				users.add(usr);
			}
			rset.close();
			ps.close();

			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean insertUser(User usr) {
		String SQL = "INSERT INTO user(name, surname, username, email, birthdate, gender) VALUES(?,?,?,?,?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, usr.getName());
			ps.setString(2, usr.getSurname());
			ps.setString(3, usr.getUsername());
			ps.setString(4, usr.getEmail());
			ps.setString(5, usr.getBirthdate());
			ps.setString(6, usr.getGender());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteUser(String username) {
		String SQL = "DELETE FROM user WHERE username=?";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);
			ps.setString(1, username);
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
