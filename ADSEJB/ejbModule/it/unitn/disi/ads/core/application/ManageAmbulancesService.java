package it.unitn.disi.ads.core.application;

import br.com.engenhodesoftware.util.ejb3.application.CrudServiceLocal;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDeactivationsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDeactivationException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.EmployeeIsNotDriverException;
import javax.ejb.Local;

import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import it.unitn.disi.ads.core.domain.DriverAssignment;
import it.unitn.disi.ads.core.domain.Employee;
import java.util.List;

/**
 * Local EJB interface for the service class (application class) that implements the use case "Manage Employees". This interface extends and
 * gets most of its methods from the CrudServiceLocal interface from the Engenho de Software CRUD framework for EJB3. Some other methods are
 * specific for this use case and are defined for this interface.
 *
 * @see br.com.engenhodesoftware.util.ejb3.application.CrudServiceLocal
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Local
public interface ManageAmbulancesService extends CrudServiceLocal<Ambulance> {
	/**
	 * Retrieves the active deactivations (deactivation that have not yet ended) for the given ambulance, if there is any. There should be at
	 * most one active deactivation for each ambulance.
	 * 
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		If it exists, the deactivation associated with the given ambulance that has not yet ended. Otherwise, returns null.
	 *
	 * @throws AmbulanceHasMultipleCurrentDeactivationsException
	 *		If there's more than one active deactivation for the given ambulance.
	 */
	AmbulanceDeactivation retrieveActiveDeactivation(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDeactivationsException;

	/**
	 * Retrieves all deactivations related to the given ambulance (the ambulance deactivation history).
	 * 
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		A list of all deactivations related to the given ambulance.
	 */
	List<AmbulanceDeactivation> retrieveAllDeactivations(Ambulance ambulance);

	/**
	 * Deactivates the given ambulance using the given reason.
	 * 
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 * @param reason
	 *		The reason for deactivating the ambulance.
	 *
	 * @return
	 *		The AmbulanceDeactivation object created by this method to represent the deactivation of the ambulance.
	 */
	public AmbulanceDeactivation deactivateAmbulance(Ambulance ambulance, String reason);

	/**
	 * Ends the current (active) deactivation
	 * 
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		The AmbulanceDeactivation object that represents the deactivation that has been ended by this method.
	 *
	 * @throws AmbulanceHasMultipleCurrentDeactivationsException
	 *		If there's more than one active deactivation for the given ambulance.
	 * @throws AmbulanceHasNoCurrentDeactivationException
	 *		If the ambulance has no current (active) deactivation.
	 */
	public AmbulanceDeactivation endCurrentDeactivation(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDeactivationsException, AmbulanceHasNoCurrentDeactivationException;

	/**
	 * Retrieves the active driver assignment (assignment that have not yet ended) for the given ambulance, if there is any. There should be
	 * at most one active assignment for each ambulance.
	 *
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		If it exists, the driver assignment associated with the given ambulance that has not yet ended. Otherwise, returns null.
	 *
	 * @throws AmbulanceHasMultipleCurrentDriverAssignmentsException
	 *		If there's more than one active driver assignment for the given ambulance.
	 */
	DriverAssignment retrieveActiveAssignment(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDriverAssignmentsException;

	/**
	 * Retrieves all driver assignment related to the given ambulance (the ambulance assignment history).
	 *
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		A list of all assignments related to the given ambulance.
	 */
	List<DriverAssignment> retrieveAllAssignments(Ambulance ambulance);

	/**
	 * Assigns the given ambulance to the given driver, starting today. If the ambulance is currently assigned to someone else, end that
	 * assignment (also effectively today).
	 *
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 * @param driver
	 *		A driver that has already been registered in the system, to whom the ambulance will be assigned.
	 *
	 * @return
	 *		The DriverAssignment object created by this method to represent the assignment of the ambulance.
	 *
	 * @throws AmbulanceHasMultipleCurrentDriverAssignmentsException
	 *		If there's more than one active driver assignment for the given ambulance.
	 * @throws EmployeeIsNotDriverException
	 *		If the given employee is not of type DRIVER.
	 *
	 * @see it.unitn.disi.ads.core.domain.EmployeeType
	 */
	public DriverAssignment assignAmbulance(Ambulance ambulance, Employee driver) throws AmbulanceHasMultipleCurrentDriverAssignmentsException, EmployeeIsNotDriverException;

	/**
	 * Ends the current (active) driver assignment for the given ambulance, assigning it to nobody.
	 *
	 * @param ambulance
	 *		An ambulance that has already been registered in the system.
	 *
	 * @return
	 *		The DriverAssignment object that represents the assignment that has been ended by this method.
	 *
	 * @throws AmbulanceHasMultipleCurrentDriverAssignmentsException
	 *		If there's more than one active driver assignment for the given ambulance.
	 * @throws AmbulanceHasNoCurrentDriverAssignmentsException
	 *		If the ambulance has no current (active) driver assignment.
	 */
	public DriverAssignment endCurrentAssignment(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDriverAssignmentsException, AmbulanceHasNoCurrentDriverAssignmentsException;
}
