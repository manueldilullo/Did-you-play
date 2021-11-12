package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.db.dao.PlayedDAO;
import it.uniroma2.pjdm.didyouplayed.entity.OutOfRangeException;
import it.uniroma2.pjdm.didyouplayed.entity.Played;

public class PlayedDAOImpl extends JdbcDAO implements PlayedDAO {

	public PlayedDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}

	@Override
	public ArrayList<Played> loadPlayedByUser(String username) {
		String query = "SELECT videogame_id, score FROM played WHERE user=?";

		try {
			ArrayList<Played> played = new ArrayList<Played>();

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				Played p = new Played();

				p.setUser(username);
				p.setVideogame(rset.getInt("videogame_id"));
				p.setScore(rset.getInt("score"));

				played.add(p);
			}
			rset.close();
			ps.close();

			return played;

		} catch (SQLException | OutOfRangeException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean insertPlayed(Played p) {
		String SQL = "INSERT INTO played(user, videogame_id, score) VALUES(?,?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, p.getUser());
			ps.setInt(2, p.getVideogame());
			ps.setInt(3, p.getScore());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deletePlayed(Played p) {
		String SQL = "DELETE FROM played WHERE user=? AND videogame_id=?";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);
			ps.setString(1, p.getUser());
			ps.setInt(2, p.getVideogame());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
