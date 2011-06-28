package it.unitn.disi.ads.core.domain;

/**
 * Domain class that represents the priority of an emergency call. The operator sets the priority so that dispatchers can service calls with
 * higher priority first.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public enum EmergencyCallPriority {
	LOW, MEDIUM, HIGH;

	/** Getter for id. */
	public Long getId() {
		return (long)this.ordinal();
	}
}
