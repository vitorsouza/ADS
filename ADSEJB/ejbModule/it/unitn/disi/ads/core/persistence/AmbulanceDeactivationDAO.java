package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import java.util.List;
import javax.ejb.Local;

/**
 * Data access object (DAO) interface for the domain class AmbulanceDeactivation.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface AmbulanceDeactivationDAO extends BaseDAO<AmbulanceDeactivation> {
	/**
	 * Retrieves all deactivations of a given ambulance.
	 *
	 * @param ambulance
	 *		The ambulance whose deactivations we are interested.
	 *
	 * @return
	 *		A list of AmbulanceDeactivation objects that are related to the given ambulance.
	 */
	List<AmbulanceDeactivation> retrieveByAmbulance(Ambulance ambulance);

	/**
	 * Retrieves the current (active) deactivation (the one that has not yet finished) of a given ambulance. An ambulance is supposed to have
	 * zero or one active deactivation.
	 *
	 * @param ambulance
	 *		The ambulance whose active deactivation we are interested.
	 *
	 * @return
	 *		An instance of AmbulanceDeactivation which is active and related to the given ambulance.
	 */
	AmbulanceDeactivation retrieveActiveByAmbulance(Ambulance ambulance);
}
