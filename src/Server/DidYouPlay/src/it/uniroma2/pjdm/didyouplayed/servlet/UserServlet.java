package it.uniroma2.pjdm.didyouplayed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;

import it.uniroma2.pjdm.didyouplayed.db.DAOFactoryJDBC;
import it.uniroma2.pjdm.didyouplayed.db.dao.LoginDAO;
import it.uniroma2.pjdm.didyouplayed.db.dao.UserDAO;
import it.uniroma2.pjdm.didyouplayed.entity.Login;
import it.uniroma2.pjdm.didyouplayed.entity.User;
import it.uniroma2.pjdm.didyouplayed.utils.BasicAuth;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// GET ACTIONS
	private static final String ACTION_AUTH = "authorization";
	private static final String ACTION_REGISTER = "register";

	// DAOs
	private LoginDAO loginDAO;
	private UserDAO userDAO;

	public UserServlet() {
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

		System.out.print("UserServlet. Opening DB connection...");

		try {
			loginDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getLoginDao();
			userDAO = DAOFactoryJDBC.getInstance(ip, port, dbName, userName, password).getUserDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("DONE.");
	}

	/**
	 * If "username" parameter is specified, returns the Users that contains that in
	 * their username ex. if parameter is "luke" returns User with following
	 * username ["luke", "luke123", "MrLuke45", ...]
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("UserServlet. Invoking a doGet method...");

		ArrayList<User> users = userDAO.loadAllUsers();

		if (users == null) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal Server Error");
			return;
		}

		resp.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JSONArray usrJson = new JSONArray(users);
		out.print(usrJson.toString());
		out.flush();
	}

	/**
	 * Execute Authorization or Register process according to the request specified
	 * in "action" parameter
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("UserServlet. Invoking a doPost method...");
		if (req.getParameter("action") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Missing 'action' parameter");
			return;
		}

		String action = req.getParameter("action");

		int result = 0;

		switch (action) {
		case ACTION_AUTH:
			result = doLogin(req);
			break;

		case ACTION_REGISTER:
			result = doRegister(req);
			break;
		}

		resp.setStatus(result);
		switch(result) {
			case HttpServletResponse.SC_NOT_FOUND:
				resp.getWriter().append("Can't find user with this credentials");
			break;
			case HttpServletResponse.SC_BAD_REQUEST:
				resp.getWriter().append("Can't extract credentials");
			break;
			case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
				resp.getWriter().append("Error executing " + action + " action");
			break;
		}
			
	}

	/**
	 * This function perform a user login action and updates response status code
	 * according to the result of the operation
	 * 
	 * @param req
	 * @return the status code for the result of the operation
	 */
	private int doLogin(HttpServletRequest req) {
		System.out.println("Performing Login...");
		String[] credentials = BasicAuth.credentialsWithBasicAuthentication(req);

		if (credentials == null)
			return HttpServletResponse.SC_BAD_REQUEST;

		Login login = loginDAO.getLoginByCredentials(credentials[0], credentials[1]);

		if (!(login == null))
			return HttpServletResponse.SC_OK;
		else
			return HttpServletResponse.SC_NOT_FOUND;
	}

	/**
	 * This function perform a user register action and updates response status code
	 * according to the result of the operation
	 * 
	 * @param req
	 * @param resp
	 * @return the status code for the result of the operation
	 */
	private int doRegister(HttpServletRequest req) {

		System.out.println("Performing Register...");

		String name = req.getParameter("name");
		String surname = req.getParameter("surname");
		String email = req.getParameter("email");

		String birthdate = req.getParameter("birthdate");
		String gender = req.getParameter("gender");

		String username = req.getParameter("username");
		String password = req.getParameter("password");

		User usr = new User(name, surname, username, email, birthdate, gender);
		Login login = new Login(username, password);

		if (!(usr.isValid() && login.isValid()))
			return HttpServletResponse.SC_BAD_REQUEST;

		if (!(userDAO.insertUser(usr) && loginDAO.insertLogin(login)))
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

		return HttpServletResponse.SC_OK;
	}

	/**
	 * After checked if credentials in Authorization parameter are correct, update
	 * the old user login replacing old password with the password in "newPassword"
	 * parameter
	 * 
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("UserServlet. Invoking a doPut method...");

		if (req.getHeader("Authorization") == null || req.getParameter("newPassword") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify username e password");
			return;
		}

		int sc = this.doLogin(req);

		if (sc != HttpServletResponse.SC_OK) {
			resp.setStatus(sc);
			return;
		}

		String username = BasicAuth.credentialsWithBasicAuthentication(req)[0];

		Login login = new Login(username, req.getParameter("newPassword"));

		if (!loginDAO.updateLogin(login)) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal server error");
		} else
			resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Perform a deleteUser action on the User with username specified in "username"
	 * parameter
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("UserServlet. Invoking a doDelete method.");
		if (req.getParameter("username") == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().append("Must specify username");
			return;
		}

		String username = req.getParameter("username");

		User oldUsr = userDAO.loadUserByUsername(username);
		if (oldUsr == null || !oldUsr.getUsername().equalsIgnoreCase(username)) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.getWriter().append("Cannot find user in db");
			return;
		}

		boolean isOk = userDAO.deleteUser(username);

		if (isOk) {
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append("Internal Server error");
		}
	}

	@Override
	public void destroy() {
		System.out.print("UserServlet. Closing DB connection...");
		userDAO.closeConnection();
		loginDAO.closeConnection();
		System.out.println("DONE.");
	}
}
