package it.unitn.disi.ads.core.application;

import it.unitn.disi.ads.core.application.exceptions.LoginFailedException;
import it.unitn.disi.ads.core.domain.Employee;
import java.io.Serializable;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public interface SessionInformation extends Serializable {
	/**
	 * TODO: documentation pending.
	 * 
	 * @return
	 */
	Employee getCurrentUser();

	/**
	 * Authenticates a user given his username and password. If the user is correctly authenticated, she should be
	 * available as an Employee object through the getCurrentUser() method.
	 *
	 * @param username
	 *		The username that identifies the user in the system.
	 * @param password
	 *		The password that authenticates the user.
	 *
	 * @throws LoginFailedException
	 *		If the username is unknown or the password is incorrect.
	 *
	 * @see it.unitn.disi.ads.application.LoginServiceLocal#getCurrentUser()
	 */
	void login(String username, String password) throws LoginFailedException;
}
