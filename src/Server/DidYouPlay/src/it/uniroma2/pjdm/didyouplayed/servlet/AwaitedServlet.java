package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.AwaitedDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Awaited;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AwaitedServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// DAOs
	VideogameDAO videogameDAO;
	AwaitedDAO awaitedDAO;

	public AwaitedServlet() {
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

		System.out.print("AwaitedServlet. Opening DB connection...");

		try {
			awaitedDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getAwaitedDao();
			videogameDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getVideogameDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * Load a JSONArray with all the games awaited by the user with username
	 * corresponding to the one in the 'username' parameter
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("AwaitedServlet. Invoking doGet method...");

		if (req.getParameter("user") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'user' parameter");
			return;
		}

		String username = req.getParameter("user");
		ArrayList<Awaited> awaited = awaitedDAO.loadAwaitedByUser(username);

		JSONArray jar = new JSONArray();

		for (Awaited aw : awaited) {
			Videogame v = videogameDAO.loadVideogameById(aw.getVideogame());

			if (v == null)
				continue;

			JSONObject jobj = new JSONObject();
			jobj.put("user", aw.getUser());
			jobj.put("created_at", aw.getCreatedAt());
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
	 * Insert an Awaited object with <user> <created_at> and <videogame_id>
	 * parameters in the db
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("AwaitedServlet. Invoking doPost method...");

		if (req.getParameter("user") == null || req.getParameter("videogame_id") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'user' and 'videogame_id' parameter");
			return;
		}

		int videogame;
		try {
			videogame = Integer.valueOf(req.getParameter("videogame_id"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("'videogame_id' must be an Integer");
			return;
		}
		String user = req.getParameter("user");
		String created_at = req.getParameter("created_at");

		if (created_at == null) {
			long millis = System.currentTimeMillis();
			created_at = new java.sql.Date(millis).toString();
		}

		if (!awaitedDAO.insertAwaited(new Awaited(user, videogame, created_at))) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal Server Error");
			return;
		}

		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Delete the awaited record with same videogame_id and user as those received
	 * as parameters
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("AwaitedServlet. Invoking doDelete method...");

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

		Awaited aw = new Awaited();
		aw.setUser(user);
		aw.setVideogame(videogame_id);

		if (!awaitedDAO.deleteAwaited(aw)) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().append("Awaited record not found");
			return;
		} else
			resp.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	public void destroy() {
		System.out.print("AwaitedServlet. Closing DB connection...");
		awaitedDAO.closeConnection();
		System.out.println("DONE.");
	}
}