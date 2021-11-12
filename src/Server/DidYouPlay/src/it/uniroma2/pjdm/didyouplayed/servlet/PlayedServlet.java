package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.PlayedDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Played;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PlayedServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// DAOs
	VideogameDAO videogameDAO;
	PlayedDAO playedDAO;

	public PlayedServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();

		String ip = context.getInitParameter("ip");
		String port = context.getInitParameter("port");
		String dbName = context.getInitParameter("dbName");
		String userName = context.getInitParameter("userName");
		String password = context.getInitParameter("password");

		System.out.print("PlayedServlet. Opening DB connection...");

		try {
			playedDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getPlayedDao();
			videogameDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getVideogameDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * Load a JSONArray with all the games played by the user with username
	 * corresponding to the one in the 'username' parameter
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("PlayedServlet. Invoking doGet method...");

		if (req.getParameter("user") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'user' parameter");
			return;
		}

		String username = req.getParameter("user");
		ArrayList<Played> played = playedDAO.loadPlayedByUser(username);

		JSONArray jar = new JSONArray();

		for (Played p : played) {
			Videogame v = videogameDAO.loadVideogameById(p.getVideogame());

			if (v == null)
				continue;

			JSONObject jobj = new JSONObject();
			jobj.put("user", p.getUser());
			jobj.put("score", p.getScore());
			jobj.put("videogame", new JSONObject(v));

			jar.put(jobj);
		}

		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		out.print(jar.toString());
		out.flush();
	}

	/**
	 * Insert a Played object with <user> <score> and <videogame_id> parameters in
	 * the db
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("PlayedServlet. Invoking doPost method...");

		if (req.getParameter("user") == null || req.getParameter("videogame_id") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'user' and 'videogame_id' parameter");
			return;
		}

		int videogame;
		int score;
		try {
			videogame = Integer.valueOf(req.getParameter("videogame_id"));
			score = Integer.valueOf(req.getParameter("score"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("'videogame_id' must be an Integer");
			return;
		}
		String user = req.getParameter("user");

		if (!playedDAO.insertPlayed(new Played(user, videogame, score))) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal Server Error");
			return;
		}

		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Delete the played record with same videogame_id and user as those received as
	 * parameters
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("PlayedServlet. Invoking doDelete method...");
		if (req.getParameter("user") == null || req.getParameter("videogame_id") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'user' and 'videogame_id' parameter");
			return;
		}

		int videogame_id;
		try {
			videogame_id = Integer.valueOf(req.getParameter("videogame_id"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("'videogame_id' must be an Integer");
			return;
		}
		String user = req.getParameter("user");

		Played p = new Played();
		p.setUser(user);
		p.setVideogame(videogame_id);

		if (!playedDAO.deletePlayed(p)) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().append("Played record not found");
			return;
		} else
			resp.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	public void destroy() {
		System.out.print("PlayedServlet. Closing DB connection...");
		videogameDAO.closeConnection();
		playedDAO.closeConnection();
		System.out.println("DONE.");
	}
}