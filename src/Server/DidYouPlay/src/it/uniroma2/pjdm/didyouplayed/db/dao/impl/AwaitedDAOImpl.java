package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.db.dao.AwaitedDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Awaited;

public class AwaitedDAOImpl extends JdbcDAO implements AwaitedDAO {

	public AwaitedDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}

	@Override
	public ArrayList<Awaited> loadAwaitedByUser(String username) {
		String query = "SELECT videogame_id, created_at FROM awaited WHERE user=?";

		try {
			ArrayList<Awaited> awaited = new ArrayList<Awaited>();

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				Awaited aw = new Awaited();
				aw.setUser(username);
				aw.setVideogame(rset.getInt("videogame_id"));
				aw.setCreatedAt(rset.getDate("created_at").toString());

				awaited.add(aw);
			}
			rset.close();
			ps.close();

			return awaited;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean insertAwaited(Awaited aw) {
		// created_at has a default value in SQL (it is NOW())
		String SQL = "INSERT INTO awaited(user, videogame_id) VALUES(?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, aw.getUser());
			ps.setInt(2, aw.getVideogame());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAwaited(Awaited aw) {
		String SQL = "DELETE FROM awaited WHERE user=? AND videogame_id=?";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);
			ps.setString(1, aw.getUser());
			ps.setInt(2, aw.getVideogame());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
