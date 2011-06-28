package it.unitn.disi.ads.core.application;

import br.com.engenhodesoftware.util.TextUtils;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.application.exceptions.LoginFailedException;
import it.unitn.disi.ads.core.application.exceptions.LoginFailedReason;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.persistence.EmployeeDAO;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Stateful, session-scoped EJB that holds session information of the user, such as the Employee object that represents the current user.
 * This class also provides some shortcut methods for determining the type of user.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@SessionScoped
public class SessionInformationBean implements SessionInformation {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(SessionInformationBean.class.getCanonicalName());

	/** The DAO for Employee objects. */
	@EJB
	private EmployeeDAO employeeDAO;

	/** The user that is currently logged in. */
	private Employee currentUser;

	/** @see it.unitn.disi.ads.core.application.SessionInformation#getCurrentUser() */
	@Override
	public Employee getCurrentUser() {
		return currentUser;
	}

	/** @see it.unitn.disi.ads.core.application.SessionInformation#login(java.lang.String, java.lang.String) */
	@Override
	public void login(String username, String password) throws LoginFailedException {
		try {
			// Retrieves the employee that owns the given username.
			logger.log(Level.FINE, "Authenticating user with username {0}...", username);
			Employee employee = employeeDAO.retrieveByUsername(username);

			// Creates the MD5 hash of the password for comparison.
			String md5pwd = TextUtils.produceMd5Hash(password);

			// Checks if the passwords match.
			String pwd = employee.getPassword();
			if ((pwd != null) && (pwd.equals(md5pwd))) {
				logger.log(Level.FINER, "Passwords match for user {0}.", username);

				// Checks also if the container authenticates the user. If not, it will thrown an exception.
				
				// FIXME: Removing the container authentication for now. Decide if it should come back later.
				// When the project migrated from GlassFish 3.0 to GlassFish 3.1 I started using application-scoped resources for
				// the persistent unit (database connection, see ADS / EarContent / META-INF / glassfish-resources.xml) and 
				// creating a realm to use the application-scoped data source doesn't seem to work. Should investigate the issue
				// further and decide what to do.
				
				//HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				//request.login(username, password);

				// Login successful. Registers the current user in the session.
				logger.log(Level.INFO, "Employee \"{0}\" successfully logged in.", username);
				currentUser = employee;
				pwd = password = null;
			}
			else {
				// Passwords don't match.
				logger.log(Level.INFO, "Employee \"{0}\" not logged in: password didn't match.", username);
				throw new LoginFailedException(LoginFailedReason.INCORRECT_PASSWORD);
			}
		}
		catch (PersistentObjectNotFoundException e) {
			// No spiritist was found with the given username.
			logger.log(Level.INFO, "User \"{0}\" not logged in: no registered employee found with given username.", username);
			throw new LoginFailedException(e, LoginFailedReason.UNKNOWN_USERNAME);
		}
		catch (MultiplePersistentObjectsFoundException e) {
			// Multiple spiritists were found with the same username.
			logger.log(Level.WARNING, "User \"{0}\" not logged in: there are more than one registered employee with the given username.", username);
			throw new LoginFailedException(e, LoginFailedReason.MULTIPLE_USERS);
		}
		catch (EJBTransactionRolledbackException e) {
			// Unknown original cause. Throw the EJB exception.
			logger.log(Level.WARNING, "User \"" + username + "\" not logged in: unknown cause.", e);
			throw e;
		}
		catch (NoSuchAlgorithmException e) {
			// No MD5 hash generation algorithm found by the JVM.
			logger.log(Level.SEVERE, "Logging in user \"" + username + "\" triggered an exception during MD5 hash generation.", e);
			throw new LoginFailedException(LoginFailedReason.MD5_ERROR);
		}
		catch (NullPointerException e) {
			// Couldn't retrieve the HTTP request. Somewhere along the way from the FacesContext class to the request some reference was null.
			logger.log(Level.SEVERE, "Logging in user \"" + username + "\" triggered an exception during acquisition of the HTTP request.", e);
			throw new LoginFailedException(LoginFailedReason.MD5_ERROR);
		}
		catch (ClassCastException e) {
			// Couldn't retrieve the HTTP request. Retrieved a request from the Faces context, but it was a different class than expected.
			logger.log(Level.SEVERE, "Logging in user \"" + username + "\" triggered an exception during acquisition of the HTTP request.", e);
			throw new LoginFailedException(LoginFailedReason.MD5_ERROR);
		}
//		catch (ServletException e) {
//			// User was not authenticated by the container.
//			logger.log(Level.WARNING, "User \"" + username + "\" not logged in: container returned an exception.", e);
//			throw new LoginFailedException(e, LoginFailedReason.CONTAINER_REJECTED);
//		}
	}
}
