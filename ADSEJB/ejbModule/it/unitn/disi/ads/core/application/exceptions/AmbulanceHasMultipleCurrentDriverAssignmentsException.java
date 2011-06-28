package it.unitn.disi.ads.core.application.exceptions;

/**
 * Exception representing the fact that the given ambulance has more than one current (active) driver assignment (an assignment that has not
 * yet ended). An ambulance is supposed to have at most one current assignment.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public class AmbulanceHasMultipleCurrentDriverAssignmentsException extends Exception {
	/** @see java.lang.Exception#Exception(java.lang.Throwable) */
	public AmbulanceHasMultipleCurrentDriverAssignmentsException(Throwable cause) {
		super(cause);
	}
}
