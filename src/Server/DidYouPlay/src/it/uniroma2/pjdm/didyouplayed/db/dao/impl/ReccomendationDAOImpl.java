package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.uniroma2.pjdm.didyouplayed.db.dao.ReccomendationDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Reccomendation;

public class ReccomendationDAOImpl extends JdbcDAO implements ReccomendationDAO {

	public ReccomendationDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}

	@Override
	public ArrayList<Reccomendation> loadReccomendationByReceiver(String username) {
		String query = "SELECT * FROM reccomendation WHERE receiver=?";

		try {
			ArrayList<Reccomendation> reccomendations = new ArrayList<Reccomendation>();

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				Reccomendation rec = new Reccomendation();

				rec.setReceiver(rset.getString("receiver"));
				rec.setSender(rset.getString("sender"));
				rec.setVideogame(rset.getInt("videogame_id"));
				rec.setCreatedAt(rset.getDate("created_at").toString());

				reccomendations.add(rec);
			}
			rset.close();
			ps.close();

			return reccomendations;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean insertReccomendation(Reccomendation rec) {
		String SQL = "INSERT INTO reccomendation(sender, receiver, videogame_id) VALUES (?,?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(SQL);

			ps.setString(1, rec.getSender());
			ps.setString(2, rec.getReceiver());
			ps.setInt(3, rec.getVideogame());
			ps.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
