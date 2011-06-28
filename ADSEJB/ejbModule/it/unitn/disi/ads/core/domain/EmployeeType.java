package it.unitn.disi.ads.core.domain;

/**
 * Domain class that represents the types of employee. An employee can be an operator (responds to emergency calls), a dispatcher (gets call
 * reports from the operator, selects and dispatches the ambulances to the emergency sites), a driver (drives ambulances) or an
 * administrator (administers the ADS).
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public enum EmployeeType {
	OPERATOR, DISPATCHER, DRIVER, ADMIN;

	/** Getter for id. */
	public Long getId() {
		return (long)this.ordinal();
	}
}
