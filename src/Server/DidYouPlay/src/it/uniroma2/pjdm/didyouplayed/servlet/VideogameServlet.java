package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VideogameServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String GET_VIDEOGAME = "single";
	private static final String GET_VIDEOGAME_LIST = "list";

	// DAOs
	VideogameDAO videogameDAO;

	public VideogameServlet() {
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

		System.out.print("VideogameServlet. Opening DB connection...");

		try {
			videogameDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getVideogameDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * IF <action>single</action> returns a JSONObject that contains the Videogame
	 * with <videogame_id> required 
	 * IF <action>list</action> returns a JSONArray
	 * that contains the Videogames which names contains <name> string required
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("VideogameServlet. Invoking doGet method...");
		PrintWriter out = resp.getWriter();
		
		if (req.getParameter("action") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.append("Missing 'action' parameter");
			return;
		}

		String action = req.getParameter("action");
		System.out.println("Action\t" + action);

		switch (action) {
		// Load Videogame by id
		case GET_VIDEOGAME:
			if (req.getParameter("id") == null) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.append("Must specify 'id' parameter");
				return;
			}

			int id = 0;
			try {
				id = Integer.valueOf(req.getParameter("id"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.append("'videogame_id' must be an Integer");
				return;
			}

			Videogame v = videogameDAO.loadVideogameById(id);
			if (v == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				out.append("Videogame not found");
				return;
			}

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			out.print(new JSONObject(v));
			out.flush();

			break;

		// Load Videogames by name
		case GET_VIDEOGAME_LIST:
			ArrayList<Videogame> videogames = videogameDAO.loadAll();
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			out.print(new JSONArray(videogames));
			out.flush();
			
			break;

		default:
			resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
			out.append("Action " + action + " doesn't exists");
			return;
		}
	}

	@Override
	public void destroy() {
		System.out.print("VideogameServlet. Closing DB connection...");
		videogameDAO.closeConnection();
		System.out.println("DONE.");
	}
}
