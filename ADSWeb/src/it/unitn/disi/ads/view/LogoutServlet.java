package it.unitn.disi.ads.view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that serves to invalidate the user's session and, therefore, log her out of the system.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
	/** @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Destroys the session for this user.
		request.getSession(false).invalidate();

		// Redirects back to the initial page.
		response.sendRedirect(request.getContextPath());
	}
}
