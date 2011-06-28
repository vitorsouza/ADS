package it.unitn.disi.ads.core.application.exceptions;

/**
 * Exception representing the fact that the given ambulance has no current (active) driver assignment (an assignment that has not yet
 * ended). In certain contexts, an ambulance is supposed to be currently assigned to a driver.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
public class AmbulanceHasNoCurrentDriverAssignmentsException extends Exception {
}
