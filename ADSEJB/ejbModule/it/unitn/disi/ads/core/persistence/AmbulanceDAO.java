package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.domain.Ambulance;
import javax.ejb.Local;

/**
 * Data access object (DAO) interface for the domain class Ambulance.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface AmbulanceDAO extends BaseDAO<Ambulance> {
	/**
	 * Retrieves the ambulance that has the given number.
	 *
	 * @param number
	 *		The number of the ambulance we're interested in.
	 *
	 * @return
	 *		An instance of Ambulance that has the given number.
	 */
	public Ambulance retrieveByNumber(Integer number) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;

	/**
	 * Retrieves the ambulance that has the given license plate.
	 *
	 * @param licensePlate
	 *		The license plate of the ambulance we're interested in.
	 *
	 * @return
	 *		An instance of Ambulance that has the given license plate.
	 */
	public Ambulance retrieveByLicensePlate(String licensePlate) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;
}
