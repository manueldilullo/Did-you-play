package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;

public class VideogameDAOImpl extends JdbcDAO implements VideogameDAO {

	public VideogameDAOImpl(Connection conn) throws SQLException {
		super(conn);
	}

	@Override
	public Videogame loadVideogameById(int id) {
		String query = "SELECT * FROM videogame WHERE videogame_id=?";

		try {
			Videogame v = null;

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				v = new Videogame();
				
				v.setId(id);
				v.setName(rset.getString("name"));
				v.setRating(rset.getInt("rating"));
				v.setPegi(rset.getInt("pegi"));
				v.setCover(rset.getString("cover"));
				v.setStoryline(rset.getString("storyline"));
				v.setSummary(rset.getString("summary"));

				break;
			}
			ps.close();
			rset.close();

			if (!(v == null)) {
				v.setCompanies(getCompaniesByVideogame(id));
				v.setPlatforms(getPlatformsByVideogame(id));
				v.setGenres(getGenresByVideogame(id));
			}

			return v;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, String> getCompaniesByVideogame(int id) {
		String query = "SELECT company, role FROM involved WHERE videogame_id=?";

		HashMap<String, String> companies = new HashMap<String, String>();

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				companies.put(rset.getString("company"), rset.getString("role"));
			}
			rset.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return companies;
	}

	public HashMap<String, String> getPlatformsByVideogame(int id) {
		String query = "SELECT platform, release_date FROM released WHERE videogame_id=?";

		HashMap<String, String> platforms = new HashMap<String, String>();

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				platforms.put(rset.getString("platform"), rset.getDate("release_date").toString());
			}
			rset.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return platforms;
	}

	public ArrayList<String> getGenresByVideogame(int id) {
		String query = "SELECT genre FROM categorize WHERE videogame_id=?";

		ArrayList<String> genres = new ArrayList<String>();

		try {

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				genres.add(rset.getString("genre"));
			}
			rset.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return genres;
	}
	
	@Override
	public ArrayList<Videogame> loadAll(){
		String query = "SELECT * FROM videogame";
		try {
			ArrayList<Videogame> games = new ArrayList<Videogame>();
			
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				Videogame v = new Videogame();
				
				int id = rset.getInt("videogame_id");
				
				v.setId(id);
				v.setName(rset.getString("name"));
				v.setRating(rset.getInt("rating"));
				v.setPegi(rset.getInt("pegi"));
				v.setCover(rset.getString("cover"));
				v.setStoryline(rset.getString("storyline"));
				v.setSummary(rset.getString("summary"));

				v.setCompanies(getCompaniesByVideogame(id));
				v.setPlatforms(getPlatformsByVideogame(id));
				v.setGenres(getGenresByVideogame(id));
				
				games.add(v);
			}
			stmt.close();
			rset.close();
			
			return games;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public ArrayList<Videogame> loadVideogamesByName(String name) {
		String query = "SELECT videogame_id FROM videogame WHERE name LIKE ?";

		try {
			ArrayList<Videogame> games = new ArrayList<Videogame>();

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + name + "%");
			ResultSet rset = ps.executeQuery();

			while (rset.next()) {
				Videogame v = new Videogame();

				int id = rset.getInt("videogame_id");
				v = this.loadVideogameById(id);

				games.add(v);
			}
			rset.close();
			ps.close();

			return games;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * loadPopular returns an ArrayList<Videogame> with the top 50 Games in the DB.
	 * The score of a Videogame is calculated as the sum of the players that played
	 * it and the players that awaited/are awaiting it
	 */
	@Override
	public ArrayList<Videogame> loadPopular() {
		String query = "SELECT id, SUM(total_count.count) as total\r\n"
				+ "FROM (\r\n"
				+ "SELECT videogame_id as id, COUNT(videogame_id) as count\r\n"
				+ "FROM played\r\n"
				+ "GROUP BY id \r\n"
				+ "UNION ALL\r\n"
				+ "SELECT videogame_id as id, COUNT(videogame_id) as count\r\n"
				+ "FROM awaited\r\n"
				+ "GROUP BY id) as total_count\r\n"
				+ "GROUP BY id\r\n"
				+ "ORDER BY total DESC limit 50;";

		try {
			ArrayList<Videogame> videogames = new ArrayList<Videogame>();

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				int id = rset.getInt("id");
				Videogame v = this.loadVideogameById(id);
				videogames.add(v);
			}

			stmt.close();
			rset.close();

			return videogames;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
