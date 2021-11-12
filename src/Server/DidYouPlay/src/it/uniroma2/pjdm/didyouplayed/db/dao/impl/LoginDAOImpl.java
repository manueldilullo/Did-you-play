package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.uniroma2.pjdm.didyouplayed.db.dao.LoginDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Login;
import it.uniroma2.pjdm.didyouplayed.utils.BasicAuth;

public class LoginDAOImpl extends JdbcDAO implements LoginDAO {

	public LoginDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}

	@Override
	public Login getLoginByCredentials(String username, String password) {
		String query = "SELECT * FROM login WHERE username=? AND password=?";

		try {
			Login login = null;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, BasicAuth.EncryptMD5(password));
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				login = new Login(username, rset.getString("password"));
				break;
			}
			rset.close();
			ps.close();

			return login;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean insertLogin(Login login) {
		String SQL = "INSERT INTO login(username, password) VALUES(?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, login.getUsername());
			ps.setString(2, BasicAuth.EncryptMD5(login.getPassword()));
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateLogin(Login login) {
		String SQL = "UPDATE login SET username=?, password=? WHERE username=?";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, login.getUsername());
			ps.setString(2, BasicAuth.EncryptMD5(login.getPassword()));
			ps.setString(3, login.getUsername());

			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
