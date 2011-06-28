package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.DriverAssignment;
import java.util.List;
import javax.ejb.Local;

/**
 * Data access object (DAO) interface for the domain class DriverAssignment.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface DriverAssignmentDAO extends BaseDAO<DriverAssignment> {
	/**
	 * Retrieves all driver assignments of a given ambulance.
	 *
	 * @param ambulance
	 *		The ambulance whose driver assignments we are interested.
	 *
	 * @return
	 *		A list of DriverAssignment objects that are related to the given ambulance.
	 */
	List<DriverAssignment> retrieveByAmbulance(Ambulance ambulance);

	/**
	 * Retrieves the current (active) assignment (the one that has not yet finished) of a given ambulance. An ambulance is supposed to have
	 * zero or one active assigment.
	 *
	 * @param ambulance
	 *		The ambulance whose active driver assignment we are interested.
	 *
	 * @return
	 *		An instance of DriverAssignment which is active and related to the given ambulance.
	 */
	DriverAssignment retrieveActiveByAmbulance(Ambulance ambulance);
}
