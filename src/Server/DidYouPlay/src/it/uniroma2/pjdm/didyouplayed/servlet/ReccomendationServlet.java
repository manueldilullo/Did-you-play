package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.ReccomendationDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Reccomendation;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReccomendationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ReccomendationDAO reccomendationDAO;
	private VideogameDAO videogameDAO;

	public ReccomendationServlet() {
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

		System.out.print("ReccomendationServlet. Opening DB connection...");

		try {
			reccomendationDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getReccomendationDao();
			videogameDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getVideogameDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * Given a String parameter named "receiver" Send to the client a JSONArray with
	 * all the reccomendations received by a user
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ReccomendationServlet. Invoking a doGet method.");

		if (req.getParameter("receiver") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Missing 'receiver' parameter");
			return;
		}

		ArrayList<Reccomendation> recs = reccomendationDAO.loadReccomendationByReceiver(req.getParameter("receiver"));
		if (recs == null) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal server issues");
			return;
		}
		
		JSONArray jsonContent = new JSONArray();
		for(Reccomendation rec: recs) {
			JSONObject jObj = new JSONObject();
			jObj.put("sender", rec.getSender());
			jObj.put("receiver", rec.getReceiver());
			jObj.put("created_at", rec.getCreatedAt());
			
			Videogame v = videogameDAO.loadVideogameById(rec.getVideogame());
			jObj.put("videogame", new JSONObject(v));
			
			jsonContent.put(jObj);
		}
		
		PrintWriter out = resp.getWriter();
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		out.print(jsonContent.toString());
		out.flush();
	}

	/**
	 * Given parameters String sender, String receiver and int videogame_id Add a
	 * Reccomendation object in the database
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ReccomendationServlet. Invoking a doPost method.");

		if (req.getParameter("sender") == null || req.getParameter("receiver") == null
				|| req.getParameter("videogame_id") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify 'sender','receiver' and 'videogame_id' parameters");
			return;
		}

		String sender = req.getParameter("sender");
		String receiver = req.getParameter("receiver");
		int videogame;
		try {
			videogame = Integer.valueOf(req.getParameter("videogame_id"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			resp.getWriter().append("'videogame_id' must be an Integer");
			return;
		}

		boolean isOk = reccomendationDAO.insertReccomendation(new Reccomendation(sender, receiver, videogame));

		if (!isOk) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal server error");
			return;
		}
		resp.setStatus(HttpServletResponse.SC_OK);

	}

	@Override
	public void destroy() {
		System.out.print("UserServlet. Closing DB connection...");
		reccomendationDAO.closeConnection();
		videogameDAO.closeConnection();
		System.out.println("DONE.");
	}
}
