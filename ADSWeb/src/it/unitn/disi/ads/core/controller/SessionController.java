package it.unitn.disi.ads.core.controller;

import br.com.engenhodesoftware.util.ejb3.controller.JSFAction;
import it.unitn.disi.ads.core.application.SessionInformation;
import it.unitn.disi.ads.core.application.exceptions.LoginFailedException;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.domain.EmployeeType;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Named
@SessionScoped
public class SessionController extends JSFAction {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(SessionController.class.getCanonicalName());

	/** The session information bean. */
	@Inject
	private SessionInformation sessionInformation;

	/** I/O parameter: username. */
	private String username;

	/** I/O parameter: password. */
	private String password;

	/** Getter for password. */
	public String getPassword() {
		return password;
	}

	/** Setter for password. */
	public void setPassword(String password) {
		this.password = password;
	}

	/** Getter for username. */
	public String getUsername() {
		return username;
	}

	/** Setter for username. */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Indicates if the user has already been identified.
	 *
	 * @return
	 *		<code>true</code> if the user is logged in, <code>false</code> otherwise.
	 */
	public boolean isLoggedIn() {
		return sessionInformation.getCurrentUser() != null;
	}

	/** Getter for currentUser. */
	public Employee getCurrentUser() {
		return sessionInformation.getCurrentUser();
	}

	/** Action method: system login. */
	public void login() {
		try {
			// Execute the login within the application.
			sessionInformation.login(username, password);

			// If all went well, show a confirmation message.
			logger.log(Level.FINER, "Login succeeded for {0}", username);
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "global.info.login");

		}
		catch (LoginFailedException e) {
			// If the login failed, report a problem in the authentication.
			logger.log(Level.INFO, "Login failed for {0}. Reason: {1}", new Object[] {username, e.getReason()});
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "global.error.login." + e.getReason());
		}
	}

	/** Provides the current date/time. */
	public Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * Indicates if the current user is an admin.
	 *
	 * @return <code>true</code> if the current user is an admin, <code>false</code> otherwise.
	 */
	public boolean isAdmin() {
		Employee user = sessionInformation.getCurrentUser();
		return (user != null) && (EmployeeType.ADMIN.equals(user.getType()));
	}

	/**
	 * Indicates if the current user is a dispatcher.
	 *
	 * @return <code>true</code> if the current user is a dispatcher, <code>false</code> otherwise.
	 */
	public boolean isDispatcher() {
		Employee user = sessionInformation.getCurrentUser();
		return (user != null) && (EmployeeType.DISPATCHER.equals(user.getType()));
	}

	/**
	 * Indicates if the current user is a driver.
	 *
	 * @return <code>true</code> if the current user is a driver, <code>false</code> otherwise.
	 */
	public boolean isDriver() {
		Employee user = sessionInformation.getCurrentUser();
		return (user != null) && (EmployeeType.DRIVER.equals(user.getType()));
	}

	/**
	 * Indicates if the current user is an operator.
	 *
	 * @return <code>true</code> if the current user is an operator, <code>false</code> otherwise.
	 */
	public boolean isOperator() {
		Employee user = sessionInformation.getCurrentUser();
		return (user != null) && (EmployeeType.OPERATOR.equals(user.getType()));
	}

	public void autoLoginAdmin() {
		setUsername("admin");
		setPassword("admin");
		login();
	}
	public void autoLoginOperator() {
		setUsername("operator");
		setPassword("operator");
		login();
	}
}
