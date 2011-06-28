package it.unitn.disi.ads.core.application.exceptions;

/**
 * Exception that represents the fact that the given ambulance has more than one current (active) deactivations (a deactivation that has not
 * yet ended). An ambulance is supposed to have only one current deactivation.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public class AmbulanceHasMultipleCurrentDeactivationsException extends Exception {
	/** @see java.lang.Exception#Exception(java.lang.Throwable) */
	public AmbulanceHasMultipleCurrentDeactivationsException(Throwable cause) {
		super(cause);
	}
}
