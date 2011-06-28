package it.unitn.disi.ads.core.application.exceptions;

/**
 * Exception representing the fact that the given ambulance has no current (active) deactivation (a deactivation that has not yet ended). In
 * certain contexts, an ambulance is supposed to be currently deactivated.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public class AmbulanceHasNoCurrentDeactivationException extends Exception {
}
