package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.VideogameDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Videogame;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PopularServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	// DAOs
	VideogameDAO videogameDAO;

	public PopularServlet() {
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

		System.out.print("PopularServlet. Opening DB connection...");

		try {
			videogameDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getVideogameDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * Send to the client a JSONArray with most popular videogames in
	 * the db
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("PopularServlet. Invoking doGet method...");

		ArrayList<Videogame> popular = videogameDAO.loadPopular();

		if (popular == null) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal Server Error");
			return;
		}

		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		out.print(new JSONArray(popular));
		out.flush();
	}

	@Override
	public void destroy() {
		System.out.print("PopularServlet. Closing DB connection...");
		videogameDAO.closeConnection();
		System.out.println("DONE.");
	}
}
